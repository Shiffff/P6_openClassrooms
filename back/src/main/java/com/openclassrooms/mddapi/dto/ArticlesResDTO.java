package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticlesResDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
    private Long themeId;
    private String themeName;
    private String updatedAt;
    private String createdAt;
    private List<CommentDTO> comments;
}

