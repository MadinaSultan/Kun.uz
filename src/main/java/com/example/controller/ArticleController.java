package com.example.controller;


import com.example.dto.article.ArticleCreateDTO;
import com.example.dto.article.ArticleDTO;
import com.example.enums.ArticleStatus;
import com.example.enums.LangEnum;
import com.example.mapper.IArticleShortInfoMapper;
import com.example.service.ArticleService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/moderator/create")
    public ResponseEntity<?> create(@Valid @RequestBody ArticleCreateDTO dto) {
        log.info("Article create " + dto);
        ArticleCreateDTO response = articleService.create(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/moderator/update")
    public ResponseEntity<?> update(@Valid @RequestBody ArticleDTO dto,
                                    @RequestParam("id") String id) {
        log.info("Article update " + dto);
        int response = articleService.update(dto, id);
        if (response == 1) {

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/moderator/delete")
    public ResponseEntity<?> delete(@RequestParam("id") String id) {
        log.info("Article delete " + id);
        Boolean response = articleService.delete(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/moderator/status")
    public ResponseEntity<?> status(@RequestParam("id") String id,
                                    @RequestParam("status") ArticleStatus status) {
        log.info("Article status " + id + " " + status);
        Boolean response = articleService.status(id, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/moderator/last")
    public ResponseEntity<?> lastFive(@RequestParam("status") String status) {
        List<ArticleDTO> response = articleService.lastFive(status);
        return ResponseEntity.ok(response);
    }

    //    @PreAuthorize("hasRole('ADMIN')")
//    @PermitAll
    @PostMapping("/last_eight")
    public ResponseEntity<?> lastEight(@Valid @RequestBody List<String> idList) {
        List<IArticleShortInfoMapper> response = articleService.lastEight(idList);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/moderator/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id,
                                     @RequestHeader(name = "Accept-Language", defaultValue = "RU") LangEnum language) {
        ArticleDTO response = articleService.getById(id, language);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/last_four/{id}")
    public ResponseEntity<?> getLastFour(@PathVariable String id) {
        List<IArticleShortInfoMapper> response = articleService.lastFour(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/most_reads")
    public ResponseEntity<?> mostReads() {
        List<IArticleShortInfoMapper> response = articleService.mostReads();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/get_five/region/{key}")
    public ResponseEntity<?> getFiveRegion(@PathVariable String key) {
        List<IArticleShortInfoMapper> response = articleService.getFiveRegion(key);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get_five/region/page")
    public ResponseEntity<?> getListRegionPage(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("key") String key) {
        Page<IArticleShortInfoMapper> response = articleService.getListRegionPage(page, size, key);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get_five/category/{key}")
    public ResponseEntity<?> getFiveCategory(@PathVariable String key) {
        List<IArticleShortInfoMapper> response = articleService.getFiveCategory(key);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get_five/category/page")
    public ResponseEntity<?> getListCategoryPage(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("key") String key) {
        Page<IArticleShortInfoMapper> response = articleService.getListCategoryPage(page, size, key);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view_count/{articleId}")
    public ResponseEntity<?> viewCount(@PathVariable("articleId") String articleId) {
        long response = articleService.viewCount(articleId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/share_count/{articleId}")
    public ResponseEntity<?> shareCount(@PathVariable("articleId") String articleId) {
        long response = articleService.shareCount(articleId);
        return ResponseEntity.ok(response);
    }

}
