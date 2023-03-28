package com.example.controller;

import com.example.dto.EmailDTO;
import com.example.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("")
    public ResponseEntity<?> getHistoryByEmail(@RequestParam String email) {
        List<EmailDTO> response = emailService.getHistoryByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date")
    public ResponseEntity<?> getHistoryByGivenDate(@RequestParam String date) {
        List<EmailDTO> response = emailService.getHistoryByGivenDate(date);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/pagination")
    public ResponseEntity<?> getEmailHistoryPagination(
            @RequestParam Integer page,
            @RequestParam Integer size) {
        Page<EmailDTO> response = emailService.getEmailHistoryPagination(page, size);
        return ResponseEntity.ok(response);
    }
}
