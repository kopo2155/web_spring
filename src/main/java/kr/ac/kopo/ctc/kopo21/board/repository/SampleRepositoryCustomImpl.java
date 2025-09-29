package kr.ac.kopo.ctc.kopo21.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.ac.kopo.ctc.kopo21.board.domain.QSample;
import kr.ac.kopo.ctc.kopo21.board.domain.Sample;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.hibernate.sql.exec.spi.StandardEntityInstanceResolver;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class SampleRepositoryCustomImpl implements SampleRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final QSample sample = QSample.sample;

    public SampleRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }
    @Override
    public List<Sample> findAllByDynamicConditions(String title) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(title)) {
            builder.and(sample.title.containsIgnoreCase(title));
        }

        return queryFactory
                .selectFrom(sample)
                .where(builder)
                .fetch();
    }

    @Override
    public Long countByTitleContaining(String title) {
        return queryFactory
                .select(sample.count())
                .from(sample)
                .where(nameContains(title))
                .fetchOne();
    }

    private BooleanExpression nameContains(String title) {
        return StringUtils.hasText(title) ? sample.title.contains(title) : null;
    }

}
