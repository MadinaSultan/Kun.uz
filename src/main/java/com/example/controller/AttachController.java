package com.example.controller;

import com.example.dto.attach.AttachDTO;
import com.example.service.AttachService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attach")
public class AttachController {
    @Autowired
    private AttachService attachService;

   /* @PostMapping("/upload")
    public ResponseEntity<String> uploadOld(@RequestParam("file") MultipartFile file) {
        String fileName = attachService.saveToSystem(file);
        return ResponseEntity.ok().body(fileName);
    }*/
   @PostMapping("/upload")
   public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
       AttachDTO dto = attachService.saveToSystem(file);
       return ResponseEntity.ok().body(dto);
   }


    @GetMapping(value = "/open/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] open(@PathVariable("fileName") String fileName) {
        if (fileName != null && fileName.length() > 0) {
            try {
                return this.attachService.loadImage(fileName);
            } catch (Exception e) {
                e.printStackTrace();
                return new byte[0];
            }
        }
        return null;
    }

    @GetMapping(value = "/open_general/{fileName}",produces = MediaType.ALL_VALUE)
    public byte[] open_general(@PathVariable("fileName") String fileName){
       return attachService.open_general(fileName);
    }

    @GetMapping("/download/{fineName}")
    public ResponseEntity<Resource> downloadOld(@PathVariable("fineName") String fileName) {
        ResponseEntity<Resource> download = attachService.download(fileName);
        return download;
    }



    @GetMapping("/pagination")
    public ResponseEntity<?> getPaginationUrl(HttpServletRequest request,
                                                       @RequestParam Integer page,
                                                       @RequestParam Integer size){
        Page<AttachDTO> response = attachService.getPaginationUrl(page, size);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(HttpServletRequest request, @RequestParam("id") String id){
       Boolean response = attachService.delete(id);
       return ResponseEntity.ok(response);
    }
   /* @GetMapping("/download/{fineName}")
    public ResponseEntity<Resource> downloadOld(@PathVariable("fineName") String fileName) {
        Resource file = attachService.download(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }*/
}
