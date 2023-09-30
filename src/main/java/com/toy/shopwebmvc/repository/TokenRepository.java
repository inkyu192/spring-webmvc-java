package com.toy.shopwebmvc.repository;

import com.toy.shopwebmvc.domain.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
}
