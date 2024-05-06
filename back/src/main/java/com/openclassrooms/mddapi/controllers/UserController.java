package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.AuthentificationDTO;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.dto.UserInfoDTO;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.services.JWTService;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
@SecurityRequirement(name = "auth")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTService jwtService;

    @PostMapping(path = "auth/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object register(@RequestBody User user) {
        try {
            this.userService.register(user);
            return this.jwtService.generate(user.getEmail());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(path = "auth/login")
    public Map<String, String> login(@RequestBody AuthentificationDTO authentificationDTO){
        String emailOrUsername = authentificationDTO.emailOrUsername();
        String password = authentificationDTO.password();

        User userByEmail = userService.findByEmail(emailOrUsername);
        if (userByEmail != null) {
            Authentication authenticationByEmail = new UsernamePasswordAuthenticationToken(userByEmail.getEmail(), password);
            try {
                Authentication authenticate = authenticationManager.authenticate(authenticationByEmail);
                return this.jwtService.generate(userByEmail.getEmail());
            } catch (BadCredentialsException e) {
                throw new BadCredentialsException("Invalid email/username or password");
            }
        }

        User userByUsername = userService.findByUsername(emailOrUsername);
        if (userByUsername != null) {
            Authentication authenticationByUsername = new UsernamePasswordAuthenticationToken(userByUsername.getEmail(), password);
            try {
                Authentication authenticate = authenticationManager.authenticate(authenticationByUsername);
                return this.jwtService.generate(userByUsername.getEmail());
            } catch (BadCredentialsException ex) {
                throw new BadCredentialsException("Invalid email/username or password");
            }
        }

        throw new BadCredentialsException("Invalid email/username or password");
    }

    @GetMapping(path = "auth/me")
    public ResponseEntity<UserInfoDTO> me() {
        User userInfo = userService.getCurrentUser();
        return ResponseEntity.ok(new UserInfoDTO(userInfo));
    }

    @PutMapping(path = "auth/me")
    public ResponseEntity<Map<String, String>> updateMe(@RequestBody UserDTO userDTO) {
        User currentUser = userService.getCurrentUser();
        User updatedUser = userService.updateUser(currentUser, userDTO);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(updatedUser, currentUser.getPassword(), currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        Map<String, String> newToken = jwtService.generate(updatedUser.getEmail());

        return ResponseEntity.ok(newToken);
    }

    @DeleteMapping(path = "user")
    public ResponseEntity<String> deleteUser() {
        try {
            userService.removeUser();
            return ResponseEntity.ok("L'utilisateur a été supprimé avec succès.");
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de l'utilisateur", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue lors de la suppression de l'utilisateur.");
        }
    }


}
