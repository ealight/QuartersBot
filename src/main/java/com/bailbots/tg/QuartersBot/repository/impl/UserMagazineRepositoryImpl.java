package com.bailbots.tg.QuartersBot.repository.impl;

import com.bailbots.tg.QuartersBot.repository.UserMagazineRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;

@Repository
public class UserMagazineRepositoryImpl implements UserMagazineRepository {
    private static final String SAVE_QUERY = "INSERT INTO `user_magazine` (`telegram_id`, `house_id`) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM `user_magazine` WHERE `house_id` = ?";
    private static final String EXIST_BY_ID_QUERY = "SELECT EXISTS(SELECT 1 FROM `user_magazine` WHERE `telegram_id` = ? AND `house_id` = ?)";

    private final EntityManager entityManager;

    public UserMagazineRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void saveForTelegramId(Long telegramId, Long houseId) {
        Query query = entityManager.createNativeQuery(SAVE_QUERY);
        query   .setParameter(1, telegramId)
                .setParameter(2, houseId)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void removeById(Long houseId) {
        Query query = entityManager.createNativeQuery(DELETE_QUERY);
        query   .setParameter(1, houseId)
                .executeUpdate();
    }

    @Override
    public boolean houseIdAlreadyExistForTelegramId(Long houseId, Long telegramId) {
        Query query = entityManager.createNativeQuery(EXIST_BY_ID_QUERY);
        query   .setParameter(1, telegramId)
                .setParameter(2, houseId);
        return query.getResultList().get(0).equals(BigInteger.valueOf(1));
    }
}
