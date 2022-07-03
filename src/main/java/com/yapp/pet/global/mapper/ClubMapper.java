package com.yapp.pet.global.mapper;

import com.yapp.pet.domain.accountclub.AccountClub;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.web.club.model.ClubCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.yapp.pet.web.club.model.ClubFindDetailResponse.ClubDetailInfo;
import static com.yapp.pet.web.club.model.ClubFindResponse.ClubInfo;

@Mapper(componentModel = "spring")
public interface ClubMapper {

    @Mapping(target = "clubId", source = "id")
    @Mapping(target = "participants", source = "accountClubs", qualifiedByName = "getParticipants")
    ClubInfo toInfo(Club club);

    @Named("getParticipants")
    default int getParticipants(List<AccountClub> accountClubs) {
        return accountClubs.size();
    }

    @Mapping(target = "participants", source = "accountClubs", qualifiedByName = "getParticipants")
    ClubDetailInfo toDetailInfo(Club club);

    @Mapping(target = "startDate", source = "startDate", qualifiedByName = "toZonedDateTime")
    @Mapping(target = "endDate", source = "endDate", qualifiedByName = "toZonedDateTime")
    Club toEntity(ClubCreateRequest clubCreateRequest);

    @Named("toZonedDateTime")
    default ZonedDateTime toZonedDateTime(LocalDateTime localDateTime) {
        return ZonedDateTime.of(localDateTime, ZoneId.of("Asia/Seoul"));
    }
}
