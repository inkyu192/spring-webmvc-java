package com.toy.shop.business.category.dto;

import com.toy.shop.domain.Category;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

public class CategoryDto {

    @Getter @Setter
    public static class Save {

        @NotEmpty
        private String name;

        @NotEmpty
        private String description;
    }

    @Getter
    public static class Update {

        private String name;

        private String description;
    }

    @Getter
    public static class Response {

        private Long id;
        private String name;
        private String description;

        public Response(Category category) {
            this.id = category.getId();
            this.name = category.getName();
            this.description = category.getDescription();
        }
    }
}
