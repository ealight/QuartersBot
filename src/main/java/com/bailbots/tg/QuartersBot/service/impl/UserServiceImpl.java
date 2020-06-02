package com.bailbots.tg.QuartersBot.service.impl;

import com.bailbots.tg.QuartersBot.dao.User;
import com.bailbots.tg.QuartersBot.dto.RegistrationDto;
import com.bailbots.tg.QuartersBot.repository.UserRepository;
import com.bailbots.tg.QuartersBot.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(RegistrationDto dto) {
        User user = User.builder()
                .telegramId(dto.getTelegramId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();

        LOGGER.info("Telegram ID #{} successfully register!",  dto.getTelegramId());

        return userRepository.save(user);
    }

    @Override
    public boolean isUserExist(Long telegramId) {
        return userRepository.existsByTelegramId(telegramId);
    }

    @Override
    public User getByTelegramId(Long telegramId) {
        return userRepository.getByTelegramId(telegramId);
    }

}
