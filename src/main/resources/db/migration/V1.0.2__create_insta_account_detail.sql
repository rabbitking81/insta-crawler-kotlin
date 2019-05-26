create table insta_account_detail_history
(
    id             bigserial                              not null
        constraint insta_account_detail_history_pk
            primary key,
    user_id        bigint                                 not null
        constraint insta_account_detail_history_insta_account_id_fk
            references insta_account,
    follow_count   integer      default 0                 not null,
    followed_count integer      default 0                 not null,
    media_count    integer      default 0                 not null,
    is_privacy     boolean      default false             not null,
    created_date   timestamp(6) default CURRENT_TIMESTAMP not null
);

alter table insta_account_detail_history
    owner to gelato;