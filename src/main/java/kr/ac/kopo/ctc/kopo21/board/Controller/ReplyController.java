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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    private boolean isAdmin(Authentication auth) {
        return auth != null && auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
    }
    private boolean isOwner(Reply reply, String username) {
        return reply.getUser() != null
                && reply.getUser().getUsername() != null
                && reply.getUser().getUsername().equals(username);
    }
    private void enforceReplyOwnerOrAdmin(Reply reply, String username, Authentication auth) {
        if (!(isAdmin(auth) || isOwner(reply, username))) {
            throw new AccessDeniedException("본인 댓글만 수정/삭제할 수 있습니다.");
        }
    }

    /** 댓글 목록 (JSON) */
    @GetMapping(value = "/post/{postId}/replies", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ReplyDto> list(@PathVariable Long postId) {
        return replyService.listByPost(postId);
    }

    /** 댓글 생성 */
    @PostMapping("/post/{postId}/reply")
    @Transactional
    public String createReply(@PathVariable Long postId,
                              @AuthenticationPrincipal(expression = "username") String username,
                              @RequestParam String replyContent,
                              RedirectAttributes rttr) {

        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        User me = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        ReplyDto dto = new ReplyDto(null, postId, me.getId(), replyContent);
        replyService.createReply(postId, dto);

        rttr.addFlashAttribute("msg", "댓글이 등록되었습니다.");
        return "redirect:/post/" + postId;
    }

    /** 댓글 수정 */
    @PostMapping("/reply/{id}/edit")
    @Transactional
    public String update(@PathVariable Long id,
                         @AuthenticationPrincipal(expression = "username") String username,
                         Authentication authentication,
                         @RequestParam("replyContent") String replyContent,
                         RedirectAttributes rttr) {

        if (username == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        Reply target = replyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        enforceReplyOwnerOrAdmin(target, username, authentication);

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
                         @AuthenticationPrincipal(expression = "username") String username,
                         Authentication authentication,
                         RedirectAttributes rttr) {

        if (username == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        Reply target = replyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        enforceReplyOwnerOrAdmin(target, username, authentication);

        ReplyDto deleted = replyService.deleteReply(id);

        rttr.addFlashAttribute("msg", "댓글이 삭제되었습니다.");
        return "redirect:/post/" + deleted.getPostId();
    }
}
