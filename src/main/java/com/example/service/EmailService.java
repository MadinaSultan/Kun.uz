package com.example.service;

import com.example.dto.EmailDTO;
import com.example.entity.EmailEntity;
import com.example.repository.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {
    @Value("${spring.mail.username}")
    private String fromAccount;

    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    void sendEmail(String toAccount, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAccount);
        message.setTo(toAccount);
        message.setSubject(subject);
        message.setText(content);

        javaMailSender.send(message);
    }

    void sendEmailMime(String toAccount, String subject, String content) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            mimeMessage.setFrom(fromAccount);

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(toAccount);
            helper.setSubject(subject);
            helper.setText(content);
            helper.setText(content, true);
            javaMailSender.send(mimeMessage);

            EmailEntity emailHistory = new EmailEntity();
            emailHistory.setEmail(toAccount);
            emailHistory.setMessage(content);
            emailHistory.setCreatedDate(LocalDateTime.now());

            emailRepository.save(emailHistory);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<EmailDTO> getHistoryByEmail(String email) {





        List<EmailEntity> emailList = emailRepository.findAllByEmail(email);

        List<EmailDTO> dtoList = new ArrayList<>();

        for (EmailEntity emailEntity : emailList) {
            EmailDTO emailDTO = toDTO(emailEntity);
            dtoList.add(emailDTO);
        }

        return dtoList;
    }

    private EmailDTO toDTO(EmailEntity emailHistory) {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setToEmail(emailHistory.getEmail());
        emailDTO.setTitle(emailHistory.getMessage());
        emailDTO.setCreatedDate(emailHistory.getCreatedDate());
        emailDTO.setId(emailHistory.getId());
        return emailDTO;
    }

        public List<EmailDTO> getHistoryByGivenDate(String date) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, timeFormatter);

            List<EmailEntity> emailHistoryList = emailRepository.getEmailByDate(localDate);

            List<EmailDTO> dtoList = new ArrayList<>();

            for (EmailEntity emailHistory : emailHistoryList) {
                EmailDTO emailDTO = toDTO(emailHistory);
                dtoList.add(emailDTO);
            }

            return dtoList;
        }

        public Page<EmailDTO> getEmailHistoryPagination(Integer page, Integer size) {
            Pageable paging = PageRequest.of(page, size);

            Page<EmailEntity> emailHistoryPage = emailRepository.findAll(paging);

            List<EmailEntity> emailHistoryList = emailHistoryPage.getContent();

            List<EmailDTO> dtoList = new ArrayList<>();

            for (EmailEntity emailHistory : emailHistoryList) {
                EmailDTO emailDTO = toDTO(emailHistory);
                dtoList.add(emailDTO);
            }

            Page<EmailDTO> emailDTOPage = new PageImpl<>(dtoList, paging, emailHistoryPage.getTotalElements());
            return emailDTOPage;
        }
    }
