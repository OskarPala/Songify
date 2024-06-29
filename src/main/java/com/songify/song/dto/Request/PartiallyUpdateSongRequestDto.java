package com.songify.song.dto.Request;

public record PartiallyUpdateSongRequestDto(
        String songName,
        String artist
) {
}
