delete from account_club; alter table account_club auto_increment = 1;
delete from interest_categories; alter table interest_categories auto_increment = 1;
delete from pet_tag; alter table pet_tag auto_increment = 1;
delete from pet; alter table pet auto_increment = 1;
delete from comment; alter table comment auto_increment = 1;
delete from account; alter table account auto_increment = 1;
delete from account_image; alter table account_image auto_increment = 1;
delete from token; alter table token auto_increment = 1;
delete from eligible_pet_size_types; alter table eligible_pet_size_types auto_increment = 1;
delete from eligible_pet_breeds; alter table eligible_pet_breeds auto_increment = 1;
delete from club; alter table club auto_increment = 1;

insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlMSIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2Njg2NjY4NTIsImlhdCI6MTY1NTUyNjg1MiwiYXV0aCI6IlVTRVIifQ.kHnI8FuhqZpAYpVAuBieczAAXzXXk-E1dNMEO8gvMjTCiswEgBjeLpHyKyh6V-y8-WFET68l6I_GOk776ldKtQ', 'KAKAO', 'unique1');
insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlMiIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2Njg2NjY5NzAsImlhdCI6MTY1NTUyNjk3MCwiYXV0aCI6IlVTRVIifQ.7_QVjo2DtC5CFpIjHVUf7Q-La5wAGBZWbOC-R57C6DXS1kpIPiYtKyYo2HAnTuANU4zjv7AZA62iUr-FnmdHQQ', 'APPLE', 'unique2');
insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlMyIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2Njg2NjY5ODcsImlhdCI6MTY1NTUyNjk4NywiYXV0aCI6IlVTRVIifQ.w3UdeFkUr8MXxnL7PZmiujC0rZklBt_mFFa8uV8bUluRsvVtTxQWU4bmtA4udav4J6nEmKOyEwJtSeWdQzRNQA', 'KAKAO', 'unique3');
insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlNCIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2Njg2NjY5OTksImlhdCI6MTY1NTUyNjk5OSwiYXV0aCI6IlVTRVIifQ.iXt7v6zGPvQ4wOjdUM6zsBYc-da7JB3xBr8ETDIcqnn56yJizoy8nRcsDfyRKZ2R_TGcZDoJzkMEkNKhoeFOvg', 'KAKAO', 'unique4');
insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlNSIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2NTc0NTIyNDQsImlhdCI6MTY1Njg0NzQ0NCwiYXV0aCI6IlVTRVIifQ.sp8GYmUnJCGuAjYh1r4O99QSKPi8iiAvUSyRnx17ijBSrywfbBSQcT0isBccUw90yXSwVkGmx1l5cdGlOc1hgw', 'APPLE', 'unique5');
insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlNiIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2NTc0NTIyNzksImlhdCI6MTY1Njg0NzQ3OSwiYXV0aCI6IlVTRVIifQ.fBXqvXpocfut_dhMS8HixSglsporH_VhxLOLIEf2Nu1ExZimkpfReSlyT_L6RQO5-KAaGCgkcn1Svy74Rcolpw', 'APPLE', 'unique6');

insert into account_image (name, origin_name, s3key, path) values ('1655044614407_cat.jpg', 'cat.jpg', '????????????1+?????????1', 'https://togaether.s3.ap-northeast-2.amazonaws.com/account/1655044614407_cat.jpg');
insert into account_image (name, origin_name, s3key, path) values ('486421_?????????2', '?????????2', '????????????2+?????????2', 'https://vdfkopvd.com');
insert into account_image (name, origin_name, s3key, path) values ('486421_?????????3', '?????????3', '????????????3+?????????3', 'https://daskodpas.com');

insert into account (age, nickname, sex, token_id, city, detail, self_introduction, account_image_id)
values (10, '????????????', 'MAN', 1, '???????????????', '?????????', '?????? ????????? ???????????????.', 1);

