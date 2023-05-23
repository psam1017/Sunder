package net.sunder.english.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter @Setter
public class TeacherJoinForm {

    @NotBlank(message = "{NotBlank.join.id}")
    @Size(min = 3, max = 10, message = "{Size.join.id}")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]*$", message = "{Pattern.join.id}")
    private String id;

    @NotBlank(message = "{NotBlank.join.password}")
    @Size(min = 8, max = 20, message = "{Size.join.password}")
    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[0-9]).{8,20}$", message = "{Pattern.join.password}")
    private String password;

    @NotBlank(message = "{NotBlank.join.name}")
    @Size(min = 2, max = 5, message = "{Size.join.name}")
    @Pattern(regexp = "^[가-힣]+$", message = "{Pattern.join.name}")
    private String name;
}
