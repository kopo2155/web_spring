package kr.ac.kopo.ctc.kopo21.board.repository;

import kr.ac.kopo.ctc.kopo21.board.domain.Parent;
import kr.ac.kopo.ctc.kopo21.board.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
