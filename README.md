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
- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Shell 3.4.1** - Interface de linha de comando interativa
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados relacional
- **Flyway** - Controle de versão do banco de dados
- **Maven** - Gerenciamento de dependências

## Pré-requisitos

Antes de começar, garanta que você tenha os seguintes softwares instalados:

  - JDK 21 ou superior.
  - Apache Maven 3.8 ou superior.
  - PostgrAQL 14 ou superior.

## Configuração e Execução

1.  **Clone o repositório:**

    ```bash
    git clone <url-do-seu-repositorio>
    cd board_de_tarefas
    ```

2.  **Configure seu banco de dados**


    ```sql
    CREATE DATABASE board_tarefas_db;
    CREATE USER board_user WITH ENCRYPTED PASSWORD 'sua_senha';
    GRANT ALL PRIVILEGES ON DATABASE board_tarefas_db TO board_user;
    ```

3.  **Configure a Conexão:**
    Abra o arquivo `src/main/resources/application.properties` e configure as credenciais:

    ```properties
    spring.datasource.username=board_user
    spring.datasource.password=sua_senha
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