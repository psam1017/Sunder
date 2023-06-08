package net.sunder.english.controller;

import lombok.RequiredArgsConstructor;
import net.sunder.english.controller.form.TeacherEditForm;
import net.sunder.english.controller.form.TeacherJoinForm;
import net.sunder.english.controller.form.JoinId;
import net.sunder.english.domain.Teacher;
import net.sunder.english.service.TeacherService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private final MessageSource messageSource;

    @GetMapping("/teachers/main")
    public String teacherMain(@SessionAttribute Long teacherId, Model model) {
        Teacher teacher = teacherService.getTeacher(teacherId);
        model.addAttribute("teacher", teacher);
        return "/teachers/main";
    }

    @GetMapping("/teachers/join")
    public String joinForm(@ModelAttribute TeacherJoinForm teacherJoinForm) {
        return "/teachers/join";
    }

    @PostMapping("/teachers/check")
    @ResponseBody
    public Map<String, Object> validateTeacherId(@ModelAttribute @Validated JoinId joinId, BindingResult bindingResult) {
        Map<String, Object> map = new HashMap<>();

        if (bindingResult.hasErrors()) {
            List<String> messages = new ArrayList<>();

            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach(error -> messages.add(error.getDefaultMessage()));
            map.put("error", messages);

            return map;
        }

        if (teacherService.isUniqueTeacherId(joinId.getId())) {
            map.put("result", "사용가능한 아이디입니다.");
            map.put("status", true);
        } else {
            map.put("result", messageSource.getMessage("duplicate.join.id", null, null));
            map.put("status", false);
        }
        return map;
    }

    @PostMapping("/teachers/join")
    public String join(@ModelAttribute @Validated TeacherJoinForm teacherJoinForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/teachers/join";
        }

        if (teacherService.isUniqueTeacherId(teacherJoinForm.getId())) {
            String encodedPassword = Base64Utils.encodeToString(teacherJoinForm.getPassword().getBytes());
            teacherService.createAccount(teacherJoinForm.getId(), encodedPassword, teacherJoinForm.getName());
            return "redirect:/teachers/welcome";
        } else {
            bindingResult.rejectValue("id", "duplicate");
            return "/teachers/join";
        }
    }

    @GetMapping("/teachers/login")
    public String loginForm() {
        return "/teachers/login";
    }

    @PostMapping("/teachers/login")
    public String login(@RequestParam String teacherId,
                        @RequestParam String password,
                        @RequestParam(defaultValue = "/teachers/main") String redirectURL,
                        RedirectAttributes redirectAttributes,
                        Model model,
                        HttpSession session) {

        String encodedPassword = Base64Utils.encodeToString(password.getBytes());
        Teacher loginTeacher = teacherService.login(teacherId, encodedPassword);

        if(loginTeacher != null){
            session.setAttribute("teacherId", loginTeacher.getId());
            return "redirect:" + redirectURL;
        }
        else{
            model.addAttribute("message", messageSource.getMessage("NotValid.login", null, null));
            redirectAttributes.addAttribute("redirectURL", redirectURL);
            return "/teachers/login";
        }
    }

    @GetMapping("/teachers/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/teachers/edit")
    public String editForm(Model model, @SessionAttribute Long teacherId) {
        Teacher teacher = teacherService.getTeacher(teacherId);
        model.addAttribute("teacher", teacher);

        return "/teachers/edit";
    }

    @PostMapping("/teachers/edit")
    public String edit(@ModelAttribute @Validated TeacherEditForm teacherEditForm, BindingResult bindingResult, @SessionAttribute Long teacherId) {
        if (bindingResult.hasErrors()) {
            return "/teachers/edit";
        }

        String encodedPassword = Base64Utils.encodeToString(teacherEditForm.getPassword().getBytes());
        teacherService.updateAccount(teacherId, teacherEditForm.getName(), encodedPassword);

        return "redirect:/teachers/edit";
    }

    @GetMapping("/teachers/withdraw")
    public String withdraw(@SessionAttribute Long teacherId) {
        teacherService.withdraw(teacherId);
        return "redirect:/";
    }
}
