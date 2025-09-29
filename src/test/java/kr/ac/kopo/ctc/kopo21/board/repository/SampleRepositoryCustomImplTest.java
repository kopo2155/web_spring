package kr.ac.kopo.ctc.kopo21.board.repository;

import kr.ac.kopo.ctc.kopo21.board.domain.Sample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest

public class SampleRepositoryCustomImplTest {
    @Autowired
    SampleRepositoryCustom sampleRepositoryCustom;

    @Test
    void countByTitleContaining() {
        Long countResult = sampleRepositoryCustom.countByTitleContaining("t1");
        int expectation = 10;
        System.out.println(" 검색 키워드: 't1'");
        System.out.println(" 기대 결과 수: " + expectation);
        System.out.println(" 실제 결과 수: " + countResult);
        assertEquals(expectation, countResult);
    }

    @Test
    void findAllByDynamicConditions() {
        List<Sample> dynamicSampleList2 = sampleRepositoryCustom.findAllByDynamicConditions("t1");

        System.out.println("검색된 결과 수: " + dynamicSampleList2.size());
        for (Sample sample : dynamicSampleList2) {
            System.out.println("Sample Title: " + sample.getTitle());
        }

    }
}
