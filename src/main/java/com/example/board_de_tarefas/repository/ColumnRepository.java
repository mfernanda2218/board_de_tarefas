package com.example.board_de_tarefas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.board_de_tarefas.model.Column;
import com.example.board_de_tarefas.model.enums.ColumnType;

public interface ColumnRepository extends JpaRepository<Column, Long> {
    // Busca uma coluna por tipo dentro de um board específico
    Optional<Column> findByBoardIdAndType(Long boardId, ColumnType type);
    
    // Busca uma coluna pela ordem dentro de um board específico
    Optional<Column> findByBoardIdAndOrderPosition(Long boardId, int orderPosition);

    List<Column> findByBoardId(Long boardId);
}