insert into account (age, nickname, sex, token_id, city, detail, account_image_id)
values (20, '???????????? ?????????', 'WOMAN', 2, '?????????', '?????????', 2);

insert into account (age, nickname, sex, token_id, city, detail, self_introduction, account_image_id)
values (30, 'yapp', 'PRIVATE', 3, '?????????', '?????????', 'yapp ???????????????~', 3);

insert into account (age, nickname, sex, token_id, city, detail)
values (40, 'abcd', 'MAN', 4, '?????????', '?????????');

insert into account (age, nickname, sex, token_id, city, detail)
values (50, '?????????', 'MAN', 5, '?????????', '?????????');

insert into account (age, nickname, sex, token_id, city, detail)
values (60, 'tmp', 'MAN', 6, '?????????', '?????????');

insert into interest_categories (account_id, interest_categories) values (1, 'WALK');
insert into interest_categories (account_id, interest_categories) values (1, 'DOG_CAFE');
insert into interest_categories (account_id, interest_categories) values (1, 'PLAY_GROUND');
insert into interest_categories (account_id, interest_categories) values (2, 'EXPOSITION');
insert into interest_categories (account_id, interest_categories) values (2, 'DOG_FRIENDLY_RESTAURANT');
insert into interest_categories (account_id, interest_categories) values (3, 'ETC');

-- ????????????????????? ?????? ??????
insert into club (category, description, title, meeting_place, status, maximum_people, eligible_sex, start_date, end_date, latitude, longitude, participants)
values ('WALK', 'description', '????????? ???????????????', 'place', 'AVAILABLE', 3, 'MAN', '2021-01-01', '2021-01-02 09:00:00', 37.523717, 126.981598, 2);

-- ????????????????????? ?????? ??????
insert into club (category, description, eligible_sex, end_date, latitude, longitude, maximum_people, meeting_place, start_date, status, title, participants)
values ('DOG_CAFE', 'description', 'WOMAN', '2022-02-05 09:00:00', 37.506706, 126.961290, 3, '?????????', '2022-02-01', 'AVAILABLE', '??????????????? ?????????', 1);

-- ????????????
insert into club (category, description, eligible_sex, end_date, latitude, longitude, maximum_people, meeting_place, start_date, status, title, participants)
values ('PLAY_GROUND', 'description', 'ALL', '2022-01-05 09:00:00', 37.510033, 126.981571, 3, '?????????', '2022-01-01', 'AVAILABLE', '????????? ??????', 1);

insert into club (category, description, eligible_sex, end_date, latitude, longitude, maximum_people, meeting_place, start_date, status, title, participants)
values ('PLAY_GROUND', 'description', 'ALL', '2022-07-01 09:00:00', 37.510033, 126.981571, 3, '?????????', '2022-06-01', 'AVAILABLE', '???????????????', 1);

insert into club (category, description, eligible_sex, end_date, latitude, longitude, maximum_people, meeting_place, start_date, status, title, participants)
values ('PLAY_GROUND', 'description', 'ALL', '2022-08-05 09:00:00', 37.510033, 126.981571, 10, '?????????', '2022-05-01', 'AVAILABLE', '????????? ?????? ???', 1);

insert into club (category, description, eligible_sex, end_date, latitude, longitude, maximum_people, meeting_place, start_date, status, title, participants)
values ('PLAY_GROUND', 'description', 'ALL', '2022-09-05 09:00:00', 37.510033, 126.981571, 5, '?????????', '2022-04-01', 'END', '????????? ???????????? ??????', 1);

insert into club (category, description, eligible_sex, end_date, latitude, longitude, maximum_people, meeting_place, start_date, status, title, participants)
values ('PLAY_GROUND', 'description', 'ALL', '2022-10-05 09:00:00', 37.510033, 126.981571, 1, '?????????', '2022-03-01', 'PERSONNEL_FULL', '??????', 1);

