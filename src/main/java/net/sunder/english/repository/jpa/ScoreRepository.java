package net.sunder.english.repository.jpa;

import net.sunder.english.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

//    @Query("SELECT s FROM Score s WHERE s.student.id = :studentId AND s.book.id = :bookId")
//    Optional<Score> findByStudentIdAndBookId(@Param("studentId") Long studentId, @Param("bookId") Long bookId);

    // 이렇게 하면 한 번에 student, book, wrong을 찾아올까?
    Optional<Score> findByStudentIdAndBookId(Long studentId, Long bookId);
}
