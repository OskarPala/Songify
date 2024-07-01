package com.songify.song.infrastructure.controller.dto.Request;

public record PartiallyUpdateSongRequestDto(
        String songName,
        String artist
) {
}
