package com.songify.song.infrastructure.controller.dto.Response;

import org.springframework.http.HttpStatus;

public record DeleteSongResponseDto(String message, HttpStatus status)  {
}
