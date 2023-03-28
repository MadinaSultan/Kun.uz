package com.example.service;

import com.example.dto.comment.CommentDTO;
import com.example.entity.CommentEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.exceptions.AppForbiddenException;
import com.example.exceptions.ItemNotFoundException;
import com.example.mapper.CommentMapperShort;
import com.example.repository.CommentRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired private ProfileService profileService;
    @Autowired
    private CommentRepository commentRepository;
    public CommentDTO create(CommentDTO dto ) {
        CommentEntity entity = new CommentEntity();
        entity.setContent(dto.getContent());
        entity.setReplyId(dto.getReplyId());
        entity.setArticleId(dto.getArticleId());
        entity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        commentRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public CommentDTO update(String articleId, CommentDTO dto) {
        int b = commentRepository.update(dto.getContent(),articleId);
        if (b == 0) {
            throw new AppForbiddenException("Something went wrong");
        }
        return dto;
    }

    public String delete(Integer commentId) {
        CommentEntity commentEntity = get(commentId);

        ProfileEntity profile = profileService.get(SpringSecurityUtil.getCurrentUserId());

        if(profile.getRole().equals(ProfileRole.ROLE_ADMIN)){
            commentRepository.deleteAdmin(commentId);
        }else if(commentEntity.getProfileId().equals(SpringSecurityUtil.getCurrentUserId())){
            commentRepository.deleteOwn(commentId);
        }else{
            throw new IllegalArgumentException("Comment it is not yours");
        }
        return "Deleted";
    }
    private CommentEntity get(Integer commentId) {
        return commentRepository.findById(commentId).orElseThrow(()->{
            throw new ItemNotFoundException("Comment Not Found");
        });
    }


    public Page<CommentMapperShort> getPageList(int page, int size) {
        Pageable paging = PageRequest.of(page,size);
        Page<CommentMapperShort> all = commentRepository.getPageList(paging);
        List<CommentMapperShort> list = all.getContent();
        return new PageImpl<>(list,paging,all.getTotalElements());
    }

    public List<CommentMapperShort> repliedList(Integer commentId) {
        return commentRepository.repliedList(commentId);
    }
}
