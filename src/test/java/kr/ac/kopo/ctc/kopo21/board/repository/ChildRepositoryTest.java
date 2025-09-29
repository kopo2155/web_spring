package kr.ac.kopo.ctc.kopo21.board.repository;

import kr.ac.kopo.ctc.kopo21.board.domain.Child;
import kr.ac.kopo.ctc.kopo21.board.domain.Parent;
import kr.ac.kopo.ctc.kopo21.board.domain.Sample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest

public class ChildRepositoryTest {
    @Autowired
    ChildRepository childRepository;
    @Autowired
    ParentRepository parentRepository;

/*    @Test
    public void before() {

        for (int i =1; i<=5; i++) {
            Parent parent = new Parent();
            parent.setName("parent" + i);
            parentRepository.save(parent);

            Child child = new Child();
            child.setName("child" + i);
            child.setParent(parent);
            childRepository.save(child);
        }
    }*/
    @Test
    public void fetchAll() {
        List<Child> children = childRepository.fetchAll();
        for (Child c : children) {
            c.getParent().getName();
        }

    }
    }




