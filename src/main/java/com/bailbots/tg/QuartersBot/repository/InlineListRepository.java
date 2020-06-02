package com.bailbots.tg.QuartersBot.repository;

import com.bailbots.tg.QuartersBot.dao.InlineList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InlineListRepository extends JpaRepository<InlineList, Long> {

    InlineList getById(Long id);

}
