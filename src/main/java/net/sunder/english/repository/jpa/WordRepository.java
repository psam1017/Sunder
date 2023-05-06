package net.sunder.english.repository.jpa;

import net.sunder.english.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    
}
