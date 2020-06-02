package com.bailbots.tg.QuartersBot.handler;

import com.bailbots.tg.QuartersBot.bpp.BotSelectHandle;
import com.bailbots.tg.QuartersBot.config.BotConfiguration;
import com.bailbots.tg.QuartersBot.dao.User;
import com.bailbots.tg.QuartersBot.dto.RegistrationDto;
import com.bailbots.tg.QuartersBot.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class QuartersBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LogManager.getLogger(QuartersBot.class);

    private final UserService userService;
    private final UserSessionService userSessionService;
    private final MessageValueService messageValueService;

    public QuartersBot(@Lazy UserService userService, UserSessionService userSessionService, @Lazy MessageValueService messageValueService) {
        this.userService = userService;
        this.userSessionService = userSessionService;
        this.messageValueService = messageValueService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = extractMessageFromUpdate(update);

        Long telegramId = message.getChatId();
        Chat chat = message.getChat();

        if (messageValueService.isGetValueTurn(telegramId)) {
            messageValueService.getValueFromMessage(update, telegramId);
            return;
        }

        boolean userExistInSession = userSessionService.getUserFromSession(telegramId) != null;

        if (!userExistInSession) {
            if (!userService.isUserExist(telegramId)) {
                String firstName = chat.getFirstName();
                String lastName = chat.getLastName();

                RegistrationDto registrationDto = createRegistrationDto(telegramId, firstName, lastName);

                User user = userService.registerUser(registrationDto);

                userSessionService.addUserToSession(telegramId, user);
            } else {
                User user = userService.getByTelegramId(telegramId);

                LOGGER.info("Telegram ID #{} successfully logged!", telegramId);
                userSessionService.addUserToSession(telegramId, user);
            }

        }

        BotSelectHandle.processByUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return BotConfiguration.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BotConfiguration.BOT_TOKEN;
    }

    private RegistrationDto createRegistrationDto(Long telegramId, String firstName, String lastName) {
        return RegistrationDto.builder()
                .telegramId(telegramId)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    private Message extractMessageFromUpdate(Update update) {
        Message message = update.getMessage();

        if (message == null) {
            if (update.getCallbackQuery().getMessage() != null) {
                message = update.getCallbackQuery().getMessage();
            } else {
                return null;
            }
        }
        return message;
    }
}
