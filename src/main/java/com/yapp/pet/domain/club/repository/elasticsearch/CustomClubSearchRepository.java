package com.yapp.pet.domain.club.repository.elasticsearch;

import com.yapp.pet.domain.club.document.ClubDocument;

import java.util.List;

import static com.yapp.pet.web.club.model.SearchingClubDto.*;

public interface CustomClubSearchRepository {

    public List<ClubDocument> findByTitleCondition(SearchingRequest request);
}
