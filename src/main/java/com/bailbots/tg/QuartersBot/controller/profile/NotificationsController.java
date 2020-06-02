package com.bailbots.tg.QuartersBot.controller.profile;

import com.bailbots.tg.QuartersBot.bpp.annotation.BotController;
import com.bailbots.tg.QuartersBot.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.QuartersBot.dao.User;
import com.bailbots.tg.QuartersBot.repository.UserRepository;
import com.bailbots.tg.QuartersBot.service.MessageService;
import com.bailbots.tg.QuartersBot.service.UserSessionService;
import org.telegram.telegrambots.meta.api.objects.Update;

@BotController
public class NotificationsController {
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final UserSessionService userSessionService;

    public NotificationsController(MessageService messageService, UserRepository userRepository, UserSessionService userSessionService) {
        this.messageService = messageService;
        this.userRepository = userRepository;
        this.userSessionService = userSessionService;
    }

    @BotRequestMapping("Enable")
    public void enable(Update update) {
        Long telegramId = update.getCallbackQuery().getMessage().getChatId();

        User user = userSessionService.getUserFromSession(telegramId);

        if(user.isNotifications()) {
            messageService.sendMessage("❌ Уведомления уже включены", telegramId);
            return;
        }

        user.setNotifications(true);
        userRepository.save(user);

        String text = "Уведомления успешно включены \uD83D\uDD14" +
                "\n\uD83D\uDCE3 Каждый день в 7:00 я буду оповещать вас о ваших бронях";

        messageService.sendMessage(text, telegramId);
    }

    @BotRequestMapping("Disable")
    public void disable(Update update) {
        Long telegramId = update.getCallbackQuery().getMessage().getChatId();

        User user = userSessionService.getUserFromSession(telegramId);

        if(!user.isNotifications()) {
            messageService.sendMessage("❌ Уведомления уже выключены", telegramId);
            return;
        }

        user.setNotifications(false);
        userRepository.save(user);

        messageService.sendMessage("Уведомления успешно выключены \uD83D\uDD15", telegramId);
    }
}
