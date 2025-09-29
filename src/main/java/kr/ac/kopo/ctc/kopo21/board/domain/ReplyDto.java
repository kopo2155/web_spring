package kr.ac.kopo.ctc.kopo21.board.domain;

import jakarta.validation.constraints.Size;

public class ReplyDto {
    private Long replyId;

    private Long postId;

    private Long userId;

    @Size(max = 255, message = "댓글은 255자 이내로 입력하세요.")
    private String replyContent;

    public ReplyDto() {}

    public ReplyDto(Long replyId, Long postId, Long userId, String replyContent) {
        this.replyId = replyId;
        this.postId = postId;
        this.userId = userId;
        this.replyContent = replyContent;
    }

    // ---------- getters / setters ----------
    public Long getReplyId() { return replyId; }
    public void setReplyId(Long replyId) { this.replyId = replyId; }

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getReplyContent() { return replyContent; }
    public void setReplyContent(String replyContent) { this.replyContent = replyContent; }

    // ---------- mapping helpers ----------

    public static ReplyDto fromEntity(Reply e) {
        return new ReplyDto(
                e.getReplyId(),                                                // ⚠ Reply에 getReplyId() 추가 필요
                e.getPostUserId() != null ? e.getPostUserId().getPostId() : null,
                e.getUser() != null ? e.getUser().getId() : null,
                e.getReplyContent()
        );
    }

    /** DTO -> 새 엔티티 (Post/User는 서비스에서 조회해서 넣기) */
    public Reply toEntity(Post post, User user) {
        Reply r = new Reply();
        r.setPostUserID(post);    // Reply 엔티티의 setter 이름이 setPostUserID 인 점 주의
        r.setUser(user);
        r.setReplyContent(this.replyContent);
        return r;
    }
}
