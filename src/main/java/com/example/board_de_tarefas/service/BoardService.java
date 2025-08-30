package com.example.board_de_tarefas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board_de_tarefas.model.Board;
import com.example.board_de_tarefas.model.Column;
import com.example.board_de_tarefas.model.enums.ColumnType;
import com.example.board_de_tarefas.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Board createBoard(String name, String initialCol, String finalCol, String cancelledCol, List<String> pendingCols) {
        Board board = new Board();
        board.setName(name);

        int order = 0;
        board.getColumns().add(createColumn(board, initialCol, order++, ColumnType.INITIAL));
        for (String pendingColName : pendingCols) {
            board.getColumns().add(createColumn(board, pendingColName, order++, ColumnType.PENDING));
        }
        board.getColumns().add(createColumn(board, finalCol, order++, ColumnType.FINAL));
        board.getColumns().add(createColumn(board, cancelledCol, order, ColumnType.CANCELLATION));

        return boardRepository.save(board);
    }

    private Column createColumn(Board board, String name, int order, ColumnType type) {
        Column column = new Column();
        column.setBoard(board);
        column.setName(name);
        column.setOrderPosition(order);
        column.setType(type);
        return column;
    }

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }

    public Board findBoardById(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Board com ID " + id + " n��o encontrado."));
    }
}