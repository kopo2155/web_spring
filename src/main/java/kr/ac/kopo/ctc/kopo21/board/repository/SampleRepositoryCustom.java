package kr.ac.kopo.ctc.kopo21.board.repository;

import kr.ac.kopo.ctc.kopo21.board.domain.Sample;

import java.util.List;

public interface SampleRepositoryCustom {
    List<Sample> findAllByDynamicConditions(String title);
    Long countByTitleContaining(String title);
}


