package com.game.gamelist.exception;

import java.time.LocalDateTime;


public record ErrorDetails(LocalDateTime timestamp, String message, String details) {
}
