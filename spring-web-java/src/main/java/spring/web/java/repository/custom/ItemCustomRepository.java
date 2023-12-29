package spring.web.java.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.web.java.domain.Item;

public interface ItemCustomRepository {

    Page<Item> findAllWithJpql(Pageable pageable, String name);

    Page<Item> findAllWithQuerydsl(Pageable pageable, String name);
}
