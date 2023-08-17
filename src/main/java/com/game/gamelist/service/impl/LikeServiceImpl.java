//package com.game.gamelist.service.impl;
//
//import com.game.gamelist.entity.LikeEntity;
//import com.game.gamelist.entity.User;
//import com.game.gamelist.repository.LikeRepository;
//import com.game.gamelist.service.LikeService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class LikeServiceImpl implements LikeService {
//    private final LikeRepository likeRepository;
//
//    @Override
//    public LikeEntity createLike(User principle, LikeEntity like) {
//
//        return likeRepository.save(like);
//    }
//
//    @Override
//    public void deleteLike(User principle, Long likeId) {
//        likeRepository.deleteById(likeId);
//    }
//}
