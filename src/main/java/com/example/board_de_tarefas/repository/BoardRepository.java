package com.example.board_de_tarefas.repository;

import com.example.board_de_tarefas.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {}
