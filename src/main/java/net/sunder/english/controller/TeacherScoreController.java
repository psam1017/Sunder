package net.sunder.english.controller;

import lombok.RequiredArgsConstructor;
import net.sunder.english.domain.Score;
import net.sunder.english.domain.Student;
import net.sunder.english.domain.Teacher;
import net.sunder.english.service.TeacherScoreService;
import net.sunder.english.service.TeacherService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teachers/scores")
@RequiredArgsConstructor
public class TeacherScoreController {

    private final TeacherService teacherService;
    private final TeacherScoreService scoreService;
    private final MessageSource messageSource;

    // 성적 조회
    @GetMapping("/")
    public String getAllScores(@SessionAttribute Long teacherId, Model model) {
        Teacher teacher = teacherService.getTeacher(teacherId);
        List<Student> students = teacher.getStudents();
        model.addAttribute("students", students);
        return "/teachers/scores";
    }

    @GetMapping("/cond")
    public String getScoresByCond(@SessionAttribute Long teacherId,
                              @RequestParam String studentName,
                              @RequestParam String title,
                              Model model) {

        Teacher teacher = teacherService.getTeacher(teacherId);
        List<Student> students = teacher.getStudents();

        // inflearn 김영한 spring 강의 참고한 스타일
        List<Student> studentCond = students.stream()
                .filter(student -> {
                    if (ObjectUtils.isEmpty(studentName)) {
                        return true;
                    }
                    return student.getStudentName().equals(studentName);
                })
                .collect(Collectors.toList());

        // 내가 만들어 본 스타일
        if (ObjectUtils.isEmpty(title)) {
            model.addAttribute("students", studentCond);
        }
        else{
            List<Student> titleCond = new ArrayList<>();

            for (Student student : studentCond) {
                student.getScores().stream()
                        .filter(score -> score.getBook().getTitle().equals(title))
                        .forEach(score -> titleCond.add(student));
            }

            model.addAttribute("students", titleCond);
        }

        return "/teachers/scores";
    }

    // 성적 삭제. ajax 로 구현
    @PostMapping("/delete/{scoreId}")
    @ResponseBody
    public Map<String, String> deleteScore(@SessionAttribute Long teacherId, @PathVariable Long scoreId) {

        Map<String, String> json = new HashMap<>();
        Score score = scoreService.getScore(scoreId);

        if (score == null) {
            json.put("status", "fail");
            json.put("message", messageSource.getMessage("NotFound.score", null, null));
        }
        else if (!score.getStudent().getTeacher().getId().equals(teacherId)) {
            json.put("status", "fail");
            json.put("message", messageSource.getMessage("NotMatch.score", null, null));
        }
        else {
            scoreService.deleteScore(scoreId);
            json.put("status", "success");
            json.put("message", messageSource.getMessage("Delete.score", null, null));
        }

        return json;
    }
}
