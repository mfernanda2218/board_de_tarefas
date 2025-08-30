package com.example.board_de_tarefas.model;

import com.example.board_de_tarefas.model.enums.BlockageType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class BlockageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime timestamp;
    private String reason;

    @Enumerated(EnumType.STRING)
    private BlockageType type;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;
}
