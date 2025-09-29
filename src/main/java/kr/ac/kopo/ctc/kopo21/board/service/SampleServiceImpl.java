package kr.ac.kopo.ctc.kopo21.board.service;

import kr.ac.kopo.ctc.kopo21.board.domain.Sample;
import kr.ac.kopo.ctc.kopo21.board.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.Thread.sleep;


@Service
public class SampleServiceImpl implements SampleService {

    @Autowired
    private SampleRepository sampleRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void testNoTransactional() {
        Sample sample = sampleRepository.findById(1l).get();
        sample.setTitle("Update1");
        sampleRepository.save(sample);

        throw new RuntimeException("No Transactional Test");
    }

    @Transactional
    @Override
    public void testTransactional() {
        Sample sample = sampleRepository.findById(1l).get();
        sample.setTitle("update1");
        sampleRepository.save(sample);

        throw new RuntimeException("Transactional Test");


    }

    @Override
    public String testNoCache(Long id) {
        sleep(3);
        return "NoCache";
    }

    @Override
    @Cacheable(value = "sample", key = "#id")
    public String testCache(Long id) {
        sleep(3);
        return "Cache";
    }

    @Override
    @CacheEvict(value = "sample", key = "#id")
    public void testCacheClear(Long id) {

    }

    private void sleep(int second) {
        try {
            Thread.sleep(second * 1000L);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Sample selectOne(Long id) {
        String sql = "SELECT * FROM sample WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new Sample(
                        rs.getLong("id"),
                        rs.getString("title")
                )
        );

    }
}
