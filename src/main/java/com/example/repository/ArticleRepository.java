package com.example.repository;

import com.example.dto.article.ArticleShortInfoDTO;
import com.example.entity.ArticleEntity;
import com.example.entity.AttachEntity;
import com.example.enums.ArticleStatus;
import com.example.mapper.ArticleShortInfoMapper;
import com.example.mapper.IArticleShortInfoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface ArticleRepository  extends JpaRepository<ArticleEntity , String> {

@Transactional
@Modifying
@Query("update ArticleEntity set status = ?1, title = ?2,description = ?3," +
        "content = ?4 ," +
        "region.id = ?5 , category.id = ?6 where id = ?7")
    int update(ArticleStatus status,
               String title,
               String description,
               String content,
               Integer regionId,
               Integer categoryId,
               String id);



    @Transactional
    @Modifying
    @Query("update ArticleEntity  set visible = false where id =?1")
    int deleteVisible(String id);



    @Transactional
    @Modifying
    @Query( "update ArticleEntity  set status = ?2 ,publishedDate = LOCAL DATETIME where id =?1")
    int status(String id,ArticleStatus status);



    @Query(value = "select * from article where status = ?1 order by created_date desc limit 5",nativeQuery = true)
    List<ArticleEntity> lastFive(String status);




    @Query(value = " select a.id as id, a.title as title, a.description as description,a.image_id as imageId," +
            " a.published_date as publishedDate from article a " +
            "where a.status=?1 and  a.id not in(?2) order by a.created_date desc  limit 8", nativeQuery = true)
    List<IArticleShortInfoMapper> getTop8Article(String status, List<String> idList);

 /*   @Query(" select  new com.example.mapper.ArticleShortInfoMapper(a.id, a.title,a.description,a.image.id,a.publishedDate) " +
            " from  ArticleEntity a where a.status=?1 and  a.id not in(?2) order by a.createdDate desc ")
    Page<ArticleShortInfoMapper> getTop8(ArticleStatus status, List<String> idList, Pageable pageable);
*/
    List<ArticleEntity> getTop8ByStatusAndIdNotInOrderByCreatedDate(ArticleStatus status, List<String> idList);

    Optional<ArticleEntity> findByIdAndStatusAndVisibleTrue(String id,ArticleStatus status);


    @Query(value = " select a.id as id, a.title as title, a.description as description,a.image_id as imageId," +
            " a.published_date as publishedDate from article a " +
            "where a.status='PUBLISHED' and  a.id not in(?1) order by a.created_date desc  limit 4", nativeQuery = true)
    List<IArticleShortInfoMapper> getLastFour(String id);



    @Query(value = " select a.id as id, a.title as title, a.description as description,a.image_id as imageId," +
            " a.published_date as publishedDate from article a " +
            "where a.status='PUBLISHED'  order by view_count desc  limit 4", nativeQuery = true)
    List<IArticleShortInfoMapper> mostReads();


    @Query(value = " select a.id as id, a.title as title, a.description as description,a.image_id as imageId," +
            " a.published_date as publishedDate from article a " +
            "where a.status='PUBLISHED'  and  a.region_id = ?1 order by created_date desc  limit 5", nativeQuery = true)
    List<IArticleShortInfoMapper> getByRegionId(Integer id);


    @Query(value = " select a.id as id, a.title as title, a.description as description,a.image_id as imageId," +
            " a.published_date as publishedDate from article a " +
            "where a.status='PUBLISHED'  and  a.region_id = ?1 order by created_date desc ", nativeQuery = true)
    Page<IArticleShortInfoMapper> findAllByKey(Pageable paging, Integer id);




    @Query(value = " select a.id as id, a.title as title, a.description as description,a.image_id as imageId," +
            " a.published_date as publishedDate from article a " +
            "where a.status='PUBLISHED'  and  a.category_id = ?1 order by created_date desc limit 5", nativeQuery = true)
    List<IArticleShortInfoMapper> getByCategoryId(Integer id);


    @Query(value = " select a.id as id, a.title as title, a.description as description,a.image_id as imageId," +
            " a.published_date as publishedDate from article a " +
            "where a.status='PUBLISHED'  and  a.category_id = ?1 order by created_date desc ", nativeQuery = true)
    Page<IArticleShortInfoMapper> findAllByKeyCategory(Pageable paging, Integer id);


    @Transactional
    @Modifying
    @Query("update  ArticleEntity a  set a.viewCount = a.viewCount + 1 where a.id = ?1")
    void viewCount(String key);


    @Query("select a.viewCount from  ArticleEntity a where a.id = ?1")
    int viewCountReturn(String id);


    @Transactional
    @Modifying
    @Query("update  ArticleEntity a  set a.sharedCount = a.sharedCount + 1 where a.id = ?1")
    void shareCount(String key);


    @Query("select a.sharedCount from  ArticleEntity a where a.id = ?1")
    int shareCountReturn(String id);
}
