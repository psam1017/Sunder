package net.sunder.english.domain;

import lombok.Getter;
import lombok.Setter;
import net.sunder.english.domain.enumtype.ContentType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Getter @Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "chapter", nullable = false)
    private String chapter;

    @Column(name = "content_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "amount")
    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "book")
    List<Word> words = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    List<Sentence> sentences = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    List<Score> scores = new ArrayList<>();
}