package kr.ac.kopo.ctc.kopo21.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.ac.kopo.ctc.kopo21.board.domain.Child;
import kr.ac.kopo.ctc.kopo21.board.domain.QSample;
import kr.ac.kopo.ctc.kopo21.board.domain.Sample;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static kr.ac.kopo.ctc.kopo21.board.domain.QChild.child;
import static kr.ac.kopo.ctc.kopo21.board.domain.QParent.parent;

@Repository
public class ChildRepositoryCustomImpl implements ChildRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public ChildRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }
    @Override
    public List<Child> selectAll() {
    return queryFactory
            .selectFrom(child)
            .leftJoin(child.parent, parent)
            .fetchJoin()
            .fetch();
    }
}
