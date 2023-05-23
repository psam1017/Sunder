package net.sunder.english.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Getter @Setter
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_id", nullable = false, unique = true)
    private String teacherId;

    @Column(name = "teacher_name", nullable = false)
    private String teacherName;

    @Column(name = "password", nullable = false)
    private String password;

    /*
    OneToMany 는 편의상 만드는 객체.
    1. ManyToOne 매핑으로 이미 연관관계 매핑은 충분하다.
    2. 또한, 연관관계의 주인은 외래키가 참조하는 테이블에 있음을 기억하자
        -> 즉, student 객체에 setTeacher 를 꼭 해야 한다.
    3. OneToMany 는 컬럼이 아니다.

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    -> 위와 같이 cascade 와 orphanRemoval 을 사용하면 JPA 레벨에서 cascade 삭제와 수정을 실시하며, 이 경우 n + 1 현상이 발생한다.
     */
    @OneToMany(mappedBy = "teacher", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "teacher", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    List<Book> books = new ArrayList<>();
}
