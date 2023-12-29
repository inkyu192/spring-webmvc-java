package com.webmvc.javaapi.repository;


import com.webmvc.javaapi.domain.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
}
