package models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "question")
public class Question {
    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "question_type")
    private QuestionType type;
    @Column(name = "question_text")
    private String text;
    @Column(name = "question_image")
    private String image;
    @Column(name = "question_answer_rows")
    private int answerRows;
    @Column(name = "question_variant_a")
    private String variantA;
    @Column(name = "question_variant_b")
    private String variantB;
    @Column(name = "question_variant_c")
    private String variantC;
    @Column(name = "question_variant_d")
    private String variantD;
    @Column(name = "question_answer")
    private String answer;
    @Column(name = "question_points")
    private int points;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "question_exam",nullable = false)
    private Exam exam;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "question_professor",nullable = false)
    private User professor;
}
