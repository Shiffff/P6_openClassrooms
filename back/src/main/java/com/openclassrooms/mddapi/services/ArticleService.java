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

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final ThemeRepository themeRepository;
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

        return buildArticlesResDTO(article, commentRepository.findByArticleId(article.getId()));
    }

    public ArticlesResDTO getArticleWithCommentsById(Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return null;
        }

        return buildArticlesResDTO(article, commentRepository.findByArticleId(article.getId()));
    }

    public List<ArticlesResDTO> getArticlesFromSubscribedThemesByUser() {
        User currentUser = userService.getCurrentUserWithSubscriptions();
        List<Theme> subscribedThemes = currentUser.getSubscriptions().stream()
                .map(UserSubscription::getTheme)
                .collect(Collectors.toList());
        List<Article> articles = articleRepository.findAllByThemeIn(subscribedThemes);

        return articles.stream()
                .map(article -> buildArticlesResDTO(article, commentRepository.findByArticleId(article.getId())))
                .collect(Collectors.toList());
    }

    private ArticlesResDTO buildArticlesResDTO(Article article, List<Comment> comments) {
        ArticlesResDTO articlesResDTO = new ArticlesResDTO();
        articlesResDTO.setId(article.getId());
        articlesResDTO.setTitle(article.getTitle());
        articlesResDTO.setContent(article.getContent());
        articlesResDTO.setAuthor(article.getAuthor());
        articlesResDTO.setThemeId(article.getTheme().getId());
        articlesResDTO.setUpdatedAt(article.getUpdated_at().toString());
        articlesResDTO.setCreatedAt(article.getCreated_at().toString());

        List<CommentDTO> commentDTOs = comments.stream()
                .map(comment -> {
                    CommentDTO commentDTO = new CommentDTO();
                    commentDTO.setContent(comment.getContent());
                    commentDTO.setArticleId(comment.getArticle().getId());
                    commentDTO.setAuthor(comment.getAuthor());
                    return commentDTO;
                })
                .collect(Collectors.toList());

        articlesResDTO.setComments(commentDTOs);

        return articlesResDTO;
    }
}
