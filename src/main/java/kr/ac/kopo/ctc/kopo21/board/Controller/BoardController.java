package kr.ac.kopo.ctc.kopo21.board.Controller;

import jakarta.transaction.Transactional;
import kr.ac.kopo.ctc.kopo21.board.LengthLimitExceededException;
import kr.ac.kopo.ctc.kopo21.board.PaginationInfo;
import kr.ac.kopo.ctc.kopo21.board.domain.*;
import kr.ac.kopo.ctc.kopo21.board.repository.BoardRepository;
import kr.ac.kopo.ctc.kopo21.board.repository.PostRepository;
import kr.ac.kopo.ctc.kopo21.board.repository.UserRepository;
import kr.ac.kopo.ctc.kopo21.board.service.PaginationInfoService;
import kr.ac.kopo.ctc.kopo21.board.service.ReplyService;
import org.apache.ibatis.session.RowBounds;
import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static kr.ac.kopo.ctc.kopo21.board.domain.QUser.user;

@Controller
public class BoardController {
    private static final int MAX_TITLE = 100;
    private static final int MAX_CONTENT = 255;

    // ✅ 공통 검사 메서드
    private void checkLength(String field, String value, int max) {
        if (value != null && value.length() > max) {
            throw new LengthLimitExceededException(field, max, value.length());
        }
    }
    @Autowired
    PostRepository postRepository;
    @Autowired
    PaginationInfoService paginationInfoService;
@Autowired
UserRepository userRepository;
@Autowired
BoardRepository boardRepository;

@Autowired
    ReplyService replyService;
    public BoardController(PostRepository postRepository,
                           PaginationInfoService paginationInfoService) {
        this.postRepository = postRepository;
        this.paginationInfoService = paginationInfoService;
    }


    private boolean isAdmin(Authentication auth) {
        return auth != null && auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
    }
    private boolean isOwner(Post post, String username) {
        return post.getUser() != null
                && post.getUser().getUsername() != null
                && post.getUser().getUsername().equals(username);
    }




