package com.example.service;

import com.example.dto.profile.ProfileDTO;
import com.example.dto.profile.ProfileFilterDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileStatus;
import com.example.exceptions.*;
import com.example.repository.ProfileRepository;
import com.example.repository.custom.ProfileCustomRepository;
import com.example.util.Md5Util;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileCustomRepository profileCustomRepository;
    @Autowired
    private AttachService attachService;

    public ProfileDTO create(ProfileDTO profileDTO) {
        ProfileEntity profile = profileRepository.findByPhone(profileDTO.getPhone());
        if (profile != null) {
            throw new EmailAlreadyExistsException("Phone already exists");
        }

        ProfileEntity entity = toEntity(profileDTO);
        entity.setPrtId(SpringSecurityUtil.getCurrentUserId());

        profileRepository.save(entity);

        profileDTO.setId(entity.getId());
        return profileDTO;
    }


    public Page<ProfileDTO> getList(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProfileEntity> entities = profileRepository.findAll(pageable);

        List<ProfileEntity> content = entities.getContent();
        List<ProfileDTO> dto = new LinkedList<>();
        for (ProfileEntity profileEntity : content) {
            ProfileDTO profileDTO = toDTO(profileEntity);
            dto.add(profileDTO);
        }
        return new PageImpl<>(dto, pageable, entities.getTotalElements());
    }

    public Boolean delete(Integer id) {
        profileRepository.deleteById(id);
        return true;
    }


    public ProfileDTO updateAdmin(Integer id, ProfileDTO profileDTO) {


        get(id);
        int b = profileRepository.updateAdminById(profileDTO.getName(), profileDTO.getSurname(), profileDTO.getVisible()
                , profileDTO.getRole(), profileDTO.getStatus(), id);

        if (b == 0) {
            throw new AppForbiddenException("Something went wrong");
        }
        profileDTO.setId(id);

        return profileDTO;
    }

    public int updateUser( ProfileDTO profileDTO) {
        return profileRepository.updateUserById(profileDTO.getName(), profileDTO.getSurname(),
                Md5Util.encode(profileDTO.getPassword()), SpringSecurityUtil.getCurrentUserId());
    }

    public ProfileEntity toEntity(ProfileDTO profileDTO) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(profileDTO.getName());
        entity.setSurname(profileDTO.getSurname());
        entity.setPhone(profileDTO.getPhone());
        entity.setPassword(Md5Util.encode(profileDTO.getPassword()));
        entity.setRole(profileDTO.getRole());
        entity.setVisible(true);
        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setPhoto(attachService.get(profileDTO.getPhotoId()));
        return entity;
    }

    public ProfileDTO toDTO(ProfileEntity profileEntity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setName(profileEntity.getName());
        dto.setSurname(profileEntity.getSurname());
        dto.setRole(profileEntity.getRole());
        dto.setVisible(profileEntity.getVisible());
        return dto;
    }


    public ProfileEntity get(Integer id) {
        return profileRepository.findById(id).
                orElseThrow(() -> new ItemNotFoundException
                        ("Profile Not Found"));
    }


    public Page<ProfileFilterDTO> filter(ProfileFilterDTO filterDTO ,int page , int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<ProfileEntity> all = profileCustomRepository.filter(filterDTO, page, size);
        List<ProfileEntity> content = all.getContent();
        List<ProfileFilterDTO> dtos = new LinkedList<>();

        for (ProfileEntity entity : content) {
            ProfileFilterDTO dto = new ProfileFilterDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setSurname(entity.getSurname());
            dto.setPhone(entity.getPhone());
            dto.setEmail(entity.getEmail());
            dtos.add(dto);
        }
        return new PageImpl<>(dtos, paging,all.getTotalElements());

    }


}
