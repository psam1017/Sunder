package net.sunder.english.controller;

import lombok.RequiredArgsConstructor;
import net.sunder.english.controller.form.BookInfoForm;
import net.sunder.english.domain.Book;
import net.sunder.english.domain.Student;
import net.sunder.english.domain.Teacher;
import net.sunder.english.service.OnlyStudentService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class OnlyStudentController {

    private final OnlyStudentService studentService;
    private final MessageSource messageSource;

    @GetMapping("/main")
    public String studentMain(@SessionAttribute Long studentId, Model model) {
        Student student = studentService.getStudent(studentId);
        model.addAttribute("student", student);
        return "/students/main";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "/students/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String studentId,
                        @RequestParam String password,
                        @RequestParam(defaultValue = "/students/main") String redirectURL,
                        RedirectAttributes redirectAttributes,
                        Model model,
                        HttpSession session) {

        String encodedPassword = Base64Utils.encodeToString(password.getBytes());
        Student student = studentService.login(studentId, encodedPassword);

        if (student != null) {
            session.setAttribute("studentId", student.getId());
            return "redirect:" + redirectURL;
        }
        else{
            model.addAttribute("message", messageSource.getMessage("NotValid.login", null, null));
            redirectAttributes.addAttribute("redirectURL", redirectURL);
            return "/students/login";
        }
    }

    // 교재 목록 조회
    @GetMapping("/books")
    public String getAllBooks(@SessionAttribute Long studentId, Model model) {
        Student student = studentService.getStudent(studentId);
        Teacher teacher = student.getTeacher();
        Long teacherId = teacher.getId();

        List<Book> books = studentService.getBookList(teacherId);
        model.addAttribute("books", books);

        return "/students/books";
    }

    // 교재 조회 -> 시험 시작하기
    @PostMapping("/books")
    public String getBookByInfo(
            @SessionAttribute Long studentId,
            @Validated @ModelAttribute BookInfoForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/students/books";
        }

        Student student = studentService.getStudent(studentId);
        Teacher teacher = student.getTeacher();

        Book foundBook = studentService.getBookByInfo(form.getTitle(), form.getChapter(), form.getContentType(), teacher.getId());

        redirectAttributes.addAttribute("bookId", foundBook.getId());
        return "redirect:/study/exam/{bookId}";
    }
}
