package com.songify.domain.crud;

import com.songify.domain.crud.dto.AlbumInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class AlbumRetriever {

    private final AlbumRepository albumRepository;

    AlbumInfo findAlbumByIdWithArtistsAndSongs(final Long id) {
        return albumRepository.findAlbumByIdWithSongAndArtists(id)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id: " + id + " not found"));

//        Set<Artist> artists = album.getArtists();
//        Set<Song> songs = album.getSongs();
//
//        AlbumDto albumDto = new AlbumDto(album.getId(),album.getTitle());
//
//        Set<ArtistDto> artistDto = artists.stream()
//                .map(artist -> new ArtistDto(artist.getId(), artist.getName()))
//                .collect(Collectors.toSet());
//
//        Set<SongDto> songDto = songs.stream()
//                .map(song -> new SongDto(song.getId(), song.getName()))
//                .collect(Collectors.toSet());

//        return new AlbumDtoWithArtistsAndSongs(albumDto,artistDto,songDto);
    }
}
