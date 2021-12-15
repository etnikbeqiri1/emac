package models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "exam")
public class Exam {

    @Id
    @Column(name = "exam_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "exam_name")
    private String name;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "exam_professor", nullable=false)
    private User professor;

    @OneToMany(mappedBy = "exam", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Question> questionList = new ArrayList<Question>();

    @Override
    public String toString(){
        return name;
    }
}
