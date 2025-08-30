package com.example.board_de_tarefas.service;

import com.example.board_de_tarefas.model.BlockageLog;
import com.example.board_de_tarefas.model.Card;
import com.example.board_de_tarefas.model.CardMovement;
import com.example.board_de_tarefas.model.Column;
import com.example.board_de_tarefas.model.enums.BlockageType;
import com.example.board_de_tarefas.model.enums.ColumnType;
import com.example.board_de_tarefas.repository.ColumnRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // Apenas leitura, não modifica dados
@RequiredArgsConstructor
public class ReportService {

    private final ColumnRepository columnRepository;

    public String generateCompletionReport(Long boardId) {
        Column finalColumn = columnRepository.findByBoardIdAndType(boardId, ColumnType.FINAL)
                .orElseThrow(() -> new EntityNotFoundException("Coluna Final não encontrada para o board ID: " + boardId));

        List<Card> completedCards = finalColumn.getCards();

        if (completedCards.isEmpty()) {
            return "Nenhum card foi finalizado neste board ainda.";
        }

        StringBuilder report = new StringBuilder();
        report.append("--- Relatório de Tempo de Conclusão ---\n");

        for (Card card : completedCards) {
            report.append("\n============================================\n");
            report.append("Card: '").append(card.getTitle()).append("'\n");
            report.append("--------------------------------------------\n");

            List<CardMovement> movements = card.getMovementHistory();
            movements.sort(Comparator.comparing(CardMovement::getEntryTime));

            Duration totalDuration = Duration.ZERO;

            for (CardMovement movement : movements) {
                // O tempo de saída é a referência para o tempo gasto na coluna
                if (movement.getExitTime() != null) {
                    Duration durationInColumn = Duration.between(movement.getEntryTime(), movement.getExitTime());
                    totalDuration = totalDuration.plus(durationInColumn);
                    report.append(String.format(" -> Coluna '%s': %s\n",
                            movement.getColumn().getName(), formatDuration(durationInColumn)));
                } else {
                     // Caso do card na coluna final, que não tem exitTime
                    report.append(String.format(" -> Chegou em '%s' em: %s\n",
                            movement.getColumn().getName(), movement.getEntryTime().toLocalDate()));
                }
            }
            
            if (!movements.isEmpty()) {
                LocalDateTime startTime = movements.get(0).getEntryTime();
                LocalDateTime endTime = movements.get(movements.size() - 1).getEntryTime(); // A "conclusão" é a entrada na última coluna
                totalDuration = Duration.between(startTime, endTime);
            }

            report.append("--------------------------------------------\n");
            report.append("Tempo Total de Ciclo: ").append(formatDuration(totalDuration)).append("\n");
        }
        report.append("============================================\n");

        return report.toString();
    }

    /**
     * Gera um relatório de todos os bloqueios ocorridos nos cards de um board.
     * @param boardId O ID do board para gerar o relatório.
     * @return Uma String formatada com o relatório.
     */
    public String generateBlockageReport(Long boardId) {
        List<Column> columns = columnRepository.findByBoardId(boardId);
        List<Card> allCards = columns.stream()
                .flatMap(column -> column.getCards().stream())
                .distinct()
                .collect(Collectors.toList());

        StringBuilder report = new StringBuilder();
        report.append("--- Relatório de Bloqueios ---\n");

        boolean hasBlockages = false;

        for (Card card : allCards) {
            List<BlockageLog> logs = card.getBlockageLogs();
            if (logs.isEmpty()) {
                continue;
            }

            hasBlockages = true;
            report.append("\n============================================\n");
            report.append("Card: '").append(card.getTitle()).append("'\n");
            report.append("--------------------------------------------\n");
            
            logs.sort(Comparator.comparing(BlockageLog::getTimestamp));

            for (int i = 0; i < logs.size(); i++) {
                BlockageLog currentLog = logs.get(i);
                if (currentLog.getType() == BlockageType.BLOCK) {
                    report.append("  -> Bloqueado em: ").append(currentLog.getTimestamp().toLocalDate()).append("\n");
                    report.append("     Motivo: ").append(currentLog.getReason()).append("\n");

                    // Procura pelo desbloqueio correspondente
                    if (i + 1 < logs.size() && logs.get(i + 1).getType() == BlockageType.UNBLOCK) {
                        BlockageLog unblockLog = logs.get(i + 1);
                        Duration blockageDuration = Duration.between(currentLog.getTimestamp(), unblockLog.getTimestamp());
                        report.append("     Desbloqueado em: ").append(unblockLog.getTimestamp().toLocalDate()).append("\n");
                        report.append("     Motivo: ").append(unblockLog.getReason()).append("\n");
                        report.append("     Tempo Bloqueado: ").append(formatDuration(blockageDuration)).append("\n");
                        i++; // Pula o log de desbloqueio já processado
                    } else {
                        // O card ainda está bloqueado
                        Duration currentBlockageDuration = Duration.between(currentLog.getTimestamp(), LocalDateTime.now());
                        report.append("     Status: AINDA BLOQUEADO\n");
                        report.append("     Tempo Bloqueado até agora: ").append(formatDuration(currentBlockageDuration)).append("\n");
                    }
                }
            }
        }
        
        if (!hasBlockages) {
            return "Nenhum bloqueio registrado para os cards deste board.";
        }
        
        report.append("============================================\n");
        return report.toString();
    }

    /**
     * Formata um objeto Duration para uma string legível (dias, horas, minutos, segundos).
     */
    private String formatDuration(Duration duration) {
        if (duration == null || duration.isZero()) {
            return "0 segundos";
        }
        
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        StringBuilder formatted = new StringBuilder();
        if (days > 0) formatted.append(days).append("d ");
        if (hours > 0) formatted.append(hours).append("h ");
        if (minutes > 0) formatted.append(minutes).append("m ");
        if (seconds > 0 || formatted.length() == 0) formatted.append(seconds).append("s");

        return formatted.toString().trim();
    }
}
