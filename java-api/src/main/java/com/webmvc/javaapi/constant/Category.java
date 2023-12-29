package com.webmvc.javaapi.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {

    ROLE_BOOK("책"),
    ROLE_TICKET("표");

    private final String description;
}
