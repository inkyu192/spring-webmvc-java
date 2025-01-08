package spring.web.java.domain.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.web.java.domain.item.Item;

public interface ItemCustomRepository {

	Page<Item> findAllUsingJpql(Pageable pageable, String name);

	Page<Item> findAllUsingQueryDsl(Pageable pageable, String name);
}
