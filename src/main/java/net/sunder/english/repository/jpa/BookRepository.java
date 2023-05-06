package net.sunder.english.repository.jpa;

import net.sunder.english.domain.Book;
import net.sunder.english.domain.enumtype.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // https://velog.io/@ohzzi/Data-Jpa-findByXXXId-는-불필요한-join을-유발한다
    @Query("SELECT b FROM Book b WHERE b.title = :title AND b.chapter = :chapter AND b.contentType = :contentType AND b.teacher.id = :teacherId")
    Optional<Book> findByBookInfo(@Param("title")String title,
                            @Param("chapter")String chapter,
                            @Param("contentType")ContentType contentType,
                            @Param("teacherId")Long teacherId);

    @Query("SELECT b FROM Book b WHERE b.teacher.id = :teacherId")
    List<Book> findAllByTeacherId(@Param("teacherId") Long teacherId);
}
