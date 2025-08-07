package kr.ac.kopo.ctc.kopo21.board.repository;

import kr.ac.kopo.ctc.kopo21.board.domain.Child;
import kr.ac.kopo.ctc.kopo21.board.domain.Post;
import kr.ac.kopo.ctc.kopo21.board.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

}
