package com.openclassrooms.mddapi.schemaResApi;

import io.swagger.v3.oas.annotations.media.Schema;

public class loginSchema {

    @Schema(description = "Sucess", example = "Bearer Token")
    private String token;

    public loginSchema(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }


}