package kr.ac.kopo.ctc.kopo21.board.repository;

import kr.ac.kopo.ctc.kopo21.board.domain.Child;
import kr.ac.kopo.ctc.kopo21.board.domain.Post;
import kr.ac.kopo.ctc.kopo21.board.domain.Sample;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    Page<Post> findByTitleContainingIgnoreCase(String q, Pageable pageable);

    @EntityGraph(attributePaths = {"board","user"})
    Page<Post> findByBoardId(Long boardId, Pageable pageable);

    @EntityGraph(attributePaths = {"board","user"})
    Page<Post> findByBoardIdAndTitleContainingIgnoreCase(Long boardId, String title, Pageable pageable);

    // findAll(Pageable)은 @EntityGraph을 못 붙이니 아래처럼 별도 선언
    @EntityGraph(attributePaths = {"board","user"})
    Page<Post> findAllBy(Pageable pageable);
}
