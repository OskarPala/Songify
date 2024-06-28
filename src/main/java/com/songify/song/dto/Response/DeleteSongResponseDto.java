package com.songify.song.dto.Response;

import org.springframework.http.HttpStatus;

public record DeleteSongResponseDto(String message, HttpStatus status)  {
}
