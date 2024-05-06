package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.ArticlesResDTO;
import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.entity.*;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final ThemeRepository themeRepository;
    private final ThemeService themeService;
    private final CommentRepository commentRepository;

    public ArticlesResDTO createArticle(ArticleDTO articleDTO) {
        User userInfo = userService.getCurrentUser();

        Theme theme = themeRepository.findById(articleDTO.getThemeId())
                .orElseThrow(() -> new IllegalArgumentException("Theme not found"));

        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setAuthor(userInfo.getUsername());
        article.setTheme(theme);

        article = articleRepository.save(article);

        ArticlesResDTO responseDTO = new ArticlesResDTO();
        responseDTO.setId(article.getId());
        responseDTO.setTitle(article.getTitle());
        responseDTO.setContent(article.getContent());
        responseDTO.setAuthor(article.getAuthor());
        responseDTO.setThemeId(article.getTheme().getId());
        responseDTO.setUpdatedAt(article.getUpdated_at().toString());
        responseDTO.setCreatedAt(article.getCreated_at().toString());

        return responseDTO;
    }
    public Article getArticleById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }
    public List<ArticlesResDTO> getArticlesWithCommentsFromSubscribedThemesByUser() {
        User currentUser = userService.getCurrentUserWithSubscriptions();
        List<Theme> subscribedThemes = currentUser.getSubscriptions().stream()
                .map(UserSubscription::getTheme)
                .collect(Collectors.toList());
        List<Article> articles = articleRepository.findAllByThemeIn(subscribedThemes);

        List<ArticlesResDTO> articlesWithComments = new ArrayList<>();

        for (Article article : articles) {
            ArticlesResDTO articleWithComments = new ArticlesResDTO();
            articleWithComments.setId(article.getId());
            articleWithComments.setTitle(article.getTitle());
            articleWithComments.setContent(article.getContent());
            articleWithComments.setAuthor(article.getAuthor());
            articleWithComments.setThemeId(article.getTheme().getId());
            articleWithComments.setUpdatedAt(article.getUpdated_at().toString());
            articleWithComments.setCreatedAt(article.getCreated_at().toString());

            List<Comment> comments = commentRepository.findByArticleId(article.getId());
            List<CommentDTO> commentDTOs = new ArrayList<>();
            for (Comment comment : comments) {
                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setContent(comment.getContent());
                commentDTO.setArticleId(comment.getArticle().getId());
                commentDTO.setAuthor(comment.getAuthor());
                commentDTOs.add(commentDTO);
            }
            articleWithComments.setComments(commentDTOs);

            articlesWithComments.add(articleWithComments);
        }

        return articlesWithComments;
    }




}
