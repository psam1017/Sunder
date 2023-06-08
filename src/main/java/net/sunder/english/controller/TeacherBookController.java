package net.sunder.english.controller;

import lombok.RequiredArgsConstructor;
import net.sunder.english.controller.form.BookInfoForm;
import net.sunder.english.domain.Content;
import net.sunder.english.domain.Teacher;
import net.sunder.english.service.TeacherBookService;
import net.sunder.english.service.TeacherService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers/books")
public class TeacherBookController {

    private final TeacherService teacherService;
    private final TeacherBookService bookService;
    private final MessageSource messageSource;

    @PostMapping("/upload")
    public String uploadFile(@ModelAttribute @Validated BookInfoForm form, BindingResult bindingResult,
                             @SessionAttribute Long teacherId,
                             @RequestParam MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "/teachers/books";
        }

        Teacher teacher = teacherService.getTeacher(teacherId);

        List<String> phrases = new ArrayList<>();
        List<String> meanings = new ArrayList<>();

        String filename = file.getOriginalFilename();
//        InputStream inputStream = file.getInputStream();

        if (filename.endsWith(".csv")) {
//            parseCSV(inputStream, phrases, meanings);
        }
        else if(filename.endsWith(".xls") || filename.endsWith("xlsx")){

        }
        else{

        }

//        bookService.createBook();

        return "redirect:/teachers/books";
    }

    private void parseCSV(InputStream is, List<String> phrases, List<String> meanings) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String line;

        int i = 0;
        while ((line = reader.readLine()) != null) {
            if(i == 0){
                i++;
                continue;
            }

            String[] fields = line.split(",");
            phrases.add(fields[0]);
            meanings.add(fields[1]);
        }
    }

    private void parseExcel(InputStream is, List<String> phrases, List<String> meanings) throws Exception {

    }
}
