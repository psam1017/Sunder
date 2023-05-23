package net.sunder.english.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter @Setter
public class JoinId {

    @NotBlank(message = "{NotBlank.join.id}")
    @Size(min = 3, max = 10, message = "{Size.join.id}")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]*$", message = "{Pattern.join.id}")
    private String id;
}
