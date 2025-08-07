package kr.ac.kopo.ctc.kopo21.board.domain;

import jakarta.persistence.*;

@Entity

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postUserID", referencedColumnName = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardID")
    private Board board;

    private String title;
    private String content;


    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return this.board;
    }

    public void setTitle(String title) {
        this.title = title;

    }
    public String getTitle() {
        return this.title;
    }

    public void setContent(String content) {
        this.content = content;

    }
    public String getContent() {
        return this.content;
    }


}
