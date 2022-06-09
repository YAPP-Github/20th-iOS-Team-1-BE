delete from account_club;
ALTER table account_club auto_increment = 1;
delete from account_image;
ALTER table account_image auto_increment = 1;
delete from account;
ALTER table account auto_increment = 1;
delete from token;
ALTER table token auto_increment = 1;
delete from club;
ALTER table club auto_increment = 1;

insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlMSIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2Njc5MjUwODEsImlhdCI6MTY1NDc4NTA4MSwiYXV0aCI6IlVTRVIifQ.-CF4LjgOrevjUMyyFG6VxoM6t5DX2-GGDpolaclLSHb46FmTAJRhTfAqaWZwh_VSZgI7CezrreRqfL9yUNayqA', 'KAKAO', 'unique1');
insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlMiIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2Njc5MjUyMTEsImlhdCI6MTY1NDc4NTIxMSwiYXV0aCI6IlVTRVIifQ.ooSfHxIEfxMF8J5go2Uo0tFZFN233FIRxnfXMAQbkESpOXZ0LFUfs3Znii8KjeZsCGJOcHrMg_uHOalTcsQghg', 'APPLE', 'unique2');
insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlMyIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2Njc5MjUyMzEsImlhdCI6MTY1NDc4NTIzMSwiYXV0aCI6IlVTRVIifQ.d9D7d5pw7mvSFqFTU6H2YpMSmDP35ZZDPfsQhFpDESmBqtclTT3qtRgygLZOefkB_CJVip_DabaW-amdlBWqzw', 'KAKAO', 'unique3');

insert into account (age, nickname, sex, token_id) values (10, '재롱잔치', 'MAN', 1);
insert into account (age, nickname, sex, token_id) values (20, '밀란이네 시트콤', 'WOMAN', 2);
insert into account (age, nickname, sex, token_id) values (30, 'yapp', 'PRIVATE', 3);

insert into club (category, description, title, meeting_place, status, maximum_people, eligible_sex, start_date, end_date, latitude, longitude)
values ('WALK', 'des', 'title', 'place', 'AVAILABLE', 2, 'MAN', '2021-01-01', '2021-01-02', 2, 3);
insert into club (category, description, eligible_sex, end_date, latitude, longitude,
                  maximum_people, meeting_place, start_date, status, title)
values ('DOG_CAFE', '설명', 'WOMAN', '2022-02-05', 23, 24, 3, 'ㅁㄴㅇ', '2022-02-01', 'AVAILABLE', '산책할사람');
insert into club (category, description, eligible_sex, end_date, latitude, longitude,
                  maximum_people, meeting_place, start_date, status, title)
values ('WALK', '설명', 'ALL', '2022-01-05', 23, 24, 3, 'ㅁㄴㅇ', '2022-01-01', 'AVAILABLE', '떙떙 산책');

insert into account_club (leader, account_id, club_id) values (true, 1, 1);
insert into account_club (leader, account_id, club_id) values (false , 2, 1);
insert into account_club (leader, account_id, club_id) values (true, 3, 2);