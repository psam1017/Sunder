package net.sunder.english.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "sentences")
@Getter @Setter
public class Sentence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "sentence", nullable = false)
    private String sentence;

    @Column(name = "meaning", nullable = false)
    private String meaning;
}
