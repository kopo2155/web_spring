package kr.ac.kopo.ctc.kopo21.board.repository;


import kr.ac.kopo.ctc.kopo21.board.domain.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SampleRepository extends JpaRepository<Sample, Long>, JpaSpecificationExecutor<Sample>{
    List<Sample> findByTitle(String title);
    List<Sample> findByTitleLike(String title);
//    List<Sample> findAllByTitleContaining(String title);

    //2) jpql
    @Query("SELECT s FROM Sample s WHERE s.title LIKE %:title%")
    List<Sample> findByTitleLikes(@Param("title") String title);


}
