package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.entity.Theme;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.entity.UserSubscription;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserSubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserService userService;

    public List<Theme> findAll() {
        return themeRepository.findAll();
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
    public List<Theme> getSubscribedThemesByUser(User user) {
        return user.getSubscriptions().stream()
                .map(UserSubscription::getTheme)
                .toList();
    }


}