-- 1??? account??? 6?????? club(1,4,5,6,7,8)??? ????????? / 6,7,8 club??? ????????? ??????
insert into account_club (leader, account_id, club_id) values (true, 1, 1);
insert into account_club (leader, account_id, club_id) values (false , 2, 1);
insert into account_club (leader, account_id, club_id) values (true, 3, 2);
insert into account_club (leader, account_id, club_id) values (true, 4, 3);
insert into account_club (leader, account_id, club_id) values (true, 1, 4);
insert into account_club (leader, account_id, club_id) values (true, 1, 5);
insert into account_club (leader, account_id, club_id) values (true, 1, 6);
insert into account_club (leader, account_id, club_id) values (true, 1, 7);

insert into comment (content, account_id, club_id, updated_at) values ('??????1', 1, 1, '2022-05-01');
insert into comment (content, account_id, club_id, updated_at) values ('??????2', 2, 1, '2022-05-01');

insert into eligible_pet_breeds (club_id, eligible_breeds) values (1, '?????????');
insert into eligible_pet_breeds (club_id, eligible_breeds) values (1, '????????????');

insert into eligible_pet_size_types (club_id, eligible_pet_size_types) values (1, 'LARGE');
insert into eligible_pet_size_types (club_id, eligible_pet_size_types) values (1, 'MEDIUM');
insert into eligible_pet_size_types (club_id, eligible_pet_size_types) values (2, 'ALL');
insert into eligible_pet_size_types (club_id, eligible_pet_size_types) values (3, 'ALL');
insert into eligible_pet_size_types (club_id, eligible_pet_size_types) values (4, 'SMALL');
insert into eligible_pet_size_types (club_id, eligible_pet_size_types) values (5, 'LARGE');
insert into eligible_pet_size_types (club_id, eligible_pet_size_types) values (5, 'MEDIUM');
insert into eligible_pet_size_types (club_id, eligible_pet_size_types) values (5, 'SMALL');
insert into eligible_pet_size_types (club_id, eligible_pet_size_types) values (6, 'SMALL');
insert into eligible_pet_size_types (club_id, eligible_pet_size_types) values (7, 'SMALL');
insert into eligible_pet_size_types (club_id, eligible_pet_size_types) values (7, 'MEDIUM');

insert into pet_image (name, origin_name, path, s3Key) values ('90419_????????????1', '????????????1', 'https://acjic.com', '????????????4+?????????4');

insert into pet (age, birth_month, birth_year, breed, name, neutering, sex, size_type, account_id, pet_image_id)
values (5, '2', '2', '?????????', '?????????', true, 'MALE', 'SMALL', 1, 1);
insert into pet (age, birth_month, birth_year, breed, name, neutering, sex, size_type, account_id)
values (4, '1', '1', '????????????', '??????', true, 'MALE', 'LARGE', 1);
insert into pet (age, birth_month, birth_year, breed, name, neutering, sex, size_type, account_id)
values (9, '1', '1', '???????????????', '??????', true, 'MALE', 'SMALL', 2);
insert into pet (age, birth_month, birth_year, breed, name, neutering, sex, size_type, account_id)
values (2, '1', '1', '???????????????', '??????', true, 'MALE', 'MEDIUM', 4);
insert into pet (age, birth_month, birth_year, breed, name, neutering, sex, size_type, account_id)
values (2, '1', '1', '??????', '?????????', true, 'MALE', 'SMALL', 5);
insert into pet (age, birth_month, birth_year, breed, name, neutering, sex, size_type, account_id)
values (8, '1', '1', '????????????', '??????', true, 'MALE', 'LARGE', 6);

insert into pet_tag (name, pet_id) values ('tag1', 1);
insert into pet_tag (name, pet_id) values ('tag2', 1);
insert into pet_tag (name, pet_id) values ('tag3', 1);
insert into pet_tag (name, pet_id) values ('tag4', 2);
insert into pet_tag (name, pet_id) values ('tag5', 2);