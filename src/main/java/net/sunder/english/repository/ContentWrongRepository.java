package net.sunder.english.repository;

import net.sunder.english.domain.ContentWrong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentWrongRepository extends JpaRepository<ContentWrong, Long> {

}
