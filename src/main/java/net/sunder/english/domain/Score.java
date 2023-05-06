package net.sunder.english.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scores", uniqueConstraints = {@UniqueConstraint(columnNames = {"student_id", "book_id"})})
@Getter @Setter
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "wrong_count", nullable = false)
    private Integer wrongCount;

    @Column(name = "submit_date", nullable = false)
    private LocalDateTime submitDate;
}
