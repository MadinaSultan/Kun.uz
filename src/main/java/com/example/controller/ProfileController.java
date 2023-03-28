package com.example.controller;

import com.example.dto.profile.ProfileDTO;
import com.example.dto.profile.ProfileFilterDTO;
import com.example.service.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@Tag(name = "ProfileController ",description = "Api list for ProfileEntity")
public class ProfileController {

    @Autowired
    private ProfileService profileService;
//    @PreAuthorize("hasRole('ADMIN')")


   /* @Operation(summary = "Authorization method", description = "Method used for profile authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article Created",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid info supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "item not found",
                    content = @Content)})*/
   @PostMapping("/admin/create")
    public ResponseEntity<ProfileDTO> save( @Valid  @RequestBody ProfileDTO profileDTO) {
        ProfileDTO profile = profileService.create(profileDTO);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/admin/list")
    public ResponseEntity<Page<ProfileDTO>> getList(@RequestParam Integer page,
                                     @RequestParam Integer size) {
        Page<ProfileDTO> response = profileService.getList(page, size);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id, HttpServletRequest request) {
        Boolean result = profileService.delete(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Integer id,
                                         @Valid @RequestBody ProfileDTO profileDTO) {
        ProfileDTO response = profileService.updateAdmin(id, profileDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin")
    public ResponseEntity<?> updateUser(@Valid @RequestBody ProfileDTO profileDTO) {
        int result = profileService.updateUser(profileDTO);

        if (result == 1) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
//        @PreAuthorize("hasRole('USER')")

    @PostMapping("/filter")
    public ResponseEntity<?> filter(@RequestBody ProfileFilterDTO profileFilterDTO ,
                                    @RequestParam("page") int page
            ,@RequestParam("size") int size){
        Page<ProfileFilterDTO> response = profileService.filter(profileFilterDTO, page, size);
        return ResponseEntity.ok(response);
    }

}
