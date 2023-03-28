package com.example.controller;

import com.example.dto.RegistrationDTO;
import com.example.dto.auth.AuthDTO;
import com.example.dto.auth.AuthResponseDTO;
import com.example.enums.LangEnum;
import com.example.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

//    private Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    private ResponseEntity<?> login(@Valid  @RequestBody AuthDTO dto,
                                    @RequestHeader(name = "Accept-Language", defaultValue = "RU")LangEnum lang){
        log.info("Authorization: {}"+dto);
        AuthResponseDTO response  = authService.login(dto,lang);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody RegistrationDTO dto){
        log.info("Registration: {}"+dto);
      String response  =  authService.registration(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verification/email/{jtwToken}")
    public ResponseEntity<?> emailVerification(@PathVariable("jtwToken") String jwt) {
        String response = authService.verification(jwt);
        return ResponseEntity.ok(response);
    }

  /*  @PostMapping("/verification/phone/")
    public ResponseEntity<?> emailVerification(@RequestBody SmsVerifivationDTO dto) {
        String response = authService.phoneVerification(dto);
        return ResponseEntity.ok(response);
//        return null;
    }*/

}
