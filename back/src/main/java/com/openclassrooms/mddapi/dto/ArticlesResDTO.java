package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String updatedAt;
    private String createdAt;
}
