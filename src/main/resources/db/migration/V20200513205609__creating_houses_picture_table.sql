create table `house_picture`
(
    `id`        bigint auto_increment not null,
    `house_id`   bigint                not null,
    `url`      varchar(1024)         not null,
    primary key (`id`),
    foreign key (`house_id`) references `house` (`id`)
)