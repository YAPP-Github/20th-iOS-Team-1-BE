package com.yapp.pet.domain.token.repository;

import com.yapp.pet.domain.token.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long>, TokenRepositoryCustom{

}
