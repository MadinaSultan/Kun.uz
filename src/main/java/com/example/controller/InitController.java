package com.example.controller;

import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.repository.ProfileRepository;
import com.example.service.AttachService;
import com.example.service.ProfileService;
import com.example.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/init")
public class InitController {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AttachService attachService;

    @GetMapping("/admin")
    public String init() {

        if (profileRepository.findByPhone("9999") == null) {
            ProfileEntity entity = new ProfileEntity();
            entity.setName("Maruf");
            entity.setSurname("Raufov");
            entity.setPhone("0000");
            entity.setPassword(Md5Util.encode("0000"));
            entity.setRole(ProfileRole.ROLE_ADMIN);
            entity.setStatus(ProfileStatus.ACTIVE);
            entity.setCreatedDate(LocalDateTime.now());
            entity.setVisible(Boolean.TRUE);
            entity.setEmail("raufovmaruf555@gmail.com");
            entity.setPhoto(attachService.get("5908ce02-56bb-4f21-a072-b206cc6c917f"));
            profileRepository.save(entity);
            return "done";
        }
        return "Already exists";
    }

}
