package com.example.controller;

import com.example.dto.CategoryDTO;
import com.example.enums.LangEnum;
import com.example.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/admin")
    public ResponseEntity<?> create(@Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(categoryService.create(dto));
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getList(@RequestParam Integer page,
                                     @RequestParam Integer size) {
        Page<CategoryDTO> response = categoryService.getList(page, size);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Integer id,
                                         @Valid @RequestBody CategoryDTO regionDTO) {
        int response = categoryService.updateAdmin(id, regionDTO);
        if (response == 1) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/admin")
    public ResponseEntity<?> delete(@RequestParam Integer id) {
        Boolean response = categoryService.delete(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/byLang")
    public ResponseEntity<?> getByLang(@RequestParam LangEnum lang) {
        List<CategoryDTO> response = categoryService.getByLang(lang);
        return ResponseEntity.ok(response);

    }


}
