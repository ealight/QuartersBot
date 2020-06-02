package com.bailbots.tg.QuartersBot.service;

import com.bailbots.tg.QuartersBot.dao.User;
import com.bailbots.tg.QuartersBot.dto.RegistrationDto;
import org.telegram.telegrambots.meta.api.objects.Message;


public interface UserService {
    User registerUser(RegistrationDto registrationDto);

    boolean isUserExist(Long telegramId);

    User getByTelegramId(Long telegramId);

}
