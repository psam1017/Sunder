package net.sunder.english.service;

import net.sunder.english.domain.Score;

public interface TeacherScoreService {

    public Score getScore(Long scoreId);
    public void deleteScore(Long scoreId);
}
