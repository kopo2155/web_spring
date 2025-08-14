package kr.ac.kopo.ctc.kopo21.board.Controller;

import jakarta.transaction.Transactional;
import kr.ac.kopo.ctc.kopo21.board.domain.Reply;
import kr.ac.kopo.ctc.kopo21.board.domain.ReplyDto;
import kr.ac.kopo.ctc.kopo21.board.domain.User;
import kr.ac.kopo.ctc.kopo21.board.repository.ReplyRepository;
import kr.ac.kopo.ctc.kopo21.board.repository.UserRepository;
import kr.ac.kopo.ctc.kopo21.board.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;

    /** 댓글 목록 (JSON) */
    @GetMapping(value = "/post/{postId}/replies", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ReplyDto> list(@PathVariable Long postId) {
        return replyService.listByPost(postId);
    }

    /** 댓글 생성 (JSP 폼: userId, replyContent 전송) */
    @PostMapping("/post/{postId}/reply")
    @Transactional
    public String createReply(@PathVariable Long postId,
                              @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails principal,
                              @RequestParam String replyContent,
                              RedirectAttributes rttr) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        // username → User 엔티티 조회 (필드명에 맞게)
        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        ReplyDto dto = new ReplyDto(null, postId, user.getId(), replyContent);
        replyService.createReply(postId, dto);

        rttr.addFlashAttribute("msg", "댓글이 등록되었습니다.");
        return "redirect:/post/" + postId;
    }


    @PostMapping("/reply/{id}/edit")
    @Transactional
    public String update(@PathVariable Long id,
                         @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails principal,
                         Authentication authentication,
                         @RequestParam("replyContent") String replyContent,
                         RedirectAttributes rttr) {

        if (principal == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        Reply target = replyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        User me = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        boolean owner = target.getUser() != null && target.getUser().getId().equals(me.getId());
        boolean admin = authentication != null &&
                authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!(owner || admin)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한 없음");

        ReplyDto patch = new ReplyDto();
        patch.setReplyContent(replyContent);
        ReplyDto updated = replyService.updateReply(id, patch);

        rttr.addFlashAttribute("msg", "댓글이 수정되었습니다.");
        return "redirect:/post/" + updated.getPostId();
    }

    /** 댓글 삭제 */
    @PostMapping("/reply/{id}/delete")
    @Transactional
    public String delete(@PathVariable Long id,
                         @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails principal,
                         Authentication authentication,
                         RedirectAttributes rttr) {

        if (principal == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        Reply target = replyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        User me = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        boolean owner = target.getUser() != null && target.getUser().getId().equals(me.getId());
        boolean admin = authentication != null &&
                authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!(owner || admin)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한 없음");

        ReplyDto deleted = replyService.deleteReply(id);

        rttr.addFlashAttribute("msg", "댓글이 삭제되었습니다.");
        return "redirect:/post/" + deleted.getPostId();
    }
}
