package com.songify.domain.crud;

class ArtisNotFoundException extends RuntimeException {
    ArtisNotFoundException(final String message) {
        super("artist with id: " + message + " not found");
    }
}
