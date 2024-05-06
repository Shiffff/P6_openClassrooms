package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.entity.Theme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThemeDTO {

    private Long id;
    private String title;
    private String description;
    private Date created_at;
    private LocalDateTime updated_at;

    public ThemeDTO(Theme theme) {
        this.id = theme.getId();
        this.title = theme.getTitle();
        this.description = theme.getDescription();
        this.created_at = theme.getCreated_at();
        this.updated_at = theme.getUpdated_at();
    }
}
