package com.example.board_de_tarefas.shell;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.example.board_de_tarefas.model.Board;
import com.example.board_de_tarefas.service.BoardService;

import lombok.RequiredArgsConstructor;

@ShellComponent
@RequiredArgsConstructor
public class MainMenuCommands {

    private final BoardService boardService;
    private final BoardMenuCommands boardMenuCommands; // Usado para gerenciar o estado do board ativo

    /**
     * Este comando serve como um guia rápido, mostrando os comandos principais.
     */
    @ShellMethod(key = "menu", value = "Exibe a lista de comandos principais disponíveis.")
    public String menu() {
        return """
               --- Comandos Principais ---
               
               • boards                  - Lista todos os boards existentes.
               • criar-board <nome>      - Cria um novo board com colunas padrão.
               • selecionar-board <id>   - Seleciona um board para manipulação com os comandos 'card-*'.
               • excluir-board <id>      - Exclui um board permanentemente.
               
               Para ver os comandos de manipulação de cards, primeiro selecione um board.
               """;
    }

    /**
     * Lista todos os boards, permitindo que o usuário veja os IDs disponíveis.
     */
    @ShellMethod(key = "boards", value = "Lista todos os boards disponíveis.")
    public String listBoards() {
        List<Board> boards = boardService.getAllBoards();
        if (boards.isEmpty()) {
            return "Nenhum board encontrado. Use 'criar-board <nome>' para começar.";
        }
        
        String boardList = boards.stream()
                .map(board -> String.format("  ID: %-4d | Nome: %s", board.getId(), board.getName()))
                .collect(Collectors.joining("\n"));

        return "--- Boards Disponíveis ---\n" + boardList;
    }

    /**
     * Cria um novo board. O nome é passado como argumento.
     * As colunas são criadas com nomes padrão para simplificar.
     */
    @ShellMethod(key = "criar-board", value = "Cria um novo board com colunas padrão.")
    public String createBoard(String nome) {
        try {
            // Para simplificar, as colunas padrão são definidas aqui.
            // Poderiam ser parâmetros opcionais com @ShellOption em uma versão mais avançada.
            Board newBoard = boardService.createBoard(nome, "A Fazer", "Feito", "Cancelado", List.of("Em Andamento"));
            return String.format("✅ Board '%s' (ID: %d) criado com sucesso!", newBoard.getName(), newBoard.getId());
        } catch (Exception e) {
            return String.format("❌ Erro ao criar board: %s", e.getMessage());
        }
    }

    /**
     * Define qual board será o alvo dos comandos de manipulação de cards.
     */
    @ShellMethod(key = "selecionar-board", value = "Seleciona um board para se tornar o board ativo.")
    public String selectBoard(Long id) {
        try {
            Board board = boardService.findBoardById(id);
            boardMenuCommands.setCurrentBoard(board); // Define o board ativo na outra classe de comando
            return String.format("✅ Board '%s' (ID: %d) agora está ativo.", board.getName(), board.getId());
        } catch (Exception e) {
            return String.format("❌ Erro: %s", e.getMessage());
        }
    }

    /**
     * Exclui um board do sistema.
     */
    @ShellMethod(key = "excluir-board", value = "Exclui um board pelo seu ID.")
    public String deleteBoard(Long id) {
        try {
            // Em uma aplicação real, aqui haveria uma confirmação (y/n).
            // O Spring Shell tem suporte para isso, mas vamos manter simples por agora.
            boardService.deleteBoard(id);
            return String.format("✅ Board ID %d foi excluído com sucesso.", id);
        } catch (Exception e) {
            return String.format("❌ Erro ao excluir board: %s", e.getMessage());
        }
    }
}