package net.sunder.english.service;

import net.sunder.english.domain.Content;

import java.util.List;

public interface StudyService {

    List<Content> startExam(Long bookId);
    // 1. startTest 단어 목록을 가져와서 랜덤으로 돌리기
    // (1) 랜덤 리스트로 만들고 반환

    List<Content> finishExam(Long studentId, Long bookId, List<Content> submit);
    // 2. transaction 보장 saveTestResult -> WrongList 를 바로 반환
    // (1) 재시험일 수 있으니, 시험결과를 제출하면 book id 를 가진 content 들을 모두 삭제
    // (2) 랜덤으로 돌린 리스트는 id를 기준으로 정렬하기
    // (3) 정렬된 리스트와 원본을 비교하기
    // (4) lowercase, 쉼표, 마침표 모두 제거하기
    // (5) 틀린 보기의 리스트를 Student Entity 의 Score, WrongList 에 저장, save
}
