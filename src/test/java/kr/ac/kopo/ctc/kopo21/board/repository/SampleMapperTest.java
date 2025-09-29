package kr.ac.kopo.ctc.kopo21.board.repository;

import kr.ac.kopo.ctc.kopo21.board.domain.Sample;
import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
 class SampleMapperTest {

    @Autowired
    SampleMapper sampleMapper;

    @Test
    void findAll() {
        List<Sample> samples = sampleMapper.findAll();
        for (Sample sample : samples) {
            System.out.println(sample.getTitle());
        }
    }

    @Test
    void findById() {
        Sample sample = sampleMapper.findById(1L);
        System.out.println(sample.getTitle());
        assertEquals(1, sample.getId());

    }

    @Test
    void findAllByTitle(){
        RowBounds rowBounds = new RowBounds(0, 10);
        List<Sample> samples = sampleMapper.findAllByTitle("t1", rowBounds);
        for (Sample sample : samples) {
            System.out.println(sample.getTitle());
        }
    }
}
