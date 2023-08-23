package com.game.gamelist.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder
@Setter
@Getter
public class MockPostView implements PostView {

    private Long id;
    private UserBasicView user;
    private String text;
    private List<LikeEntityView> likes;
    private LocalDateTime createdAt;

    // Constructor
    public MockPostView(Long id, UserBasicView user, String text, List<LikeEntityView> likes) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.likes = likes;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public UserBasicView getUser() {
        return user;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public List<LikeEntityView> getLikes() {
        return likes;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}