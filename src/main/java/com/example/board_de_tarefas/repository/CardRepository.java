package com.example.board_de_tarefas.repository;

import com.example.board_de_tarefas.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {}
