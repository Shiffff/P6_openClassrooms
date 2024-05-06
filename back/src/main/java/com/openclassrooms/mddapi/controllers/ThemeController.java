package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.entity.Theme;
import com.openclassrooms.mddapi.services.ThemeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class ThemeController {

    private final ThemeService themeService;

    @GetMapping(path = "/themes")
    public ResponseEntity<List<Theme>> findAll() {
        return ResponseEntity.ok().body(themeService.findAll());
    }

    @PostMapping(path = "/themes/{themeId}/subscribe")
    public ResponseEntity<String> subscribe(@PathVariable Long themeId) {
        String message = themeService.subscribeToTheme(themeId);
        return ResponseEntity.ok().body(message);
    }

    @PostMapping(path = "/themes/{themeId}/unsubscribe")
    public ResponseEntity<String> unsubscribe(@PathVariable Long themeId) {
        String message = themeService.unsubscribeFromTheme(themeId);
        return ResponseEntity.ok().body(message);
    }
}
