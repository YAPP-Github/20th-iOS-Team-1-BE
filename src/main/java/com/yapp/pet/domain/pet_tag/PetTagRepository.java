package com.yapp.pet.domain.pet_tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PetTagRepository extends JpaRepository<PetTag, Long> {  //covering index

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("delete from PetTag pt where pt.id in :ids")
    int deletePetTagByPetId(@Param("ids") List<Long> ids);

}
