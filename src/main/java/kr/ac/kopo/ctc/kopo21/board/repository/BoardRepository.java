package kr.ac.kopo.ctc.kopo21.board.repository;

import kr.ac.kopo.ctc.kopo21.board.domain.Board;
import kr.ac.kopo.ctc.kopo21.board.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
