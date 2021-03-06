package com.yapp.pet.domain.club.repository.elasticsearch;

import com.yapp.pet.domain.club.document.ClubDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ClubSearchRepository extends ElasticsearchRepository<ClubDocument, Long>, CustomClubSearchRepository {

    List<ClubDocument> findByTitleContaining(String title);
}