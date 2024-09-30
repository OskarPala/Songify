package com.songify.domain.crud;

class GenreNotFoundException extends RuntimeException {
    GenreNotFoundException(final String message) {
        super(message);
    }
}
