package net.sunder.english.repository.jpa;

import net.sunder.english.domain.SentenceWrong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentenceWrongRepository extends JpaRepository<SentenceWrong, Long> {
}
