package com.example.service;

import com.example.config.security.JwtTokenFilter;
import com.example.dto.auth.AuthDTO;
import com.example.dto.auth.AuthResponseDTO;
import com.example.dto.RegistrationDTO;
import com.example.entity.EmailEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.LangEnum;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exceptions.*;
import com.example.repository.EmailRepository;
import com.example.repository.ProfileRepository;
import com.example.util.JwtTokenUtil;
import com.example.util.Md5Util;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    @Autowired
    private EmailService emailService;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private ProfileService profileService;
    @Autowired
    private ResourceBundleService resourceService;

    public AuthResponseDTO login(AuthDTO dto, LangEnum lang) {

        ProfileEntity profile = profileRepository.
                findByPhoneAndPassword(dto.getPhone(),
                        Md5Util.encode(dto.getPassword()));

        if (profile == null) {
            throw new PasswordOrEmailWrongException(resourceService.getMessage("credential.wrong",lang.name()));
        }
        if (!profile.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new AppForbiddenException("No Access");
        }
        return toDTO(profile);
    }

    private AuthResponseDTO toDTO(ProfileEntity profile) {
        AuthResponseDTO dto = new AuthResponseDTO();
        //  if (!profile.getRole().equals(ProfileRole.USER)) { TODO token kere ekan
        dto.setName(profile.getName());
        dto.setSurname(profile.getSurname());
        dto.setRole(profile.getRole());
        dto.setToken(JwtTokenUtil.encode(profile.getPhone(), profile.getRole()));
        //  } else {
        dto.setName(profile.getName());
        dto.setSurname(profile.getSurname());
        dto.setRole(profile.getRole());
        //  }
        return dto;
    }

    public String registration(RegistrationDTO dto) {
        ProfileEntity exists = profileRepository.findByEmail(dto.getEmail());
        if (exists != null) {
            if (exists.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
                profileRepository.delete(exists);
            } else {
                throw new EmailAlreadyExistsException("Email already exists");
            }
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhone(dto.getPhone());
        entity.setPassword(Md5Util.encode(dto.getPassword()));
        entity.setRole(ProfileRole.ROLE_USER);
        entity.setStatus(ProfileStatus.NOT_ACTIVE);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        entity.setEmail(dto.getEmail());
//        entity.setSmsCode("12345");
        profileRepository.save(entity);
        Thread thread = new Thread(){
            @Override
            public void run() {
                sendVerificationEmail(entity);    
            }
        };
        thread.start();
/*        StringBuilder sb = new StringBuilder();
        sb.append("Salom Qalaysan \n");
        sb.append(" Bu test message");
        sb.append("Click there:  http://localhost:8081/auth/verification/email/").append(JwtUtil.encode(entity.getId()));
        emailService.sendEmail(dto.getEmail(), "Complete Registration", sb.toString());

       Runnable runnable = new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                sb.append("<h1 style=\"text-align: center\">Complete Registration</h1>");
                String link = String.format("<a href=\"http://localhost:8081/auth/verification/email/%s\"> Click there</a>", JwtUtil.encode(entity.getId()));
                sb.append(link);
                emailService.sendEmailMine(dto.getEmail(), "Complete Registration", sb.toString());
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();


        smsService.sendSms(entity.getPhone(),"12345");*/
        return "Email ga link ketdi. Mazgi emailni tekshir.";
    }
    private void sendVerificationEmail(ProfileEntity entity) {
        StringBuilder builder = new StringBuilder();
        String encode = JwtTokenUtil.encode(entity.getPhone(),entity.getRole());
        builder.append("<h1 style=\"text-align: center\">Complete Registration</h1>");
        String link = String.format("<a href=\"http://localhost:8081/auth/verification/email/%s\"> Click there</a>",encode );
        builder.append(link);
        String title = "Activate Your Registration";
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setEmail(entity.getEmail());
        emailEntity.setMessage(encode);
        emailEntity.setCreatedDate(LocalDateTime.now());
        emailRepository.save(emailEntity);
        emailService.sendEmailMime(entity.getEmail(), title, builder.toString());
    }
    public String verification(String jwt) {
        Integer id;
        try {
            id = JwtTokenUtil.decodeForEmailVerification(jwt);
        } catch (JwtException e) {
            return "Verification failed";
        }

        ProfileEntity exists = profileService.get(id);
        if (!exists.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
            return "Verification failed";
        }
        exists.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(exists);

        return "Verification success";
    }



}
