package io.repository;
import io.domain.Item;

import io.domain.enumeration.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Item entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select item from Item item where item.owner.login = ?#{principal.username}")
    List<Item> findByOwnerIsCurrentUser();

    @Query(value = "select item from Item item join fetch item.owner",
        countQuery = "select count(item) from Item item join item.owner")
    Page<Item> findAll(Pageable pageable);

    @Query(value = "select item from Item item where lower(item.title) like %:search% and (item.category =:category1 or item.category =:category2 or item.category =:category3)",
        countQuery = "select count(item) from Item item  where lower(item.title) like %:search% and (item.category =:category1 or item.category =:category2 or item.category =:category3)")
    Page<Item> findAllForSearch(Pageable pageable, @Param("search") String search, @Param("category1") Category category1, @Param("category2") Category category2, @Param("category3") Category category3);

    @Query(value = "select item from Item item where lower(item.hash)=:search and (item.category =:category1 or item.category =:category2 or item.category =:category3)",
        countQuery = "select count(item) from Item item  where lower(item.hash)=:search and (item.category =:category1 or item.category =:category2 or item.category =:category3)")
    Page<Item> findAllForHashtagSearch(Pageable pageable, @Param("search") String search, @Param("category1") Category category1, @Param("category2") Category category2, @Param("category3") Category category3);

    @Query(value = "select item from Item item join item.interesteds ii where " +
        "ii.id = :userId and lower(item.title) like %:search% and (item.category =:category1 or item.category =:category2 or item.category =:category3)",
        countQuery = "select item from Item item join item.interesteds ii where " +
            "ii.id = :userId and  lower(item.title) like %:search% and (item.category =:category1 or item.category =:category2 or item.category =:category3)")
    Page<Item> findAllLiked(Pageable pageable, @Param("search") String search, @Param("category1") Category category1, @Param("category2") Category category2,
                            @Param("category3") Category category3, @Param("userId") long userId);

    @Query(value = "select item from Item item join item.interesteds ii where " +
        "ii.id = :userId and lower(item.hash)=:search and (item.category =:category1 or item.category =:category2 or item.category =:category3)",
        countQuery = "select count(item) from Item item join item.interesteds ii where " +
            "ii.id = :userId and lower(item.hash)=:search and (item.category =:category1 or item.category =:category2 or item.category =:category3)")
    Page<Item> findAllLikedHashtag(Pageable pageable, @Param("search") String search, @Param("category1") Category category1, @Param("category2") Category category2,
                                   @Param("category3") Category category3, @Param("userId") long userId);

    @Query("select item from Item item join fetch item.owner where item.id = ?1")
    Optional<Item> findById( Long id);

    @Query("select item from Item item where item.id = ?1")
    Optional<Item> findByIdNoJoin( Long id);

    @Query("select item from Item item where item.owner.login = ?1")
    List<Item> findItemsOfUser(String login);

    @Modifying
    @Query(value = "insert into Item_Interested VALUES (:id, :user_id)", nativeQuery = true )
    void addInterested(@Param("id") long id, @Param("user_id") long user_id);

}
