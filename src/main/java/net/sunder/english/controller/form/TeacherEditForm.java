package net.sunder.english.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter @Setter
public class TeacherEditForm {

    @NotBlank(message = "{NotBlank.edit.name}")
    @Size(min = 2, max = 5, message = "{Size.edit.name}")
    @Pattern(regexp = "^[가-힣]+$", message = "{Pattern.edit.name}")
    private String name;

    @NotBlank(message = "{NotBlank.edit.password}")
    @Size(min = 8, max = 20, message = "{Size.edit.password}")
    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[0-9]).{8,20}$", message = "{Pattern.edit.password}")
    private String password;
}
