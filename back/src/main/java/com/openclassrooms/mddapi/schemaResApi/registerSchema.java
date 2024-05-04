package com.openclassrooms.mddapi.schemaResApi;

import io.swagger.v3.oas.annotations.media.Schema;

public class registerSchema {
    @Schema(description = "Adresse e-mail", example = "test@test.fr")
    private String email;

    @Schema(description = "Nom", example = "Jhon Doe")
    private String name;

    @Schema(description = "Mot de passe", example = "root")
    private String password;

    public registerSchema(String token, String email, String name, String password) {

        this.email = email;
        this.name = name;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
