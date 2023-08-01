package com.game.gamelist.service;

import com.game.gamelist.entity.User;

import java.util.List;

public interface AdminService {
    List<User> getAllUsers();
    User getUser(int id);
}
