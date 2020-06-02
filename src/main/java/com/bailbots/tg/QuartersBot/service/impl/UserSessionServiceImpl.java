package com.bailbots.tg.QuartersBot.service.impl;

import com.bailbots.tg.QuartersBot.dao.User;
import com.bailbots.tg.QuartersBot.service.UserSessionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@EnableScheduling
@Service
public class UserSessionServiceImpl implements UserSessionService {
    private static final Logger LOGGER = LogManager.getLogger(UserSessionServiceImpl.class);

    private Map<Long, User> usersInSession = new HashMap<>();
    private Map<Long, Long> telegramIdToExpirationTime = new HashMap<>();

    @Value("${bot.session.time.millis}")
    private Long sessionTime;

    @Override
    public void addUserToSession(Long telegramId, User user) {
        Long timestamp = System.currentTimeMillis();

        usersInSession.put(telegramId, user);
        telegramIdToExpirationTime.put(telegramId, timestamp + sessionTime);

        LOGGER.info("Telegram ID #{} successfully added to session", telegramId);
    }

    @Scheduled(cron = "0 0/${bot.session.time.checkdelay} * * * *")
    private void removeExpiredUserSession() {
        Long currentTimestamp = System.currentTimeMillis();
        int startSize = usersInSession.size();

        Set<Long> collect = telegramIdToExpirationTime.entrySet().stream()
                .filter(entry -> currentTimestamp > entry.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toUnmodifiableSet());

        for (Long telegramIdToRemove : collect) {
            usersInSession.remove(telegramIdToRemove);
            telegramIdToExpirationTime.remove(telegramIdToRemove);
        }

        LOGGER.info("Removed {} logged users", startSize - usersInSession.size());
    }

    @Override
    public User getUserFromSession(Long telegramId) {
        return usersInSession.get(telegramId);
    }
}
