package com.yapp.pet.global.mapper;

import com.yapp.pet.domain.accountclub.AccountClub;
import com.yapp.pet.domain.club.entity.Club;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

import static com.yapp.pet.web.club.model.ClubFindDetailResponse.ClubDetailInfo;
import static com.yapp.pet.web.club.model.ClubFindResponse.ClubInfo;

@Mapper(componentModel = "spring")
public interface ClubMapper {

    @Mapping(target = "clubId", source = "id")
    @Mapping(target = "participants", source = "accountClubs", qualifiedByName = "getParticipants")
    ClubInfo toInfo(Club club);

    @Named("getParticipants")
    default int getParticipants(List<AccountClub> accountClubs){
        return accountClubs.size();
    }

    @Mapping(target = "participants", source = "accountClubs", qualifiedByName = "getParticipants")
    ClubDetailInfo toDetailInfo(Club club);
}
