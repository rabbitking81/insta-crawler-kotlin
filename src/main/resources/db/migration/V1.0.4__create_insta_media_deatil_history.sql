-- auto-generated definition
create table insta_media_detail_history
(
    id                 bigserial                              not null
        constraint insta_media_detail_history_pk
            primary key,
    media_id           bigint                                 not null,
    like_count         integer      default 0                 not null,
    comment_count      integer      default 0                 not null,
    insta_created_date timestamp(6)                           not null,
    created_date       timestamp(6) default CURRENT_TIMESTAMP not null
);

alter table insta_media_detail_history
    owner to gelato;

