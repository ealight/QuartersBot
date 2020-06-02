create table `user`
(
    `id`           bigint auto_increment not null,
    `telegram_id`  bigint                not null,
    `first_name`   varchar(32)           not null,
    `last_name`    varchar(32)           not null,
    `phone_number` varchar(16),
    primary key (`id`),
    unique index `unique_telegram_id` (`telegram_id` asc),
    unique index `unique_phone_number` (`phone_number` desc)
)