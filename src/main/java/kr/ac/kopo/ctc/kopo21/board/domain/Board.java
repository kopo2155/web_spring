package kr.ac.kopo.ctc.kopo21.board.domain;

import jakarta.persistence.*;

@Entity

public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String boardTitle;


    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;

    }
    public String getBoardTitle() {
        return this.boardTitle;
    }

    public Long getId() { return id; }


}
