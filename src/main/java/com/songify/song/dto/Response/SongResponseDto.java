package com.songify.song.dto.Response;

import com.songify.song.controller.Song;

import java.util.Map;

public record SongResponseDto(Map<Integer, Song> songs) {
}
