package com.game.gamelist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This error is meant to bbe thrown when an internal error has occured and cannot fulfill the request. For example,
 * this error can be thrown when trying to fetch something from the database, but fails sometime along the way.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message) {
        super(message);
    }
}