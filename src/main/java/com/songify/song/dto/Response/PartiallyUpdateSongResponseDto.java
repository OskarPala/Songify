package com.songify.song.dto.Response;

import com.songify.song.controller.Song;

public record PartiallyUpdateSongResponseDto(Song updatedSong) {
}
