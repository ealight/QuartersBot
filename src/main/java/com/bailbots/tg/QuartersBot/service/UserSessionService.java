package com.bailbots.tg.QuartersBot.service;

import com.bailbots.tg.QuartersBot.dao.User;

public interface UserSessionService {
    void addUserToSession(Long telegramId, User user);

    User getUserFromSession(Long telegramId);

}
