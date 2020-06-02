package com.bailbots.tg.QuartersBot.repository;

import com.bailbots.tg.QuartersBot.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByTelegramId(Long telegramId);

    User getByTelegramId(Long telegramId);

}
