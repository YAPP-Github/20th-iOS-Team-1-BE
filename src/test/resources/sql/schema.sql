

create table account_image
(
    account_image_id bigint auto_increment
        primary key,
    created_at       datetime(6)  null,
    updated_at       datetime(6)  null,
    name             varchar(255) not null,
    origin_name      varchar(255) not null,
    path             varchar(255) not null,
    s3key            varchar(255) not null
);

create table club
(
    club_id              bigint auto_increment
        primary key,
    created_at           datetime(6)  null,
    updated_at           datetime(6)  null,
    category             varchar(255) not null,
    description          text         null,
    eligible_sex         varchar(255) not null,
    end_date             datetime(6)  not null,
    latitude             double       not null,
    longitude            double       not null,
    maximum_people       int          not null,
    meeting_place        varchar(255) not null,
    start_date           datetime(6)  not null,
    status               varchar(255) not null,
    title                varchar(70)  not null,
    version              int          null,
    participating_people int          null,
    participants         int          not null
);

create index idx_latitude
    on club (latitude);

create index idx_longitude
    on club (longitude);

create table eligible_pet_breeds
(
    club_id         bigint       not null,
    eligible_breeds varchar(255) not null,
    primary key (club_id, eligible_breeds),
    constraint FKohr6o70qti242a556nlvfwtw2
        foreign key (club_id) references club (club_id)
);

create table eligible_pet_size_types
(
    club_id                 bigint       not null,
    eligible_pet_size_types varchar(255) not null,
    primary key (club_id, eligible_pet_size_types),
    constraint FK2rr3s8yo541etjgrnc40i45q0
        foreign key (club_id) references club (club_id)
);

create table pet_image
(
    pet_image_id bigint auto_increment
        primary key,
    created_at   datetime(6)  null,
    updated_at   datetime(6)  null,
    name         varchar(255) not null,
    origin_name  varchar(255) not null,
    path         varchar(255) not null,
    s3key        varchar(255) not null
);

create table report
(
    report_id           bigint auto_increment
        primary key,
    created_at          datetime(6)  null,
    updated_at          datetime(6)  null,
    deleted             bit          null,
    reason              varchar(100) null,
    reported_acoount_id bigint       null,
    reported_club_id    bigint       null,
    reported_comment_id bigint       null,
    reporter_acoount_id bigint       null,
    reported_account_id bigint       null,
    reporter_account_id bigint       null
);

create table token
(
    token_id            bigint auto_increment
        primary key,
    created_at          datetime(6)  null,
    updated_at          datetime(6)  null,
    refresh_token       longtext     null,
    social_type         varchar(255) not null,
    unique_id_by_social varchar(255) not null,
    constraint UK_303uypnih32nx0jh5ruodcb60
        unique (unique_id_by_social)
);

create table account
(
    account_id        bigint auto_increment
        primary key,
    created_at        datetime(6)  null,
    updated_at        datetime(6)  null,
    city              varchar(255) null,
    detail            varchar(255) null,
    age               int          not null,
    email             varchar(255) null,
    nickname          varchar(10)  not null,
    self_introduction varchar(60)  null,
    sex               varchar(255) not null,
    account_image_id  bigint       null,
    token_id          bigint       null,
    constraint UK_q0uja26qgu1atulenwup9rxyr
        unique (email),
    constraint UK_s2a5omeaik0sruawqpvs18qfk
        unique (nickname),
    constraint FK1hjxf86bny48myruv9xv3tw62
        foreign key (account_image_id) references account_image (account_image_id),
    constraint FKjghmieghgobxbl45atn46lo3k
        foreign key (token_id) references token (token_id)
);

create table account_club
(
    account_club_id bigint auto_increment
        primary key,
    created_at      datetime(6)                  null,
    updated_at      datetime(6)                  null,
    leader          tinyint unsigned default '0' null,
    account_id      bigint                       null,
    club_id         bigint                       null,
    version         int                          null,
    constraint FKetiq7tb8t770v2mbtr9x8r10
        foreign key (account_id) references account (account_id),
    constraint FKn51oweqmgdaml6jriivb2ndv3
        foreign key (club_id) references club (club_id)
);

create table comment
(
    comment_id bigint auto_increment
        primary key,
    created_at datetime(6) null,
    updated_at datetime(6) null,
    content    text        not null,
    account_id bigint      null,
    club_id    bigint      null,
    constraint FKfas0jm4664vilp9k50ixnqj0h
        foreign key (club_id) references club (club_id),
    constraint FKp41h5al2ajp1q0u6ox3i68w61
        foreign key (account_id) references account (account_id)
);

create table interest_categories
(
    account_id          bigint       not null,
    interest_categories varchar(255) null,
    constraint FKad6ls6c9wl9b481vhvld9sohx
        foreign key (account_id) references account (account_id)
);

create table pet
(
    pet_id       bigint auto_increment
        primary key,
    created_at   datetime(6)  null,
    updated_at   datetime(6)  null,
    age          varchar(255) null,
    birth_month  int          not null,
    birth_year   int          not null,
    breed        varchar(255) null,
    name         varchar(30)  not null,
    neutering    bit          not null,
    sex          varchar(255) not null,
    size_type    varchar(255) not null,
    account_id   bigint       null,
    pet_image_id bigint       null,
    constraint FK2nrjk6qfkfqykera7g4rkq9uq
        foreign key (account_id) references account (account_id),
    constraint FKgv1s6jiwy15980jxhoanbphvo
        foreign key (pet_image_id) references pet_image (pet_image_id)
);

create table pet_tag
(
    pet_tag_id bigint auto_increment
        primary key,
    created_at datetime(6) null,
    updated_at datetime(6) null,
    name       varchar(10) not null,
    pet_id     bigint      null,
    constraint FK9xeu6efunv152wmqxd426er78
        foreign key (pet_id) references pet (pet_id)
);


