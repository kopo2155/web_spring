package kr.ac.kopo.ctc.kopo21.board.repository;


import kr.ac.kopo.ctc.kopo21.board.domain.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SampleRepository extends JpaRepository<Sample, Long>, JpaSpecificationExecutor<Sample> {
    List<Sample> findByTitle(String title);
    List<Sample> findByTitleLike(String title);


}
