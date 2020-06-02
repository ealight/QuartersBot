package com.bailbots.tg.QuartersBot.repository;

import com.bailbots.tg.QuartersBot.dao.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House, Long>, HouseRepositoryCustom {

    House getById(Long id);

    Long countByFilters(int minSeats, int minPrice, int maxPrice, boolean swimmingPol, boolean bath);

    List<House> getHousesListByFilters(int minSeats, int minPrice, int maxPrice, boolean swimmingPool, boolean bath, Integer page, int pageSize);

    Long countByTelegramId(Long telegramId, String table);

    List<House> getHousesListByTelegramId(Long telegramId, String table, Integer page, int pageSize);

}
