package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.ArticlesResDTO;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.Theme;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.entity.UserSubscription;
import com.openclassrooms.mddapi.repository.ArticleRepository;
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
    private final ThemeService themeService;

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
    public List<Article> getAllArticlesFromSubscribedThemesByUser() {
        User currentUser = userService.getCurrentUserWithSubscriptions();
        List<Theme> subscribedThemes = currentUser.getSubscriptions().stream()
                .map(UserSubscription::getTheme)
                .collect(Collectors.toList());
        return articleRepository.findAllByThemeIn(subscribedThemes);
    }




}
