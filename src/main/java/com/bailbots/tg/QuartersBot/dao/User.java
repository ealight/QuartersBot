package com.bailbots.tg.QuartersBot.dao;

import com.bailbots.tg.QuartersBot.domain.HouseFilter;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "telegram_id")
    private Long telegramId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column
    private boolean notifications;

    @Column(name = "phone_number")
    private String phoneNumber;

    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(name = "reserve_house",
            joinColumns = { @JoinColumn(name = "telegram_id") },
            inverseJoinColumns = { @JoinColumn(name = "house_id") })
    private List<ReserveHouse> reserveHouses;

    @Transient
    @Builder.Default
    private HouseFilter houseFilter = HouseFilter.builder()
            .minSeatsNumber(0)
            .minPrice(0)
            .maxPrice(100000)
            .bath(true)
            .swimmingPool(true)
            .build();
}
