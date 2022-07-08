package com.yapp.pet.domain.club.repository;

import com.yapp.pet.domain.club.document.ClubDocument;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.common.PetSizeType;
import com.yapp.pet.web.club.model.SearchingClubDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class CustomClubSearchRepositoryImpl implements CustomClubSearchRepository{

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<ClubDocument> findByTitleCondition(SearchingClubDto.SearchingRequest request) {
        CriteriaQuery query = filter(request);

        SearchHits<ClubDocument> search = elasticsearchOperations.search(query, ClubDocument.class);

        return search.stream()
                     .map(SearchHit::getContent)
                     .collect(Collectors.toList());
    }

    public CriteriaQuery filter(SearchingClubDto.SearchingRequest request) {
        CriteriaQuery query = new CriteriaQuery(new Criteria());

        if (request == null) {
            return query;
        }

        if (request.getCategory() != null) {
            query.addCriteria(Criteria.where("category").is(request.getCategory()));
        }

        if (request.getStatus() != null) {
            query.addCriteria(Criteria.where("status").is(request.getStatus()));
        }

        if (request.getPetSizeType() != null && request.getPetSizeType() != PetSizeType.ALL) {
            query.addCriteria(Criteria.where("eligiblePetSizeTypes").in(request.getPetSizeType()));
        }

        if (request.getEligibleBreed() != null) {
            query.addCriteria(Criteria.where("eligibleBreeds").in(request.getEligibleBreed()));
        }

        if (request.getEligibleSex() != null && request.getEligibleSex() != EligibleSex.ALL) {
            query.addCriteria(Criteria.where("eligibleSex").is(request.getEligibleSex()));
        }

        if (request.getSearchingWord() != null) {
            query.addCriteria(Criteria.where("title").contains(request.getSearchingWord()));
        }

        return query;
    }
}
