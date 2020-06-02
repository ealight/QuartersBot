package com.bailbots.tg.QuartersBot.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

public interface KeyboardObjectRepository {
    List<Object> getObjectListByEntity(Class clazz, Integer page, int pageSize);
    Long getPageNumbersOfObjectListByEntity(Class clazz);
}