    @GetMapping("/board")
    public String board(@RequestParam(defaultValue = "0") Long boardId,
                        @RequestParam(value = "search_value", required = false) String search_value,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        int safePage = Math.max(page, 1) - 1;

        Pageable pageable = PageRequest.of(safePage, size, Sort.by(Sort.Direction.DESC, "postId"));
        String q = (search_value == null) ? "" : search_value.trim();

        List<Board> boards = boardRepository.findAll(Sort.by(Sort.Direction.ASC, "boardTitle"));



        Page<Post> pageResult;
        if (boardId == 0L) {                    // ✅ 전체
            pageResult = q.isBlank()
                    ? postRepository.findAllBy(pageable)
                    : postRepository.findByTitleContainingIgnoreCase(q, pageable);
            model.addAttribute("board", null);
        } else {                                // ✅ 특정 게시판
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));
            model.addAttribute("board", board);
            pageResult = q.isBlank()
                    ? postRepository.findByBoardId(boardId, pageable)
                    : postRepository.findByBoardIdAndTitleContainingIgnoreCase(boardId, q, pageable);
        }


        int pagesPerBlock = 5; // 페이지 블록 크기(원하면 환경설정/파라미터로 뺄 수 있음)
        int totalItems = (int) pageResult.getTotalElements();

        PaginationInfo pagination = paginationInfoService.getPaginationInfo(
                page ,            // 현재 페이지(1-base)
                pagesPerBlock,   // 블록 당 페이지 수
                size,            // 페이지당 게시글 수
                totalItems       // 전체 게시글 수
        );


        model.addAttribute("boards", boards);
        model.addAttribute("selectedBoardId", boardId);
        model.addAttribute("posts", pageResult.getContent());
        model.addAttribute("pagination", pagination); // ✅ JSP에서 쓰는 키
        model.addAttribute("size", size);                  // ← 링크에서 사용
        model.addAttribute("search_value", search_value);

        // 검색어 유지
        return "board";
    }

    @GetMapping("post")
    public String post() {
        return "post";
    }


    @GetMapping("/post/{id}")
    public String post(@PathVariable Long id, Model model,  @AuthenticationPrincipal(expression = "username") String username, Authentication auth) {
        Post saved = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + id));
        List<ReplyDto> replies = replyService.listByPost(id);

        model.addAttribute("post", saved);
        model.addAttribute("replies", replies);      // ★ JSP에서 사용
        boolean admin  = isAdmin(auth);
        boolean owner  = username != null && isOwner(saved, username);
        model.addAttribute("canEdit", admin || owner); // 글 수정/삭제 버튼
        model.addAttribute("isAdmin", admin);          // 필요 시 UI 분기용

        // 댓글 작성/수정/삭제 버튼 비교용 현재 로그인 사용자 id
        if (username != null) {
            userRepository.findByUsername(username)
                    .ifPresent(u -> model.addAttribute("loginUserId", u.getId()));
            model.addAttribute("loginUsername", username);
        }


        return "post";
    }


    @GetMapping("/new_post")
    public String newPostForm(Model model, Principal principal) {
        model.addAttribute("boards", boardRepository.findAll()); // 셀렉트에 쓸 목록
        if (principal != null) {
            userRepository.findByUsername(principal.getName())
                    .ifPresent(u -> model.addAttribute("loginUserId", u.getId())); // <-- 실제 getId() 이름 확인
        }
        // model.addAttribute("loginUser", 현재 로그인 사용자); // hidden userId에 쓸 값
        return "new_post"; // /WEB-INF/views/new_post.jsp
    }

    @PostMapping("/post/create")
    public String create(@RequestParam String title,
                         @RequestParam String content,
                         @RequestParam Long userId,      // ↔️ 권장: Principal로 대체 (아래 참고)
                         @RequestParam Long boardId) {

        // ✅ 길이 검사
        checkLength("title", title, MAX_TITLE);
        checkLength("content", content, MAX_CONTENT);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자: " + userId));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시판: " + boardId));

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);
        post.setBoard(board);

        Post saved = postRepository.save(post);
        return "redirect:/post/" + saved.getPostId();
    }
    @GetMapping("/post/{postId}/edit")
    public String edit(@PathVariable Long postId, Model model,@AuthenticationPrincipal(expression = "username") String username,
                       Authentication auth) {
        Post saved = postRepository.findById(postId).orElse(null);

        if (!isAdmin(auth) && !isOwner(saved, username)) {
            throw new AccessDeniedException("본인 글만 수정할 수 있습니다.");
        }
        model.addAttribute("post", saved);

        return "edit";
    }


    @PostMapping("/post/update")
    @Transactional
    public String update(@RequestParam("id") Long id,
                         @RequestParam(value = "title", required = false) String title,
                         @RequestParam(value = "userId", required = false) Long userId,
                         @RequestParam(value = "content", required = false) String content,
                         @RequestParam(value = "boardId", required = false) Long boardId,@AuthenticationPrincipal(expression = "username") String username,
                         Authentication auth) {

        Post target = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post not found"));
        if (!isAdmin(auth) && !isOwner(target, username)) {
            throw new AccessDeniedException("본인 글만 수정할 수 있습니다.");
        }
        if (title != null) {
            // ✅ 길이 검사
            checkLength("title", title, MAX_TITLE);
            target.setTitle(title);
        }
        if (content != null) {
            // ✅ 길이 검사
            checkLength("content", content, MAX_CONTENT);
            target.setContent(content);
        }
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid userId"));
            target.setUser(user);
        }
        if (boardId != null) {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid boardId"));
            target.setBoard(board);
        }

        postRepository.save(target); // @Transactional이라 생략 가능
        return "redirect:/post/" + id;
    }

    @PostMapping("/post/{id}/delete")
    public String deleteArticle(@PathVariable Long id, RedirectAttributes rttr,@AuthenticationPrincipal(expression = "username") String username,
                                Authentication auth){
        Post saved = postRepository.findById(id).orElse(null);

        if (!isAdmin(auth) && !isOwner(saved, username)) {
            throw new AccessDeniedException("본인 글만 삭제할 수 있습니다.");
        }

        if(saved !=null){
            postRepository.delete(saved);
            rttr.addFlashAttribute("msg", "삭제가 완료되었습니다.");

        }

        return "redirect:/board";

    }

}
