package com.example.board_de_tarefas.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class TestCommands {

    @ShellMethod(key = "hello", value = "Um comando de teste simples para diagnóstico.")
    public String helloWorld() {
        return "Olá! Se você está vendo isso, o Spring Shell está encontrando os comandos!";
    }
}