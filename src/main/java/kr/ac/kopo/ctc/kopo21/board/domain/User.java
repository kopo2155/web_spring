package kr.ac.kopo.ctc.kopo21.board.domain;

import jakarta.persistence.*;

@Entity

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String name;

    private String roles = "USER";

    public Long getId() { return id; }

    public void setUsername(String username) {
        this.username = username;

    }
    public String getUsername() {
        return this.username;
    }
    public void setPassword(String password) {
        this.password = password;

    }
    public String getPassword() {
        return this.password;
    }
    public void setRoles(String roles) {
        this.roles = roles;

    }
    public String getRoles() {
        return this.roles;
    }

    public void setName(String name) {
        this.name = name;

    }
    public String getName() {
        return this.name;
    }


}
