package kr.ac.kopo.ctc.kopo21.board.service;

import kr.ac.kopo.ctc.kopo21.board.domain.Sample;
import kr.ac.kopo.ctc.kopo21.board.repository.SampleRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SampleService {
    void testNoTransactional();

    void testTransactional();

    String testNoCache(Long id);
    String testCache(Long id);
    void testCacheClear(Long id);

    Sample selectOne(Long id);
}


