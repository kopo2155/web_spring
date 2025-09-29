package kr.ac.kopo.ctc.kopo21.board.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity

public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post postUserId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replyUserId")
    private User user;

    @Size(max = 255)
    private String replyContent;



    public void setPostUserID(Post postUserId) {
        this.postUserId = postUserId;
    }

    public Post getPostUserId() {
        return this.postUserId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;

    }
    public String getReplyContent() {
        return this.replyContent;
    }


    public Long getReplyId() {return this.replyId;
    }
}
