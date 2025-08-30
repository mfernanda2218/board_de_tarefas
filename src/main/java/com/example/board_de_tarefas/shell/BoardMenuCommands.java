package com.example.board_de_tarefas.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.example.board_de_tarefas.model.Board;
import com.example.board_de_tarefas.service.BoardService;
import com.example.board_de_tarefas.service.CardService;
import com.example.board_de_tarefas.service.ReportService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Setter
@ShellComponent
@RequiredArgsConstructor
public class BoardMenuCommands {

    private final CardService cardService;
    private final ReportService reportService;
    private final BoardService boardService;

    private Board currentBoard;

    // Setter para que MainMenuCommands possa definir o board
    public void setCurrentBoard(Board board) {
        this.currentBoard = board;
    }

    // Este método especial desativa os comandos de card se nenhum board for selecionado
    @ShellMethod(key = "menu-board", value = "Exibe o menu de comandos disponíveis para o board ativo.")
    public String showBoardMenu() {
        return """
            --- Menu do Board Ativo ---
            1. ver-board                 - Exibe o estado atual do board
            2. criar-card <titulo> <desc> - Cria um novo card
            3. mover-card <id>           - Move um card para a próxima coluna
            4. relatorio-conclusao       - Relatório de tempo de conclusão
            5. relatorio-bloqueios       - Relatório de bloqueios
            Digite o comando desejado acima para executar a ação.
            """;
    }

    @ShellMethod(key = "ver-board", value = "Exibe o estado atual do board selecionado.")
    public String displayBoard() {
        // Recarrega o board para ter a visão mais atual
        currentBoard = boardService.findBoardById(currentBoard.getId());

        StringBuilder boardView = new StringBuilder();
        boardView.append(String.format("--- Board Ativo: %s (ID: %d) ---\n", currentBoard.getName(), currentBoard.getId()));
        // ... (A lógica de displayBoard que já tínhamos, adaptada para retornar String)
        return boardView.toString();
    }

    @ShellMethod(key = "criar-card", value = "Cria um novo card no board ativo.")
    public String createCard(String titulo, String descricao) {
        cardService.createCard(currentBoard.getId(), titulo, descricao);
        return String.format("Card '%s' criado com sucesso no board '%s'.", titulo, currentBoard.getName());
    }

    @ShellMethod(key = "mover-card", value = "Move um card para a próxima coluna.")
    public String moveCard(Long id) {
        try {
            cardService.moveCard(id);
            return "Card ID " + id + " movido com sucesso.";
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

    @ShellMethod(key = "relatorio-conclusao", value = "Gera o relatório de tempo de conclusão para o board ativo.")
    public String generateCompletionReport() {
        return reportService.generateCompletionReport(currentBoard.getId());
    }

    @ShellMethod(key = "relatorio-bloqueios", value = "Gera o relatório de bloqueios para o board ativo.")
    public String generateBlockageReport() {
        return reportService.generateBlockageReport(currentBoard.getId());
    }
}
