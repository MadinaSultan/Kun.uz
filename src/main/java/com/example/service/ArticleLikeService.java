package com.example.service;
import com.example.dto.article.ArticleLikeDTO;
import com.example.entity.ArticleLikeEntity;
import com.example.enums.ArticleLIkeStatus;
import com.example.repository.ArticleLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleLikeService {
    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    public void create(ArticleLikeDTO dto) {
        ArticleLikeEntity exists = get(dto);
        if (exists != null) {
            updateStatus(dto);
            return;
        }
        ArticleLikeEntity entity = new ArticleLikeEntity();
        entity.setArticleId(dto.getArticleId());
        entity.setProfileId(dto.getProfileId());
        entity.setStatus(dto.getStatus());
        articleLikeRepository.save(entity);
    }

    public void updateStatus(ArticleLikeDTO dto) {
        ArticleLikeEntity entity1 = get(dto);

        if (entity1.getStatus().equals(dto.getStatus())) {
            delete(dto);
            return;
        } else if (entity1.getStatus().equals(ArticleLIkeStatus.LIKE)) {
            dto.setStatus(ArticleLIkeStatus.DISLIKE);
        } else if (entity1.getStatus().equals(ArticleLIkeStatus.DISLIKE)) {
            dto.setStatus(ArticleLIkeStatus.LIKE);
        }
        entity1.setStatus(dto.getStatus());
        articleLikeRepository.save(entity1);

    }

    public ArticleLikeEntity get(ArticleLikeDTO dto) {
        return articleLikeRepository.findByProfileIdAndArticleId(dto.getProfileId(),
                dto.getArticleId());
    }

    public void delete(ArticleLikeDTO dto) {
        articleLikeRepository.deleted(dto.getProfileId(), dto.getArticleId());
    }
}
