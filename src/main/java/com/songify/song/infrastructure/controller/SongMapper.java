package com.songify.song.infrastructure.controller;

import com.songify.song.infrastructure.controller.dto.Request.CreateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.Request.PartiallyUpdateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.Request.UpdateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.Response.*;
import com.songify.song.domain.model.Song;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class SongMapper {

    public static Song mapFromCreateSongRequestDtoToSong(CreateSongRequestDto dto) {
        return new Song(dto.songName(), dto.artist());
    }

    public static Song mapFromUpdateSongRequestDtoToSong(UpdateSongRequestDto dto) {
        return new Song(dto.songName(), dto.artist());
    }

    public static Song mapFromPartiallyUpdateSongRequestDtoToSong(PartiallyUpdateSongRequestDto dto) {
        return new Song(dto.songName(), dto.artist());
    }

    public static CreateSongResponseDto mapFromSongToCreateSongResponseDto(Song song) {
        return new CreateSongResponseDto(song);
    }

    public static DeleteSongResponseDto mapFromSongToDeleteSongResponseDto(Integer id) {
        return new DeleteSongResponseDto("You deleted song with id: " + id, HttpStatus.OK);
    }

    public static UpdateSongResponseDto mapFromSongToUpdateSongResponseDto(Song newSong) {
        return new UpdateSongResponseDto(newSong.name(), newSong.artist());
    }

    public static PartiallyUpdateSongResponseDto mapFromSongToPartiallyUpdateSongResponseDto(Song updatedSong) {
        return new PartiallyUpdateSongResponseDto(updatedSong);
    }

    public static GetSongResponseDto mapFromSongToGetSongResponseDto(Song song) {
        return new GetSongResponseDto(song);
    }

    public static GetAllSongsResponseDto mapFromSongToGetAllSongsResponseDto(Map<Integer, Song> database) {
        return new GetAllSongsResponseDto(database);
    }
}
