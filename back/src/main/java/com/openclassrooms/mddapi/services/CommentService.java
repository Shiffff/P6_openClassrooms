package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ArticleService articleService;

    public void createComment(CommentDTO commentDTO) {
        User currentUser = userService.getCurrentUser();
        Article article = articleService.getArticleById(commentDTO.getArticleId());
        if (article == null) {
            throw new IllegalArgumentException("Article not found");
        }
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setAuthor(currentUser.getUsername());
        comment.setArticle(article);
        commentRepository.save(comment);
    }
}
