package com.example.controller;

import com.example.dto.article.ArticleLikeDTO;
import com.example.service.ArticleLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article_like")
public class ArticleLikeController {
    @Autowired private ArticleLikeService articleLikeService;
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ArticleLikeDTO dto){
        articleLikeService.create(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody ArticleLikeDTO dto){
        articleLikeService.updateStatus(dto);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody ArticleLikeDTO dto){
         articleLikeService.delete(dto);
        return ResponseEntity.ok().build();
    }
}
