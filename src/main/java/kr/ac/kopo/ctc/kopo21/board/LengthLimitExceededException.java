package kr.ac.kopo.ctc.kopo21.board;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // 400 응답 (뷰 리턴 시에도 무해)
public class LengthLimitExceededException extends RuntimeException {

    private final String field;   // 예: "content"
    private final int max;        // 예: 255
    private final int actual;     // 실제 길이

    public LengthLimitExceededException(String field, int max, int actual) {
        super(String.format("'%s' 길이가 %d자를 초과했습니다. (현재 %d자)", field, max, actual));
        this.field = field;
        this.max = max;
        this.actual = actual;
    }

    public String getField() { return field; }
    public int getMax() { return max; }
    public int getActual() { return actual; }
}