package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.entity.User;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String email;
    private Long id;
    private String username;
    private String password;
    private Date created_at;
    private LocalDateTime updated_at;

    public UserDTO(User user) {
        this.email = user.getEmail();
        this.id = user.getId();
        this.username = user.getUsername();
        this.created_at = user.getCreated_at();
        this.updated_at = user.getUpdated_at();
    }
}
