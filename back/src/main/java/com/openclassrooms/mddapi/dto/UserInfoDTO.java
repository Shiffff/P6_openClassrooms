package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    private String email;
    private String username;
    private Date created_at;

    public UserInfoDTO(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.created_at = user.getCreated_at();
    }
}
