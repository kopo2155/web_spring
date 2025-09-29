package kr.ac.kopo.ctc.kopo21.board.repository;

import kr.ac.kopo.ctc.kopo21.board.domain.Sample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleRestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetSample() throws Exception {
        URI uri = UriComponentsBuilder.fromPath("/sample/selectOne")
                .queryParam("id",2)
                .build()
                .toUri();

        Sample response = restTemplate.getForObject(uri, Sample.class);

        assertEquals(2L,response.getId());
        assertEquals("t2",response.getTitle());
    }
}
