package com.bailbots.tg.QuartersBot.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HouseFilter {
    private int minSeatsNumber;

    private int minPrice;

    private int maxPrice;

    private boolean swimmingPool;

    private boolean bath;
}
