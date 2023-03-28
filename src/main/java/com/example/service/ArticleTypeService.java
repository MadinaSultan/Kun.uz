package com.example.service;
import com.example.dto.article.ArticleTypeDTO;
import com.example.entity.ArticleTypeEntity;
import com.example.enums.LangEnum;
import com.example.exceptions.ArticleTypeAlreadyExitsException;
import com.example.exceptions.ItemNotFoundException;
import com.example.repository.ArticleTypeRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleTypeService {
    @Autowired
    private ArticleTypeRepository articleTypeRepository;


    public ArticleTypeDTO create(ArticleTypeDTO dto) {
        ArticleTypeEntity byKey = articleTypeRepository.findByKey(dto.getKey());
        if (byKey != null) {
            throw new ArticleTypeAlreadyExitsException("Already exists key");
        }
        ArticleTypeEntity entity = toEntity(dto);
        entity.setPrtId(SpringSecurityUtil.getCurrentUserId());
        articleTypeRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }



    private ArticleTypeEntity toEntity(ArticleTypeDTO dto) {
        ArticleTypeEntity entity = new ArticleTypeEntity();
        entity.setId(dto.getId());
        entity.setKey(dto.getKey());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        entity.setNameUz(dto.getNameUz());
        entity.setCreatedDate(LocalDateTime.now());
        return entity;
    }


    public Page<ArticleTypeDTO> getList(Integer page, Integer size) {
        Pageable paging = PageRequest.of(page, size);
        Page<ArticleTypeEntity> all = articleTypeRepository.findAll(paging);
        List<ArticleTypeEntity> entities = all.getContent();
        List<ArticleTypeDTO> dtoList = new ArrayList<>();
        for (ArticleTypeEntity entity : entities) {
            ArticleTypeDTO dto1 = new ArticleTypeDTO();
            dto1.setId(entity.getId());
            dto1.setKey(entity.getKey());
            dto1.setNameEn(entity.getNameEn());
            dto1.setNameRu(entity.getNameRu());
            dto1.setNameUz(entity.getNameUz());
            dto1.setCreatedDate(entity.getCreatedDate());

            dtoList.add(dto1);
        }
        return new PageImpl<>(dtoList, paging, all.getTotalElements());
    }

    public int updateAdmin(Integer id, ArticleTypeDTO dto) {
        get(id);
        return articleTypeRepository.update(dto.getKey(),
                dto.getNameEn()
                , dto.getNameRu(), dto.getNameUz(), id);
    }

    public Boolean delete(Integer id) {
        get(id);
        articleTypeRepository.deleteById(id);
        return true;
    }


    public List<ArticleTypeDTO> getByLang(LangEnum lang) {
              List<ArticleTypeEntity> entityList = articleTypeRepository.findAll();
        List<ArticleTypeDTO> dtoList = new ArrayList<>();
        for (ArticleTypeEntity entity : entityList)
        {
            ArticleTypeDTO dto = new ArticleTypeDTO();
            dto.setKey(entity.getKey());
            dto.setId(entity.getId());
            switch (lang) {
                case UZ -> dto.setName(entity.getNameUz());
                case RU -> dto.setName(entity.getNameRu());
                case EN -> dto.setName(entity.getNameEn());
            }
            dtoList.add(dto);
        }
        return dtoList;
    }
    public ArticleTypeEntity get(Integer id){
        return articleTypeRepository.findById(id).
                orElseThrow(() -> new ItemNotFoundException
                        ("Not Found"));
    }



}
