package com.songify.song.controller;

import com.songify.song.dto.DeleteSongResponseDto;
import com.songify.song.dto.SingleSongResponseDto;
import com.songify.song.dto.SongRequestDto;
import com.songify.song.dto.SongResponseDto;
import com.songify.song.error.SongNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Log4j2
public class SongRestController {

    Map<Integer, String> database = new HashMap<>(Map.of(
            1, "shawn mendes song1",
            2, "ariana grande song2",
            3, "ariana grande song3",
            4, "ariana grande song4"
    ));

    @GetMapping("/songs")
    public ResponseEntity<SongResponseDto> getAllSongs(@RequestParam(required = false) Integer limit) {
        if (limit != null) {
            Map<Integer, String> limitedMap = database.entrySet()
                    .stream()
                    .limit(limit)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            SongResponseDto response = new SongResponseDto(limitedMap);
            return ResponseEntity.ok(response);
        }
        SongResponseDto response = new SongResponseDto(database);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<SingleSongResponseDto> getSongById(
            @PathVariable Integer id,
            @RequestHeader(required = false) String requestId
    ) {
        log.info(requestId);
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
        String song = database.get(id);
        SingleSongResponseDto response = new SingleSongResponseDto(song);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/songs")
    public ResponseEntity<SingleSongResponseDto> postSong(@RequestBody @Valid SongRequestDto request) {
        String songName = request.songName();
        String logMessage = "adding new song: " + songName;
        log.info(logMessage);
        database.put(database.size() + 1, songName);
        return ResponseEntity.ok(new SingleSongResponseDto(songName));
    }

    @DeleteMapping("/songs/{id}")
    public ResponseEntity<DeleteSongResponseDto> deleteSongById(@PathVariable Integer id) {
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
        database.remove(id);
        return ResponseEntity.ok(new DeleteSongResponseDto("You deleted song with id: " + id, HttpStatus.OK));
    }
}
