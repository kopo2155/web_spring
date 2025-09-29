package kr.ac.kopo.ctc.kopo21.board.repository;

import kr.ac.kopo.ctc.kopo21.board.domain.Child;
import kr.ac.kopo.ctc.kopo21.board.domain.Post;
import kr.ac.kopo.ctc.kopo21.board.domain.Sample;
import kr.ac.kopo.ctc.kopo21.board.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest

public class PostRepositoryTest {
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;


//    @Test
//    public void before() {
//
//        for (int i = 1; i <= 5; i++) {
//            User user = new User();
//            user.setUserId("user" + i);
//            userRepository.save(user);
//
//            Post post = new Post();
//            post.setTitle("post" + i);
//            post.setUser(user);
//            postRepository.save(post);
//        }
//
//
//    }

    @Test
    void findAllByTitle(){
        Map<String, Object> filter = new HashMap<>();
        filter.put("title","post1");

        PageRequest pageable = PageRequest.of(0,10);
        Page<Post> page = postRepository.findAll(pageable);

        System.out.println("page1");
        for(Post s : page) {
            System.out.println(s.getTitle());

        }
    }
}
