package com.example.board_de_tarefas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.board_de_tarefas.model.CardMovement;

public interface CardMovementRepository extends JpaRepository<CardMovement, Long> {
    // Busca o último registro de movimento de um card (aquele que ainda não tem data de saída)
    Optional<CardMovement> findFirstByCardIdAndExitTimeIsNullOrderByEntryTimeDesc(Long cardId);
}