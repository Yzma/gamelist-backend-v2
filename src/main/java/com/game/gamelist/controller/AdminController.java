package com.game.gamelist.controller;

import com.game.gamelist.entity.User;
import com.game.gamelist.service.impl.AdminServiceImpl;
import com.game.gamelist.utils.JacksonValueUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminServiceImpl adminService;
    
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<MappingJacksonValue> getUser(@PathVariable int id) {
        User user = adminService.getUser(id);
        return new ResponseEntity<>(JacksonValueUtil.getMappingJacksonValue(user), HttpStatus.OK);
    }
}
