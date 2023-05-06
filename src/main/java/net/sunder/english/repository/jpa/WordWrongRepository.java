package net.sunder.english.repository.jpa;

import net.sunder.english.domain.WordWrong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordWrongRepository extends JpaRepository<WordWrong, Long> {
}
