package com.songify.domain.crud;

class SongifyCrudFacadeConfiguration {

    public static SongifyCrudFacade createSongifyCrud(final SongRepository songRepository,
                                                      final GenreRepository genreRepository,
                                                      final ArtistRepository artistRepository,
                                                      final AlbumRepository albumRepository){
        SongRetriever songRetriever = new SongRetriever(songRepository);
        SongUpdater songUpdater = new SongUpdater(songRepository);
        AlbumAdder albumAdder = new AlbumAdder(songRetriever, albumRepository);
        ArtistRetriever artistRetriever = new ArtistRetriever(artistRepository);
        AlbumRetriever albumRetriever = new AlbumRetriever(albumRepository);
        GenreDeleter genreDeleter = new GenreDeleter(genreRepository);
        SongDeleter songDeleter = new SongDeleter(songRepository);
        GenreRetriever genreRetriever = new GenreRetriever(genreRepository);
        GenreAssigner genreAssigner = new GenreAssigner(songRetriever, genreRetriever);
        SongAdder songAdder = new SongAdder(songRepository, genreAssigner);
        ArtistAdder artistAdder = new ArtistAdder(artistRepository,albumAdder,songAdder);
        GenreAdder genreAdder = new GenreAdder(genreRepository);
        AlbumDeleter albumDeleter = new AlbumDeleter(albumRepository);
        ArtistDeleter artistDeleter = new ArtistDeleter(artistRepository, artistRetriever, albumRetriever, albumDeleter, songDeleter);
        ArtistAssigner artistAssigner = new ArtistAssigner(artistRetriever, albumRetriever);
        ArtistUpdater artistUpdater = new ArtistUpdater(artistRetriever);
        return new SongifyCrudFacade(
                songRetriever,
                songUpdater,
                songDeleter,
                songAdder,
                artistAdder,
                genreAdder,
                albumAdder,
                artistRetriever,
                albumRetriever,
                artistDeleter,
                artistAssigner,
                artistUpdater
        );
    }
}
