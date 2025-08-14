package kr.ac.kopo.ctc.kopo21.board.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")          // ← DB 컬럼명과 일치

    private Long postId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_userid", referencedColumnName = "Id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardid", referencedColumnName = "id")
    private Board board;

    private String title;
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Long getPostId() { return postId; }
    public User getUser() { return user; }
    public Board getBoard() { return board; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setUser(User user) { this.user = user; }
    public void setBoard(Board board) { this.board = board; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
}



