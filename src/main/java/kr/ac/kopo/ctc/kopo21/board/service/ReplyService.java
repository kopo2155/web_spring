package kr.ac.kopo.ctc.kopo21.board.service;



import kr.ac.kopo.ctc.kopo21.board.domain.Post;
import kr.ac.kopo.ctc.kopo21.board.domain.Reply;
import kr.ac.kopo.ctc.kopo21.board.domain.ReplyDto;
import kr.ac.kopo.ctc.kopo21.board.domain.User;
import kr.ac.kopo.ctc.kopo21.board.repository.PostRepository;
import kr.ac.kopo.ctc.kopo21.board.repository.ReplyRepository;
import kr.ac.kopo.ctc.kopo21.board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class ReplyService {
    @Autowired
    public ReplyRepository replyRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;


    public List<ReplyDto> listByPost(Long postId) {
        List<Reply> replies = replyRepository.findByPostUserId_PostId(postId);
        return replies.stream()
                .map(ReplyDto::fromEntity)
                .collect(Collectors.toList());
    }

    /** 댓글 생성 */
    @Transactional
    public ReplyDto createReply(Long postId, ReplyDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("작성자(userId)가 유효하지 않습니다."));

        Reply reply = new Reply();
        reply.setPostUserID(post);           // ✅ 반드시 설정
        reply.setUser(user);           // ✅ 반드시 설정
        reply.setReplyContent(dto.getReplyContent());
        Reply saved = replyRepository.save(reply);
        return ReplyDto.fromEntity(saved);
    }
    @Transactional
    public ReplyDto updateReply(Long id, ReplyDto dto) {
        Reply saved = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정할 댓글이 없어요"));

        // 내용 수정(값이 왔을 때만)
        if (dto.getReplyContent() != null) {
            saved.setReplyContent(dto.getReplyContent());
        }

        // 게시글 변경 요청이 있으면 매핑
        if (dto.getPostId() != null &&
                (saved.getPostUserId() == null ||
                        !dto.getPostId().equals(saved.getPostUserId().getPostId()))) {
            Post post = postRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new IllegalArgumentException("postId가 유효하지 않습니다."));
            saved.setPostUserID(post); // 엔티티 setter 명 주의
        }

        // 작성자 변경 요청이 있으면 매핑
        if (dto.getUserId() != null &&
                (saved.getUser() == null ||
                        !dto.getUserId().equals(saved.getUser().getId()))) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("userId가 유효하지 않습니다."));
            saved.setUser(user);
        }

        // @Transactional 더티체킹으로 flush 되지만, 명시 저장 원하면 다음 라인 유지
        Reply updated = replyRepository.save(saved);
        return ReplyDto.fromEntity(updated);
    }

    /** 댓글 삭제 */
    @Transactional
    public ReplyDto deleteReply(Long id) {
        Reply saved = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글삭제 실패, 대상없음"));
        replyRepository.delete(saved);
        return ReplyDto.fromEntity(saved);
    }
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        postRepository.delete(post); // 엔티티 remove → 자식 먼저 삭제됨
    }


}


