package net.sunder.english.service.impl;

import lombok.RequiredArgsConstructor;
import net.sunder.english.domain.Score;
import net.sunder.english.repository.ScoreRepository;
import net.sunder.english.service.TeacherScoreService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherScoreServiceImpl implements TeacherScoreService {

    private final ScoreRepository scoreRepository;

    @Override
    public Score getScore(Long scoreId) {
        return scoreRepository.findById(scoreId).orElse(null);
    }

    @Override
    public void deleteScore(Long scoreId) {
        scoreRepository.deleteById(scoreId);
    }
}
