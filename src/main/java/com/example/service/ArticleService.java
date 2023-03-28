package com.example.service;

import com.example.dto.article.ArticleCreateDTO;
import com.example.dto.article.ArticleDTO;
import com.example.entity.ArticleEntity;
import com.example.entity.CategoryEntity;
import com.example.entity.RegionEntity;
import com.example.enums.ArticleStatus;
import com.example.enums.LangEnum;
import com.example.exceptions.ItemNotFoundException;
import com.example.mapper.IArticleShortInfoMapper;
import com.example.repository.ArticleRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private RegionService regionService;
    @Autowired
    private CategoryService categoryService;



    public ArticleCreateDTO create(ArticleCreateDTO dto) {

        ArticleEntity entity = toEntity(dto);
        entity.setPrtId(SpringSecurityUtil.getCurrentUserId());
        articleRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    private ArticleEntity toEntity(ArticleCreateDTO dto) {
        ArticleEntity entity = new ArticleEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setImageId((dto.getImageId()));
        entity.setRegionId(dto.getRegionId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);
        return entity;
    }

    public int update(ArticleDTO dto, String id) {
        get(id);
        return articleRepository.update(dto.getStatus(), dto.getTitle(), dto.getDescription(),
                dto.getContent(), dto.getRegionId(), dto.getCategoryId(), id);
    }

    public Boolean delete(String id) {
        get(id);
        int i = articleRepository.deleteVisible(id);
        return i == 1;
    }

    private void get(String id) {
        articleRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Not Found");
        });
    }

    private ArticleEntity getEntity(String id) {
        return  articleRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Not Found");
        });

    }

    public Boolean status(String id, ArticleStatus status) {
        get(id);
        int i = articleRepository.status(id, status);
        return i == 1;
    }

    public List<ArticleDTO> lastFive(String status) {
        List<ArticleEntity> lastFive = articleRepository.lastFive(status);
        List<ArticleDTO> dto = new ArrayList<>();
        for (ArticleEntity entity : lastFive) {
            ArticleDTO articleDTO = toDTO(entity);
            dto.add(articleDTO);

        }
        return dto;
    }

    private ArticleDTO toDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setPublishedDate(entity.getPublishedDate());

        return dto;
    }

    public List<IArticleShortInfoMapper> lastEight(List<String> idList) {
        return articleRepository
                .getTop8Article(ArticleStatus.PUBLISHED.name(), idList);
    }

    public ArticleDTO getById(String id, LangEnum language) {
        Optional<ArticleEntity> optional = articleRepository.findByIdAndStatusAndVisibleTrue(id, ArticleStatus.PUBLISHED);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Article not Found");
        }
        ArticleEntity entity = optional.get();
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setViewCount(entity.getViewCount());
        dto.setSharedCount(entity.getSharedCount());
        dto.setRegion(regionService.getById(entity.getRegionId(), language));
        dto.setCategory(categoryService.getById(entity.getCategoryId(), language));

        return dto;
    }

    public List<IArticleShortInfoMapper> lastFour(String id) {
        return articleRepository.getLastFour(id);
    }

    public List<IArticleShortInfoMapper> mostReads() {
        return articleRepository.mostReads();
    }

    public List<IArticleShortInfoMapper> getFiveRegion(String key) {
        RegionEntity region = regionService.byKey(key);
        return articleRepository.getByRegionId(region.getId());
    }

    public Page<IArticleShortInfoMapper> getListRegionPage(Integer page, Integer size, String key) {
        RegionEntity regionEntity = regionService.byKey(key);
        Pageable paging = PageRequest.of(page, size);
        Page<IArticleShortInfoMapper> page1 = articleRepository.findAllByKey(paging, regionEntity.getId());
        List<IArticleShortInfoMapper> list = page1.getContent();
        return new PageImpl<>(list, paging, page1.getTotalElements());
    }

    public List<IArticleShortInfoMapper> getFiveCategory(String key) {
        CategoryEntity category = categoryService.getByKey(key);
       return articleRepository.getByCategoryId(category.getId());
    }

    public ArticleDTO getArticleShortInfo(String id){
        ArticleDTO dto= new ArticleDTO();
        ArticleEntity entity = getEntity(id);
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setImageId(entity.getImageId());
        return dto;
    }

    public Page<IArticleShortInfoMapper> getListCategoryPage(Integer page, Integer size, String key) {
        CategoryEntity categoryEntity = categoryService.byKey(key);
        Pageable paging = PageRequest.of(page, size);
        Page<IArticleShortInfoMapper> page1 = articleRepository.findAllByKeyCategory(paging, categoryEntity.getId());
        List<IArticleShortInfoMapper> list = page1.getContent();
        return new PageImpl<>(list, paging, page1.getTotalElements());
    }

    public long viewCount(String id) {
         articleRepository.viewCount(id);
        return articleRepository.viewCountReturn(id);
    }

    public long shareCount(String articleId) {
        articleRepository.shareCount(articleId);
        return articleRepository.shareCountReturn(articleId);
    }
}
