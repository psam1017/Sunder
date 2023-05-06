package net.sunder.english.domain;

import lombok.Getter;
import lombok.Setter;
import net.sunder.english.domain.enumtype.Grade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Getter @Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false, unique = true)
    private String studentId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    // EnumType 사용 예제
    // https://lng1982.tistory.com/280
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "grade", nullable = false)
    private Grade grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "student")
    List<Score> scores = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    List<WordWrong> wordWrongs = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    List<SentenceWrong> sentenceWrongs = new ArrayList<>();
}