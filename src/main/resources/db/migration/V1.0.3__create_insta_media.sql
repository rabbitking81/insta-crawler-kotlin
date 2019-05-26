-- auto-generated definition
create table insta_media
(
    id                 bigserial                              not null
        constraint insta_media_pk
            primary key,
    short_code         varchar(50)                            not null,
    image_url          varchar(512)                           not null,
    insta_type         varchar(20)                            not null,
    user_id            bigserial                              not null,
    insta_created_date timestamp(6)                           not null,
    created_date       timestamp(6) default CURRENT_TIMESTAMP not null
);

alter table insta_media
    owner to gelato;

