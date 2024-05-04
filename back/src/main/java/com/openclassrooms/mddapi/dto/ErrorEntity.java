package com.openclassrooms.mddapi.dto;

public record ErrorEntity(
        int code,
        String message
) {
}
