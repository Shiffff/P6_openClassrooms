package com.openclassrooms.mddapi.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private String content;
    private Long articleId;
    private String author;
}
