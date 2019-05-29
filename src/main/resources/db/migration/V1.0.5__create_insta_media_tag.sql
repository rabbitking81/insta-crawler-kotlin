-- auto-generated definition
create table insta_media_tag
(
    id       bigserial    not null
        constraint insta_media_tag_pk
            primary key,
    media_id bigserial    not null,
    tag_name varchar(256) not null
);

alter table insta_media_tag
    owner to gelato;
