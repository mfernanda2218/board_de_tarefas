package com.example.board_de_tarefas.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class HelloCommand {
    
    @ShellMethod(key = "hello", value = "Diz olá")
    public String hello() {
        return "Olá! O Spring Shell está funcionando!";
    }
}