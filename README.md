# Board de Gerenciamento de Tarefas (CLI)

Uma aplicação de linha de comando (CLI) para criar e gerenciar boards de tarefas customizáveis, similar ao Trello ou Kanban, construída com Java e Spring Boot.

## Funcionalidades

  - **Gerenciamento de Boards:** Crie, liste, selecione e exclua múltiplos boards.
  - **Estrutura de Colunas Customizável:** Cada board é composto por colunas (Inicial, Pendente, Final, Cancelamento).
  - **Gerenciamento de Cards:** Crie, mova, cancele, bloqueie e desbloqueie tarefas (cards) dentro de um board.
  - **Regras de Movimentação:** Cards movem-se sequencialmente entre as colunas, com regras específicas para bloqueio e cancelamento.
  - **Persistência de Dados:** Todas as informações são salvas em um banco de dados MySQL.
  - **Versionamento de Schema:** O schema do banco de dados é gerenciado automaticamente pelo Flyway.
  - **Relatórios:** Gere relatórios sobre o tempo de conclusão de tarefas e o histórico de bloqueios.

## Tecnologias Utilizadas

  - **Java 17+**
  - **Spring Boot 3+**
  - **Spring Shell:** Para a interface de linha de comando.
  - **Spring Data JPA:** Para a persistência de dados.
  - **MySQL Server 8.0+:** Como banco de dados.
  - **Flyway:** Para versionamento e migração do schema do banco.
  - **Maven:** Como gerenciador de dependências e build.

## Pré-requisitos

Antes de começar, garanta que você tenha os seguintes softwares instalados:

  - JDK 17 ou superior.
  - Apache Maven 3.8 ou superior.
  - MySQL Server 8.0 ou superior.

## Configuração e Execução

1.  **Clone o repositório:**

    ```bash
    git clone <url-do-seu-repositorio>
    cd board_de_tarefas
    ```

2.  **Crie o Banco de Dados:**
    Conecte-se ao seu MySQL e execute o seguinte comando para criar o banco de dados vazio:

    ```sql
    CREATE DATABASE board_tarefas_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    ```

3.  **Configure a Conexão:**
    Abra o arquivo `src/main/resources/application.properties` e altere as seguintes linhas com suas credenciais do MySQL:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/board_tarefas_db?useSSL=false&serverTimezone=UTC
    spring.datasource.username=seu_usuario_mysql
    spring.datasource.password=sua_senha_mysql
    ```

4.  **Execute a Aplicação:**
    Use o Maven para compilar e iniciar a aplicação. O Flyway criará todas as tabelas automaticamente na primeira execução.

    ```bash
    mvn spring-boot:run
    ```

    Após a inicialização, o prompt `shell:>` aparecerá, pronto para receber comandos.

## Referência de Comandos

### Gerenciamento de Boards

| Comando | Descrição | Exemplo de Uso |
| :--- | :--- | :--- |
| `menu` | Exibe uma lista de comandos principais. | `menu` |
| `boards` | Lista todos os boards existentes. | `boards` |
| `criar-board <nome>` | Cria um novo board. | `criar-board "Projeto da Faculdade"` |
| `selecionar-board <id>` | Seleciona um board para se tornar ativo. | `selecionar-board 1` |
| `excluir-board <id>` | Exclui um board e todos os seus dados. | `excluir-board 1` |

### Gerenciamento de Cards (Requer um board selecionado)

| Comando | Descrição | Exemplo de Uso |
| :--- | :--- | :--- |
| `ver-board` | Exibe o estado atual do board ativo. | `ver-board` |
| `criar-card <titulo> <desc>`| Cria um novo card no board ativo. | `criar-card "Fazer relatório" "Pesquisar fontes e escrever"` |
| `mover-card <id>` | Move um card para a próxima coluna. | `mover-card 5` |
| `cancelar-card <id>` | Move um card para a coluna de cancelamento. | `cancelar-card 5` |
| `bloquear-card <id> <motivo>`| Bloqueia um card com um motivo. | `bloquear-card 5 "Aguardando aprovação"` |
| `desbloquear-card <id> <motivo>`| Desbloqueia um card com um motivo. | `desbloquear-card 5 "Aprovação recebida"` |
| `relatorio-conclusao` | Gera o relatório de tempo de conclusão. | `relatorio-conclusao` |
| `relatorio-bloqueios` | Gera o relatório de bloqueios. | `relatorio-bloqueios` |
| `exit` | Sai do programa. | `exit` |