package kr.ac.kopo.ctc.kopo21.board.repository;

import kr.ac.kopo.ctc.kopo21.board.domain.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Long>, ChildRepositoryCustom {
    @Query("SELECT c FROM Child c left join fetch c.parent")
    List<Child> fetchAll();
}
