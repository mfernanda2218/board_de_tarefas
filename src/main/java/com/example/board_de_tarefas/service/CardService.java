package com.example.board_de_tarefas.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board_de_tarefas.model.BlockageLog;
import com.example.board_de_tarefas.model.Card;
import com.example.board_de_tarefas.model.CardMovement;
import com.example.board_de_tarefas.model.Column;
import com.example.board_de_tarefas.model.enums.BlockageType;
import com.example.board_de_tarefas.model.enums.ColumnType;
import com.example.board_de_tarefas.repository.BlockageLogRepository;
import com.example.board_de_tarefas.repository.CardMovementRepository;
import com.example.board_de_tarefas.repository.CardRepository;
import com.example.board_de_tarefas.repository.ColumnRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final ColumnRepository columnRepository;
    private final CardMovementRepository cardMovementRepository;
    private final BlockageLogRepository blockageLogRepository;

    /**
     * Cria um novo card na coluna inicial do board especificado.
     */
    public Card createCard(Long boardId, String title, String description) {
        Column initialColumn = columnRepository.findByBoardIdAndType(boardId, ColumnType.INITIAL)
                .orElseThrow(() -> new EntityNotFoundException("Coluna inicial não encontrada para o board ID: " + boardId));

        Card card = new Card();
        card.setTitle(title);
        card.setDescription(description);
        card.setCreationDate(LocalDateTime.now());
        card.setBlocked(false);
        card.setColumn(initialColumn);

        Card savedCard = cardRepository.save(card);
        // Cria o primeiro registro no histórico de movimentação
        logNewMovement(savedCard, initialColumn);
        return savedCard;
    }

    /**
     * Move um card para a próxima coluna em ordem sequencial.
     */
    public Card moveCard(Long cardId) {
        Card card = findCardById(cardId);
        validateCardCanBeMoved(card);

        Column currentColumn = card.getColumn();
        if (currentColumn.getType() == ColumnType.FINAL || currentColumn.getType() == ColumnType.CANCELLATION) {
            throw new IllegalStateException("Não é possível mover cards que já estão na coluna final ou de cancelamento.");
        }

        int nextPosition = currentColumn.getOrderPosition() + 1;
        Column nextColumn = columnRepository.findByBoardIdAndOrderPosition(currentColumn.getBoard().getId(), nextPosition)
                .orElseThrow(() -> new EntityNotFoundException("Nenhuma coluna seguinte encontrada."));

        updateAndLogMovement(card, nextColumn);
        return cardRepository.save(card);
    }

    /**
     * Move um card de qualquer coluna (exceto a final) para a coluna de cancelamento.
     */
    public Card cancelCard(Long cardId) {
        Card card = findCardById(cardId);
        validateCardCanBeMoved(card);

        if (card.getColumn().getType() == ColumnType.FINAL) {
            throw new IllegalStateException("Não é possível cancelar um card que já foi finalizado.");
        }

        Column cancellationColumn = columnRepository.findByBoardIdAndType(card.getColumn().getBoard().getId(), ColumnType.CANCELLATION)
                .orElseThrow(() -> new EntityNotFoundException("Coluna de cancelamento não encontrada."));

        updateAndLogMovement(card, cancellationColumn);
        return cardRepository.save(card);
    }

    /**
     * Bloqueia um card, impedindo sua movimentação, e registra o motivo.
     */
    public Card blockCard(Long cardId, String reason) {
        Card card = findCardById(cardId);
        if (card.isBlocked()) {
            throw new IllegalStateException("O card " + cardId + " já está bloqueado.");
        }
        card.setBlocked(true);

        BlockageLog log = new BlockageLog();
        log.setCard(card);
        log.setReason(reason);
        log.setType(BlockageType.BLOCK);
        log.setTimestamp(LocalDateTime.now());
        blockageLogRepository.save(log);

        return cardRepository.save(card);
    }

    /**
     * Desbloqueia um card, permitindo sua movimentação, e registra o motivo.
     */
    public Card unblockCard(Long cardId, String reason) {
        Card card = findCardById(cardId);
        if (!card.isBlocked()) {
            throw new IllegalStateException("O card " + cardId + " não está bloqueado.");
        }
        card.setBlocked(false);

        BlockageLog log = new BlockageLog();
        log.setCard(card);
        log.setReason(reason);
        log.setType(BlockageType.UNBLOCK);
        log.setTimestamp(LocalDateTime.now());
        blockageLogRepository.save(log);

        return cardRepository.save(card);
    }

    // MÉTODOS AUXILIARES

    private Card findCardById(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card com ID " + cardId + " não encontrado."));
    }

    private void validateCardCanBeMoved(Card card) {
        if (card.isBlocked()) {
            throw new IllegalStateException("O card '" + card.getTitle() + "' está bloqueado e não pode ser movido.");
        }
    }
    
    private void updateAndLogMovement(Card card, Column newColumn) {
        // Encontra o registro de movimento atual para registrar a hora de saída
        cardMovementRepository.findFirstByCardIdAndExitTimeIsNullOrderByEntryTimeDesc(card.getId())
                .ifPresent(lastMovement -> {
                    lastMovement.setExitTime(LocalDateTime.now());
                    cardMovementRepository.save(lastMovement);
                });
        
        // Atualiza a coluna do card
        card.setColumn(newColumn);
        
        // Cria o novo registro de movimento para a nova coluna
        logNewMovement(card, newColumn);
    }

    private void logNewMovement(Card card, Column newColumn) {
        CardMovement movement = new CardMovement();
        movement.setCard(card);
        movement.setColumn(newColumn);
        movement.setEntryTime(LocalDateTime.now());
        movement.setExitTime(null); // Fica nulo até ser movido novamente
        cardMovementRepository.save(movement);
    }
}