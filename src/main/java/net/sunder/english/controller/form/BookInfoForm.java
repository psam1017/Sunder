package net.sunder.english.controller.form;

import lombok.Getter;
import lombok.Setter;
import net.sunder.english.domain.enumtype.ContentType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class BookInfoForm {

    @NotBlank(message = "{NotBlank.book.title}")
    private String title;
    @NotBlank(message = "{NotBlank.book.chapter}")
    private String chapter;
    @NotNull(message = "{NotNull.book.contentType}")
    private ContentType contentType;
}
