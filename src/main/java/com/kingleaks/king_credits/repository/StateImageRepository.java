package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.StateImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateImageRepository extends JpaRepository<StateImage, Long> {

    @Query(value = "SELECT s.* FROM state_image s", nativeQuery = true)
    List<StateImage> findAllStateForPicture();

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM state_image s WHERE s.photo_data IS NULL AND s.id = :id", nativeQuery = true)
    Boolean isStateImageHasPicture(@Param("id") Long id);
}
