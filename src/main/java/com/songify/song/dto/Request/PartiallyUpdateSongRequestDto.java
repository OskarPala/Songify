package com.songify.song.dto.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PartiallyUpdateSongRequestDto(
        String songName,
        String artist
) {
}
