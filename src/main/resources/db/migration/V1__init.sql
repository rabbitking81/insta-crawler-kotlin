create table insta_account
(
    id                 bigserial                                       not null,
    insta_account_id   bigserial                                       not null,
    insta_account_name varchar(64)                                     not null,
    insta_full_name    varchar(128)                                    not null,
    status             varchar(20)  default 'READY'::character varying not null,
    created_date       timestamp(6) default CURRENT_TIMESTAMP          not null
);

alter table insta_account
    owner to gelato;