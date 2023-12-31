package com.game.gamelist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidAuthorizationException extends RuntimeException{
   public InvalidAuthorizationException(String message) {
      super(message);
   }
}
