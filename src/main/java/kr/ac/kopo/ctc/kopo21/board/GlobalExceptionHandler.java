package kr.ac.kopo.ctc.kopo21.board;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(LengthLimitExceededException.class)
    public String handleLengthLimit(LengthLimitExceededException ex, Model model) {
        model.addAttribute("title", "글자수 제한 초과");
        model.addAttribute("error",
                String.format("입력한 '%s' 값이 최대 %d자를 초과했습니다. (현재 %d자)",
                        ex.getField(), ex.getMax(), ex.getActual()));
        // detail이나 fieldErrors는 굳이 노출하지 않음
        return "error"; // /WEB-INF/views/error.jsp(or .jspf) 에 매핑되는 뷰 이름
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrity(DataIntegrityViolationException ex, Model model) {
        model.addAttribute("title", "입력값이 너무 깁니다");
        model.addAttribute("error", "댓글은 255자 이내로 입력하세요.");
        model.addAttribute("detail", ex.getMostSpecificCause().getMessage());
        return "error";
    }
    // 4) 마지막 보루(예상치 못한 모든 예외)
    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {
        model.addAttribute("title", "알 수 없는 오류");
        model.addAttribute("error", "요청 처리 중 오류가 발생했습니다.");
        model.addAttribute("detail", ex.getMessage());
        return "error";
    }
}