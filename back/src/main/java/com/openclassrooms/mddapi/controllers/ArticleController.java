package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.ArticlesResDTO;
import com.openclassrooms.mddapi.dto.CommentDTO;
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
    private final UserService userService;
    private final CommentService commentService;

    @PostMapping(path = "/article")
    public ResponseEntity<ArticlesResDTO> createArticle(@RequestBody ArticleDTO articleDTO) {
        ArticlesResDTO createdArticle = articleService.createArticle(articleDTO);
        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }

    @GetMapping(path = "/article/{id}")
    public ResponseEntity<ArticlesResDTO> getArticleById(@PathVariable Long id) {
        ArticlesResDTO articleWithComments = articleService.getArticleWithCommentsById(id);
        return articleWithComments != null ? ResponseEntity.ok(articleWithComments) : ResponseEntity.notFound().build();
    }


    @GetMapping(path = "/articles/subscribed")
    public ResponseEntity<List<ArticlesResDTO>> getArticlesWithCommentsFromSubscribedThemesByUser() {
        List<ArticlesResDTO> articlesWithComments = articleService.getArticlesFromSubscribedThemesByUser();
        return ResponseEntity.ok(articlesWithComments);
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        CommentDTO createdComment = commentService.createComment(commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

}

