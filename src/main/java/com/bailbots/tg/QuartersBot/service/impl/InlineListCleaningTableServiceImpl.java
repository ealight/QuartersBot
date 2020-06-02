package com.bailbots.tg.QuartersBot.service.impl;

import com.bailbots.tg.QuartersBot.service.InlineListCleaningTableService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@EnableScheduling
@Service
public class InlineListCleaningTableServiceImpl implements InlineListCleaningTableService {
    private static final Logger LOGGER = LogManager.getLogger(InlineListCleaningTableServiceImpl.class);
    private static final String CLEANING_QUERY = "DELETE FROM `inline_list` WHERE UNIX_TIMESTAMP(`deletion_time`) + ? < UNIX_TIMESTAMP(CURRENT_TIMESTAMP)";

    private final EntityManager entityManager;

    @Value("${bot.inlinelist.callback.lifetime}")
    private Long callbackLifeTime;

    public InlineListCleaningTableServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void cleaningTable() {
        Query query = entityManager.createNativeQuery(CLEANING_QUERY);
        query.setParameter(1, callbackLifeTime);
        LOGGER.info("Removed {} records from table 'inline_list'", query.executeUpdate());
    }
    //This method will be invoke every day on 00:00 hours
}
