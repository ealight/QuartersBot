package com.bailbots.tg.QuartersBot.repository;

import com.bailbots.tg.QuartersBot.dao.House;
import com.bailbots.tg.QuartersBot.dao.ReserveHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;


@Repository
public interface ReserveHouseRepository extends JpaRepository<ReserveHouse, Long> {
    boolean existsByTelegramIdAndHouseId(Long telegramId, Long houseId);

    boolean existsByHouseId(Long houseId);

    ReserveHouse getByHouseId(Long houseId);

    @Query(value = "SELECT EXISTS(" +
            "SELECT TRUE FROM reserve_house " +
            "WHERE CAST(date_from AS DATE) <= CAST(?2 AS DATE) " +
            "AND CAST(date_to AS DATE) >= CAST(?2 AS DATE) " +
            "AND house_id = ?1" +
            ")", nativeQuery = true)
    int existByDateBetweenReserveDates(Long houseId, Date date);

    ReserveHouse getByHouseIdAndDateToAfterAndDateFromBefore(Long houseId, Date finish, Date start);
    ReserveHouse getByHouseIdAndDateFromBeforeAndDateToAfter(Long houseId, Date start, Date finish);
}
