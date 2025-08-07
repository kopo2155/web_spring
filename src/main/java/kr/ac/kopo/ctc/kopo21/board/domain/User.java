package kr.ac.kopo.ctc.kopo21.board.domain;

import jakarta.persistence.*;

@Entity

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;

    private String password;


    public void setUserId(String userId) {
        this.userId = userId;

    }
    public String getUserId() {
        return this.userId;
    }
}
