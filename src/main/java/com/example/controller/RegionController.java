package com.example.controller;

import com.example.dto.RegionDTO;
import com.example.enums.LangEnum;
import com.example.service.RegionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/region")
public class RegionController {

    @Autowired
    private RegionService regionService;
    @PostMapping("/admin")
    public ResponseEntity<?> create(@Valid  @RequestBody RegionDTO dto){
                return ResponseEntity.ok(regionService.create(dto));
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getList(@RequestParam Integer page,
                                     @RequestParam Integer size) {Page<RegionDTO> response = regionService.getList(page, size);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Integer id,
                                         @Valid @RequestBody RegionDTO regionDTO
                                         ) {
        int response = regionService.updateAdmin(id, regionDTO);
        if (response == 1){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/admin")
    public ResponseEntity<?> delete(@RequestParam Integer id ){
        Boolean response = regionService.delete(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/byLang")
    public ResponseEntity<?> getByLang(@RequestParam LangEnum lang){
        List<RegionDTO> response = regionService.getByLang(lang);
        return ResponseEntity.ok(response);

    }



}
