package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.ThemeDTO;
import com.openclassrooms.mddapi.entity.Theme;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.services.ThemeService;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
public class ThemeController {

    private final ThemeService themeService;
    private final UserService userService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Opération réussie"),
            @ApiResponse(responseCode = "403", description = "Non autorisé")
    })
    @GetMapping(path = "/themes")
    public ResponseEntity<List<ThemeDTO>> findAll() {
        User currentUser = userService.getCurrentUser();
        List<ThemeDTO> themeDtos = themeService.getThemesWithSubscriptionStatus(currentUser);
        return ResponseEntity.ok().body(themeDtos);
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Opération réussie"),
            @ApiResponse(responseCode = "403", description = "Non autorisé")
    })
    @PostMapping(path = "/themes/{themeId}/subscribe")
    public ResponseEntity<Map<String, String>> subscribe(@PathVariable Long themeId) {
        String message = themeService.subscribeToTheme(themeId);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", message);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Opération réussie"),
            @ApiResponse(responseCode = "403", description = "Non autorisé")
    })
    @PostMapping(path = "/themes/{themeId}/unsubscribe")
    public ResponseEntity<Map<String, String>> unsubscribe(@PathVariable Long themeId) {
        String message = themeService.unsubscribeFromTheme(themeId);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", message);
        return ResponseEntity.ok().body(responseBody);
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Opération réussie"),
            @ApiResponse(responseCode = "403", description = "Non autorisé")
    })
    @GetMapping(path = "/themes/subscribed")
    public ResponseEntity<List<Theme>> getSubscribedThemes() {
        User currentUser = userService.getCurrentUser();
        List<Theme> subscribedThemes = themeService.getSubscribedThemesByUser(currentUser.getUsername());
        return ResponseEntity.ok().body(subscribedThemes);
    }
}
