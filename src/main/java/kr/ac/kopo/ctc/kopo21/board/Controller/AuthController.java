package kr.ac.kopo.ctc.kopo21.board.Controller;

import kr.ac.kopo.ctc.kopo21.board.config.JwtTokenService;
import kr.ac.kopo.ctc.kopo21.board.domain.AuthRequest;
import kr.ac.kopo.ctc.kopo21.board.domain.AuthResponse;
import org.hibernate.sql.exec.spi.StandardEntityInstanceResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtTokenService jwtTokenService;

    @PostMapping("/register")
    public String registerUser(@RequestBody AuthRequest authRequest) {
        return jwtTokenService.registerUser(authRequest);

    }

    @PostMapping("/authenticate")
    @ResponseBody
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = jwtTokenService.authenticateUser(authRequest);
        if(jwtTokenService.isUserAuthenticated()) {
            return ResponseEntity.ok(authResponse);
        }else {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }
    }
}
