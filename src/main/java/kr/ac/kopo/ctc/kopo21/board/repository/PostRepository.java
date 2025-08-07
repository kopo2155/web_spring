package kr.ac.kopo.ctc.kopo21.board.repository;

import kr.ac.kopo.ctc.kopo21.board.domain.Child;
import kr.ac.kopo.ctc.kopo21.board.domain.Post;
import kr.ac.kopo.ctc.kopo21.board.domain.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

}
