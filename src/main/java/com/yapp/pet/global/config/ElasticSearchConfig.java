package com.yapp.pet.global.config;

import com.yapp.pet.domain.club.document.AccountClubDocument;
import com.yapp.pet.domain.club.repository.ClubSearchRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableElasticsearchRepositories(basePackageClasses = ClubSearchRepository.class)
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${TOGATHER_PRIVATE_IP}")
    String connectedUrl;

    @Override
    public RestHighLevelClient elasticsearchClient() {

        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                                                                     .connectedTo(connectedUrl)
                                                                     .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(
                Arrays.asList(new AccountClubDocumentToMap(), new MapToAccountClub()));
    }

    @WritingConverter
    static class AccountClubDocumentToMap implements Converter<AccountClubDocument, Map<String, Long>> {

        @Override
        public Map<String, Long> convert(AccountClubDocument source) {
            Map<String, Long> target = new LinkedHashMap<>();

            target.put("club", source.getClubId());
            target.put("account", source.getAccountId());

            return target;
        }
    }

    @ReadingConverter
    static class MapToAccountClub implements Converter<Map<String, Long>, AccountClubDocument> {

        @Override
        public AccountClubDocument convert(Map<String, Long> source) {

            long accountId = ((Number) source.get("account")).longValue();

            long clubId = ((Number) source.get("club")).longValue();

            return AccountClubDocument.of(clubId, accountId);
        }
    }
}