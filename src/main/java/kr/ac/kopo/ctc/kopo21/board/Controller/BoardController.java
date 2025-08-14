package kr.ac.kopo.ctc.kopo21.board.Controller;

import jakarta.transaction.Transactional;
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


    @GetMapping("/board")
    public String board(@RequestParam(value = "search_value", required = false) String search_value,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        Model model) {
        int safePage = Math.max(page, 1) - 1;

        Pageable pageable = PageRequest.of(safePage, size, Sort.by(Sort.Direction.DESC, "postId"));



        Page<Post> pageResult = (search_value == null || search_value.isBlank())
                ? postRepository.findAll(pageable)
                : postRepository.findByTitleContainingIgnoreCase(search_value, pageable);

        int pagesPerBlock = 5; // 페이지 블록 크기(원하면 환경설정/파라미터로 뺄 수 있음)
        int totalItems = (int) pageResult.getTotalElements();

        PaginationInfo pagination = paginationInfoService.getPaginationInfo(
                page ,            // 현재 페이지(1-base)
                pagesPerBlock,   // 블록 당 페이지 수
                size,            // 페이지당 게시글 수
                totalItems       // 전체 게시글 수
        );


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
    public String post(@PathVariable Long id, Model model,  @AuthenticationPrincipal(expression = "username") String username) {
        Post saved = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + id));
        List<ReplyDto> replies = replyService.listByPost(id);

        model.addAttribute("post", saved);
        model.addAttribute("replies", replies);      // ★ JSP에서 사용

        if (username != null) {
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
                         @RequestParam Long userId,
                         @RequestParam Long boardId) {

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
    public String edit(@PathVariable Long postId, Model model) {
        Post saved = postRepository.findById(postId).orElse(null);

        model.addAttribute("post", saved);

        return "edit";
    }
    @PostMapping("/post/update")
    @Transactional
    public String update(@RequestParam("id") Long id,
                         @RequestParam(value = "title", required = false) String title,
                         @RequestParam(value = "userId", required = false) Long userId,
                         @RequestParam(value = "content", required = false) String content,
                         @RequestParam(value = "boardId", required = false) Long boardId) {

        Post target = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post not found"));

        if (title != null) {
            target.setTitle(title);
        }

        if (content != null) {
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

        // @Transactional 이면 save() 생략 가능(JPA dirty checking). 그래도 명시하고 싶다면:
        postRepository.save(target);

        return "redirect:/post/" + id;
    }

    @PostMapping("/post/{id}/delete")
    public String deleteArticle(@PathVariable Long id, RedirectAttributes rttr){
        Post saved = postRepository.findById(id).orElse(null);

        if(saved !=null){
            postRepository.delete(saved);
            rttr.addFlashAttribute("msg", "삭제가 완료되었습니다.");

        }

        return "redirect:/board";

    }

}
