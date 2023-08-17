package com.game.gamelist.entity;


import java.util.List;

public interface Likeable {
    Long getId();
    List<Like> getLikes();
}
