package com.example.board_de_tarefas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.board_de_tarefas", "com.example.shell"})
public class BoardDeTarefasApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardDeTarefasApplication.class, args);
	}

}
