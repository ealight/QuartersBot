package com.bailbots.tg.QuartersBot.repository;

import com.bailbots.tg.QuartersBot.dao.House;

import java.util.List;

public interface HouseRepositoryCustom {

    Long countByFilters(int minSeats, int minPrice, int maxPrice, boolean swimmingPol, boolean bath);

    List<House> getHousesListByFilters(int minSeats, int minPrice, int maxPrice, boolean swimmingPool, boolean bath, Integer page, int pageSize);

    Long countByTelegramId(Long telegramId, String table);

    List<House> getHousesListByTelegramId(Long telegramId, String table,Integer page, int pageSize);
}
