package com.example.service;
import com.example.dto.CategoryDTO;
import com.example.entity.CategoryEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.RegionEntity;
import com.example.enums.LangEnum;
import com.example.exceptions.ArticleTypeException;
import com.example.exceptions.ItemNotFoundException;
import com.example.exceptions.RegionAlreadyExsistException;
import com.example.repository.CategoryRepository;
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
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;


    public CategoryDTO create(CategoryDTO dto) {
        CategoryEntity byKey = categoryRepository.findByKey(dto.getKey());
        if (byKey != null) {
            throw new RegionAlreadyExsistException("Already exists key");
        }
        CategoryEntity entity = toEntity(dto);
        entity.setPrtId(SpringSecurityUtil.getCurrentUserId());
        categoryRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }
    private CategoryEntity toEntity(CategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(dto.getId());
        entity.setKey(dto.getKey());
        entity.setNameEn(dto.getNameEng());
        entity.setNameRu(dto.getNameRu());
        entity.setNameUz(dto.getNameUz());
        entity.setVisible(Boolean.TRUE);
        entity.setCreatedDate(LocalDateTime.now());
        return entity;
    }


    public Page<CategoryDTO> getList(Integer page, Integer size) {
        Pageable paging = PageRequest.of(page, size);
        Page<CategoryEntity> all = categoryRepository.findAll(paging);
        List<CategoryEntity> entities = all.getContent();
        List<CategoryDTO> dtoList = new ArrayList<>();
        for (CategoryEntity entity : entities) {
            CategoryDTO dto1 = new CategoryDTO();
            dto1.setId(entity.getId());
            dto1.setKey(entity.getKey());
            dto1.setNameEng(entity.getNameEn());
            dto1.setNameRu(entity.getNameRu());
            dto1.setNameUz(entity.getNameUz());
            dto1.setCreatedDate(entity.getCreatedDate());
            dto1.setVisible(entity.getVisible());

            dtoList.add(dto1);
        }
        return new PageImpl<>(dtoList, paging, all.getTotalElements());
    }

    public int updateAdmin(Integer id, CategoryDTO dto) {
        return categoryRepository.update(dto.getKey(),
                dto.getNameEng()
                , dto.getNameRu(), dto.getNameUz() ,dto.getVisible(), id);
    }
    public Boolean delete(Integer id) {
        categoryRepository.deleteById(id);
        return true;
    }


    public List<CategoryDTO> getByLang( LangEnum lang) {
        List<CategoryEntity> entityList = categoryRepository.findAllByVisibleTrue();
        List<CategoryDTO> dtoList = new ArrayList<>();
        for (CategoryEntity entity : entityList) {
            CategoryDTO dto = new CategoryDTO();
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
    public CategoryEntity get(Integer id){
        return categoryRepository.findById(id).
                orElseThrow(() -> new ItemNotFoundException
                        ("Not Found"));
    }


    public CategoryDTO getById(Integer categoryId, LangEnum language) {
        CategoryEntity entity = get(categoryId);
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setKey(entity.getKey());
        switch (language){
            case UZ -> dto.setName(entity.getNameUz());
            case EN -> dto.setName(entity.getNameEn());
            case RU -> dto.setName(entity.getNameRu());
        }
        return dto;
    }

    public CategoryEntity getByKey(String key) {
      return categoryRepository.findAllByKey(key);
    }

    public CategoryEntity byKey(String key) {
        return categoryRepository.findByKey(key);
    }

}
