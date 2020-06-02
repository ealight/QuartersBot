package com.bailbots.tg.QuartersBot.repository.impl;

import com.bailbots.tg.QuartersBot.dao.House;
import com.bailbots.tg.QuartersBot.repository.HouseRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class HouseRepositoryCustomImpl implements HouseRepositoryCustom {
    private static final String GET_HOUSES_LIST_BY_FILTERS_QUERY = "from House h where h.seatsNumber >= :minSeats and h.price >= :minPrice and h.price <= :maxPrice and h.swimmingPool = :swimmingPool and h.bath = :bath";
    private static final String GET_HOUSES_COUNT_BY_FILTERS_QUERY = "select count(h) from House h where h.seatsNumber >= :minSeats and h.price >= :minPrice and h.price <= :maxPrice and h.swimmingPool = :swimmingPool and h.bath = :bath";
    private static final String GET_HOUSES_LIST_BY_USER_ID_QUERY = "SELECT * FROM house WHERE id IN (SELECT house_id FROM %s WHERE `telegram_id` = ?)";
    private static final String GET_HOUSES_COUNT_BY_USER_ID_QUERY = "SELECT COUNT(*) FROM house WHERE id IN (SELECT house_id FROM %s WHERE `telegram_id` = ?)";


    private final EntityManager entityManager;

    public HouseRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long countByFilters(int minSeats, int minPrice, int maxPrice, boolean swimmingPool, boolean bath) {
        Query query = entityManager.createQuery(GET_HOUSES_COUNT_BY_FILTERS_QUERY);
        query   .setParameter("minSeats", minSeats)
                .setParameter("minPrice", minPrice)
                .setParameter("maxPrice", maxPrice)
                .setParameter("swimmingPool", swimmingPool)
                .setParameter("bath", bath);
        return (Long) query.getResultList().get(0);
    }

    @Override
    public List<House> getHousesListByFilters(int minSeats, int minPrice, int maxPrice, boolean swimmingPool, boolean bath, Integer page, int pageSize) {
        Query query = entityManager.createQuery(GET_HOUSES_LIST_BY_FILTERS_QUERY);
        query   .setParameter("minSeats", minSeats)
                .setParameter("minPrice", minPrice)
                .setParameter("maxPrice", maxPrice)
                .setParameter("swimmingPool", swimmingPool)
                .setParameter("bath", bath)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public Long countByTelegramId(Long telegramId, String table) {
        Query query = entityManager.createNativeQuery(String.format(GET_HOUSES_COUNT_BY_USER_ID_QUERY, table));
        query.setParameter(1, telegramId);
        return Long.valueOf(query.getResultList().get(0).toString());
    }

    @Override
    public List<House> getHousesListByTelegramId(Long telegramId, String table, Integer page, int pageSize) {
        Query query = entityManager.createNativeQuery(String.format(GET_HOUSES_LIST_BY_USER_ID_QUERY, table), House.class);
        query
                .setParameter(1, telegramId)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize);
        return query.getResultList();
    }

}
