package com.openclassrooms.mddapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    @Size(min = 2, max = 100)
    private String title;

    @NotBlank
    @NotNull
    @Size(min = 2)
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotBlank
    @NotNull
    @Size(min = 2, max = 50)
    private String author;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    @CreationTimestamp
    private Date created_at;
}
