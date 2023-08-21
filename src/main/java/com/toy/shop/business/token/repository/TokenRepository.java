package com.toy.shop.business.token.repository;

import com.toy.shop.domain.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Long> {
}
