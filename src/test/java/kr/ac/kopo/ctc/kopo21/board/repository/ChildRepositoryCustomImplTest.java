package kr.ac.kopo.ctc.kopo21.board.repository;

import kr.ac.kopo.ctc.kopo21.board.domain.Child;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest

public class ChildRepositoryCustomImplTest {
    @Autowired
    ChildRepository childRepository;
    @Autowired
    ParentRepository parentRepository;


    @Test
    void selectAll() {
        List<Child> children = childRepository.selectAll();
        for (Child c : children) {
            c.getParent().getName();
        }
    }
}
