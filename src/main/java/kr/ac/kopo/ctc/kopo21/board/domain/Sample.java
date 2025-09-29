package kr.ac.kopo.ctc.kopo21.board.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Sample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String title;


    public Long getId() {
        return id;

    }

    public void setId(Long id){
        this.id = id;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }


    public Sample() {
    }

    public Sample(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
