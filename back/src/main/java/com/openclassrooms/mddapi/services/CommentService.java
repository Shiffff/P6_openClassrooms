package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.ArticlesResDTO;
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

    public CommentDTO createComment(CommentDTO commentDTO) {
        User currentUser = userService.getCurrentUser();
        ArticlesResDTO articleWithComments = articleService.getArticleWithCommentsById(commentDTO.getArticleId());
        if (articleWithComments == null) {
            throw new IllegalArgumentException("Article not found");
        }
        Article article = new Article();
        article.setId(articleWithComments.getId());
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setAuthor(currentUser.getUsername());
        comment.setArticle(article);
        Comment savedComment = commentRepository.save(comment);

        CommentDTO savedCommentDTO = new CommentDTO();
        savedCommentDTO.setContent(savedComment.getContent());
        savedCommentDTO.setArticleId(savedComment.getArticle().getId());
        savedCommentDTO.setAuthor(savedComment.getAuthor());
        return savedCommentDTO;
    }


}
