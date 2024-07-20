package com.songify.song.infrastructure.controller.dto.Response;

import java.util.List;

public record GetAllSongsResponseDto(List<SongDto> songs) {
}
