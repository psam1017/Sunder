package net.sunder.english.domain.enumtype;

import lombok.Getter;

@Getter
public enum ContentType {

    WORD("단어"), MAIN_TEXT("교과서 본문"), SUB_TEXT("추가 지문");

    private final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }
}
