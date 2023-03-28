package com.example.controller;

import com.example.dto.article.ArticleTypeDTO;
import com.example.enums.LangEnum;
import com.example.enums.ProfileRole;
import com.example.service.ArticleTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articleType")
public class ArticleTypeController {

    @Autowired
    private ArticleTypeService articleTypeService;

    @PostMapping("/admin")
    public ResponseEntity<?> create(@Valid  @RequestBody ArticleTypeDTO dto) {
        return ResponseEntity.ok(articleTypeService.create(dto));
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getList(@RequestParam Integer page,
                                     @RequestParam Integer size) {
        Page<ArticleTypeDTO> response = articleTypeService.getList(page, size);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Integer id,
                                         @Valid @RequestBody ArticleTypeDTO articleTypeDTO) {
        int response = articleTypeService.updateAdmin(id, articleTypeDTO);
        if (response == 1) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/admin")
    public ResponseEntity<?> delete(@RequestParam Integer id) {
        Boolean response = articleTypeService.delete(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/byLang")
    public ResponseEntity<?> getByLang(@RequestParam LangEnum lang) {
        List<ArticleTypeDTO> response = articleTypeService.getByLang(lang);
        return ResponseEntity.ok(response);

    }


}
