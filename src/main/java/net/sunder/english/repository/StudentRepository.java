package net.sunder.english.repository;

import net.sunder.english.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s JOIN FETCH s.teacher WHERE s.teacher.id = :teacherId ORDER BY s.grade DESC, s.studentName ASC")
    List<Student> findAllByTeacherIdOrderByInfo(@Param("teacherId") Long teacherId);

    boolean existsByStudentId(String studentId);
}
