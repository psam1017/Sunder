package net.sunder.english.controller;

import lombok.RequiredArgsConstructor;
import net.sunder.english.controller.form.JoinId;
import net.sunder.english.controller.form.StudentEditForm;
import net.sunder.english.controller.form.StudentJoinForm;
import net.sunder.english.domain.Student;
import net.sunder.english.domain.Teacher;
import net.sunder.english.service.TeacherService;
import net.sunder.english.service.TeacherStudentService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/teachers/students")
@RequiredArgsConstructor
public class TeacherStudentController {

    private final TeacherService teacherService;
    private final TeacherStudentService teacherStudentService;
    private final MessageSource messageSource;

    // 아이디 중복 검사
    @PostMapping("/check")
    @ResponseBody
    public Map<String, Object> validateStudentId(@ModelAttribute @Validated JoinId joinId, BindingResult bindingResult) {
        Map<String, Object> map = new HashMap<>();

        if (bindingResult.hasErrors()) {
            List<String> messages = new ArrayList<>();

            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach(error -> messages.add(error.getDefaultMessage()));
            map.put("error", messages);

            return map;
        }

        if (teacherStudentService.isUniqueStudentId(joinId.getId())) {
            map.put("result", "사용가능한 아이디입니다.");
            map.put("status", true);
        } else {
            map.put("result", messageSource.getMessage("duplicate.join.id", null, null));
            map.put("status", false);
        }
        return map;
    }

    // 학생 추가
    @GetMapping("/new")
    public String joinForm(@ModelAttribute StudentJoinForm studentJoinForm) {
        return "/teachers/students/new";
    }

    @PostMapping("/new")
    public String join(@ModelAttribute @Validated StudentJoinForm form, BindingResult bindingResult,
                       @SessionAttribute Long teacherId) {

        if (bindingResult.hasErrors()) {
            return "/teachers/students/new";
        }

        if (teacherStudentService.isUniqueStudentId(form.getId())) {
            Teacher teacher = teacherService.getTeacher(teacherId);
            String encodedPassword = Base64Utils.encodeToString(form.getPassword().getBytes());

            teacherStudentService.createAccount(form.getId(), encodedPassword, form.getName(), form.getGrade(), teacher);
            return "redirect:/teachers/students/list";
        } else {
            bindingResult.rejectValue("id", "duplicate");
            return "/teachers/students/new";
        }
    }

    // 학생 목록 조회
    // 필터는 js
    @GetMapping("/")
    public String getList(@SessionAttribute Long teacherId, Model model) {

        List<Student> students = teacherStudentService.getAllStudent(teacherId);
        model.addAttribute("students", students);
        return "/teachers/students/list";
    }

    // 학생 수정
    @GetMapping("/edit/{studentId}")
    public String editForm(Model model, @PathVariable Long studentId, @SessionAttribute Long teacherId) {
        Student student = teacherStudentService.getStudent(studentId);

        if (student.getTeacher().getId().equals(teacherId)) {
            model.addAttribute("student", student);
            return "/teachers/students/edit";
        } else {
            model.addAttribute("message", messageSource.getMessage("NotMatch.student", new Object[]{student}, null));
            return "/teachers/students/list";
        }
    }

    @PostMapping("/edit/{studentId}")
    public String edit(@ModelAttribute @Validated StudentEditForm form, BindingResult bindingResult,
                       @PathVariable Long studentId,
                       @SessionAttribute Long teacherId,
                       Model model) {

        if (bindingResult.hasErrors()) {
            return "/teachers/students/edit";
        }

        Teacher teacher = teacherService.getTeacher(teacherId);
        Student student = teacherStudentService.getStudent(studentId);

        if (student.getTeacher().getId().equals(teacherId)) {
            teacherStudentService.updateAccount(teacher, studentId, form.getName(), form.getPassword(), form.getGrade());
            return "redirect:/teachers/students/list";
        }
        else{
            model.addAttribute("message", messageSource.getMessage("NotMatch.student", new Object[]{student}, null));
            return "/teachers/students/edit";
        }
    }

    // 학생 삭제
    @PostMapping("/withdraw/{studentId}")
    public String withdraw(@SessionAttribute Long teacherId, @PathVariable Long studentId, Model model) {
        Student student = teacherStudentService.getStudent(studentId);
        Teacher teacher = teacherService.getTeacher(teacherId);

        if (student.getTeacher().getId().equals(teacherId)) {
            teacherStudentService.withdraw(teacher, studentId);
            model.addAttribute("message", messageSource.getMessage("withdraw.student", null, null));
        }
        else{
            model.addAttribute("message", messageSource.getMessage("NotMatch.student", new Object[]{student}, null));
        }
        return "/teachers/students/list";
    }

    // 학생 개인의 성적 조회, 성적 삭제 필요
    
}
