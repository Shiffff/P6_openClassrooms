package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.ArticlesResDTO;
import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.services.ArticleService;
import com.openclassrooms.mddapi.services.CommentService;
import com.openclassrooms.mddapi.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
public class ArticleController {

    private final ArticleService articleService;
    private UserService userService;
    private final CommentService commentService;

    @PostMapping(path = "/article")
    public ResponseEntity<ArticlesResDTO> createArticle(@RequestBody ArticleDTO articleDTO) {
        ArticlesResDTO createdArticle = articleService.createArticle(articleDTO);
        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }

    @GetMapping(path = "/article/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        if (article == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(article);
        }
    }
    @GetMapping(path = "/articles/subscribed")
    public ResponseEntity<List<ArticlesResDTO>> getArticlesWithCommentsFromSubscribedThemesByUser() {
        List<ArticlesResDTO> articlesWithComments = articleService.getArticlesWithCommentsFromSubscribedThemesByUser();
        return ResponseEntity.ok(articlesWithComments);
    }

    @PostMapping("/comment")
    public ResponseEntity<String> createComment(@RequestBody CommentDTO commentDTO) {
        commentService.createComment(commentDTO);
        return ResponseEntity.ok("Votre commentaire a été enregistré avec succès.");
    }


}