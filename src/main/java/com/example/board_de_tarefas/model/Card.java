package com.example.board_de_tarefas.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private boolean blocked;

    @ManyToOne
    @JoinColumn(name = "column_id")
    private Column column;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<CardMovement> movementHistory = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<BlockageLog> blockageLogs = new ArrayList<>();
}
