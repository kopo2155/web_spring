package kr.ac.kopo.ctc.kopo21.board.service;

import kr.ac.kopo.ctc.kopo21.board.domain.User;
import kr.ac.kopo.ctc.kopo21.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(String username, String rawPassword) {
        userRepository.findByUsername(username).ifPresent(u -> {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        });
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRoles("USER");  // ★ 저장 전 보장

        userRepository.save(u);
    }
}