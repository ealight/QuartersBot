create table `user_magazine` (
                                `telegram_id` bigint not null,
                                `house_id` bigint not null,
                                primary key (`telegram_id`, `house_id`),
                                foreign key (`house_id`) references `house` (`id`),
                                foreign key (`telegram_id`) references `user` (`telegram_id`)
)