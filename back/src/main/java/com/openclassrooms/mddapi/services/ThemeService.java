package com.openclassrooms.mddapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.ThemeDTO;
import com.openclassrooms.mddapi.entity.Theme;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.entity.UserSubscription;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.repository.UserSubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public List<ThemeDTO> getThemesWithSubscriptionStatus(User currentUser) {
        List<Theme> themes = findAll();
        return themes.stream()
                .map(theme -> {
                    ThemeDTO themeDto = new ThemeDTO(theme);
                    themeDto.setSubscribed(isUserSubscribedToTheme(currentUser, theme));
                    return themeDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public String subscribeToTheme(Long themeId) {
        User user = userService.getCurrentUser();

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("Theme not found with id: " + themeId));

        UserSubscription existingSubscription = userSubscriptionRepository.findByUserAndTheme(user, theme);
        if (existingSubscription != null) {
            return "You are already subscribed to this theme!";
        }

        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUser(user);
        userSubscription.setTheme(theme);
        userSubscriptionRepository.save(userSubscription);

        return "Subscribed successfully to the theme: " + theme.getTitle();
    }

    @Transactional
    public String unsubscribeFromTheme(Long themeId) {
        User user = userService.getCurrentUser();

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("Theme not found with id: " + themeId));

        UserSubscription existingSubscription = userSubscriptionRepository.findByUserAndTheme(user, theme);
        if (existingSubscription == null) {
            return "You are not subscribed to this theme!";
        }

        userSubscriptionRepository.delete(existingSubscription);

        return "Unsubscribed successfully from the theme: " + theme.getTitle();
    }

    @Transactional
    public List<Theme> getSubscribedThemesByUser(String username) {
        User user = userRepository.findByUsernameWithSubscriptions(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        return user.getSubscriptions().stream()
                .map(UserSubscription::getTheme)
                .collect(Collectors.toList());
    }

    public boolean isUserSubscribedToTheme(User user, Theme theme) {
        UserSubscription existingSubscription = userSubscriptionRepository.findByUserAndTheme(user, theme);
        return existingSubscription != null;
    }
}
