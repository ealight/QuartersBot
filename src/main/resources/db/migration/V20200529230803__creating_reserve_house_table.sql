create table `reserve_house`
(
    `id`          bigint auto_increment not null,
    `telegram_id` bigint                not null,
    `house_id`    bigint                not null,
    `date_from`   datetime              not null,
    `date_to`     datetime              not null,
    primary key (`id`, `telegram_id`, `house_id`),
    foreign key (`house_id`) references `house` (`id`),
    foreign key (`telegram_id`) references `user` (`telegram_id`)
)