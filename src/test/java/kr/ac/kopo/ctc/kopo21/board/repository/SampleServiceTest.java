package kr.ac.kopo.ctc.kopo21.board.repository;

import kr.ac.kopo.ctc.kopo21.board.service.SampleService;
import kr.ac.kopo.ctc.kopo21.board.service.SampleServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

public class SampleServiceTest {
    @Autowired
    SampleService sampleService;
    @Test
    void testNoCache1() {
        System.out.println("testNoCache1 start");
        String ret = sampleService.testNoCache(3L);
        System.out.println("testNoCache1 end, " + ret);
    }
    @Test
    void testNoCache2() {
        System.out.println("testNoCache2 start");
        String ret = sampleService.testNoCache(3L);
        System.out.println("testNoCache2 end, " + ret);
    }

    @Test
    void testCache1() {
        System.out.println("testCache1 start");
        String ret = sampleService.testCache(3L);
        System.out.println("testCache1 end, " + ret);
    }

    @Test
    void testCache2() {
        System.out.println("testCache2 start");
        String ret = sampleService.testCache(3L);
        System.out.println("testCache2 end, " + ret);
    }
}
