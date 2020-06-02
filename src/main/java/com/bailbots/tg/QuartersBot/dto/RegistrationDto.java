package com.bailbots.tg.QuartersBot.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RegistrationDto {
    private Long telegramId;
    private String firstName;
    private String lastName;
}
