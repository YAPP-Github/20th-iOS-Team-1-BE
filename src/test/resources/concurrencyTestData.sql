delete from account_club;
alter table account_club auto_increment = 1;

delete from interest_categories;
alter table interest_categories auto_increment = 1;

delete from pet_tag;
alter table pet_tag auto_increment = 1;

delete from pet;
alter table pet auto_increment = 1;

delete from comment;
alter table comment auto_increment = 1;

delete from account;
alter table account auto_increment = 1;

delete from account_image;
alter table account_image auto_increment = 1;

delete from token;
alter table token auto_increment = 1;

delete from eligible_pet_size_types;
alter table eligible_pet_size_types auto_increment = 1;

delete from eligible_pet_breeds;
alter table eligible_pet_breeds auto_increment = 1;

delete from club;
alter table club auto_increment = 1;

insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlMSIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2Njg2NjY4NTIsImlhdCI6MTY1NTUyNjg1MiwiYXV0aCI6IlVTRVIifQ.kHnI8FuhqZpAYpVAuBieczAAXzXXk-E1dNMEO8gvMjTCiswEgBjeLpHyKyh6V-y8-WFET68l6I_GOk776ldKtQ', 'KAKAO', 'unique1');
insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlMiIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2Njg2NjY5NzAsImlhdCI6MTY1NTUyNjk3MCwiYXV0aCI6IlVTRVIifQ.7_QVjo2DtC5CFpIjHVUf7Q-La5wAGBZWbOC-R57C6DXS1kpIPiYtKyYo2HAnTuANU4zjv7AZA62iUr-FnmdHQQ', 'APPLE', 'unique2');

insert into account (age, nickname, sex, token_id, city, detail, self_introduction)
values (10, '재롱잔치', 'MAN', 1, '인천광역시', '남동구', '저는 재롱이 견주입니다.');
insert into account (age, nickname, sex, token_id, city, detail, self_introduction)
values (20, '밀란이네 시트콤', 'MAN', 2, '서울시', '강남구', '밀란이 견주에요~');

insert into club (category, description, title, meeting_place, status, maximum_people, eligible_sex, start_date, end_date, latitude, longitude, participants)
values ('WALK', 'description', '쿄쿄량 산책할사람', 'place', 'AVAILABLE', 2, 'MAN', '2021-01-01', '2021-01-02 09:00:00', 37.523717, 126.981598, 1);

insert into account_club (leader, account_id, club_id) values (true, 1, 1);

insert into eligible_pet_size_types (club_id, eligible_pet_size_types) values (1, 'SMALL');
insert into eligible_pet_size_types (club_id, eligible_pet_size_types) values (1, 'MEDIUM');
insert into eligible_pet_size_types (club_id, eligible_pet_size_types) values (1, 'LARGE');

insert into pet (age, birth_month, birth_year, breed, name, neutering, sex, size_type, account_id)
values (5, '2', '2', '말티즈', '재롱이', true, 'MALE', 'SMALL', 1);
insert into pet (age, birth_month, birth_year, breed, name, neutering, sex, size_type, account_id)
values (4, '1', '1', '리트리버', '밀란', true, 'MALE', 'LARGE', 2);
