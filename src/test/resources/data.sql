delete from account_club;
alter table account_club auto_increment = 1;

delete from interest_categories;
alter table interest_categories auto_increment = 1;

delete from pet_tag;
alter table pet_tag auto_increment = 1;

delete from pet;
alter table pet auto_increment = 1;

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
insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlMSIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2NTUxOTExMDMsImlhdCI6MTY1NDU4NjMwMywiYXV0aCI6IlVTRVIifQ.Tzbbr-wgN-ZZubtNjLAijPBJHdGON-PJkLNgejpyNceANk6m3YigBqNiQtAn8BUzgsiyn1iTRBZj7G7RzTAlFQ', 'KAKAO', 'unique1');
insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlMiIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2NTUxOTExMzEsImlhdCI6MTY1NDU4NjMzMSwiYXV0aCI6IlVTRVIifQ._R5djBprrbqZMpqxv7RZVHO_gLFGlyhVNYeRa5Nbfv2YkQENZ71-mjmIlsM0V8NItMs4VsC77QBIuB-ye2Kq2A', 'APPLE', 'unique2');
insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlMyIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2NTUxOTExNDcsImlhdCI6MTY1NDU4NjM0NywiYXV0aCI6IlVTRVIifQ.6iUkhRoI9l2lOsKiMc6CxLylzjWdMpnQB_RyihL4drwVpfGWLlyESLB9UC44V5diQ5vBNkSA-NZPCcLkpYJ3fw', 'KAKAO', 'unique3');
insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlNCIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2NTUzODcyMTAsImlhdCI6MTY1NDc4MjQxMCwiYXV0aCI6IlVTRVIifQ.LPcC3t4W6LF6cRed1YbcjjbY5PGk1nEkxPrXhG6vJyjOJSdWIIJHTadTl4UFjZdAKZ_5Ja3idgqi6Wn6f7C5og', 'KAKAO', 'unique4');

insert into account_image (name, origin_name, s3key, path) values ('1655044614407_cat.jpg', 'cat.jpg', '디렉터리1+파일명1', 'https://togaether.s3.ap-northeast-2.amazonaws.com/account/1655044614407_cat.jpg');
insert into account_image (name, origin_name, s3key, path) values ('486421_이미지2', '이미지2', '디렉터리2+파일명2', 'https://vdfkopvd.com');
insert into account_image (name, origin_name, s3key, path) values ('486421_이미지3', '이미지3', '디렉터리3+파일명3', 'https://daskodpas.com');

insert into account (age, nickname, sex, token_id, city, detail, self_introduction, account_image_id)
values (10, '재롱잔치', 'MAN', 1, '인천광역시', '남동구', '저는 재롱이 견주입니다.', 1);

insert into account (age, nickname, sex, token_id, city, detail, account_image_id)
values (20, '밀란이네 시트콤', 'WOMAN', 2, '서울시', '강남구', 2);

insert into account (age, nickname, sex, token_id, city, detail, self_introduction, account_image_id)
values (30, 'yapp', 'PRIVATE', 3, '서울시', '강동구', 'yapp 견주입니다~', 3);

insert into account (age, nickname, sex, token_id, city, detail)
values (40, 'abcd', 'MAN', 4, '서울시', '강서구');

insert into interest_categories (account_id, interest_categories) values (1, 'WALK');
insert into interest_categories (account_id, interest_categories) values (1, 'DOG_CAFE');
insert into interest_categories (account_id, interest_categories) values (1, 'PLAY_GROUND');
insert into interest_categories (account_id, interest_categories) values (2, 'EXPOSITION');
insert into interest_categories (account_id, interest_categories) values (2, 'DOG_FRIENDLY_RESTAURANT');
insert into interest_categories (account_id, interest_categories) values (3, 'ETC');

-- 국립중앙박물관 위도 경도
insert into club (category, description, title, meeting_place, status, maximum_people, eligible_sex, start_date, end_date, latitude, longitude)
values ('WALK', 'description', '쿄쿄량 산책할사람', 'place', 'AVAILABLE', 2, 'MAN', '2021-01-01', '2021-01-02', 37.523717, 126.981598);

-- 중앙대학교병원 위도 경도
insert into club (category, description, eligible_sex, end_date, latitude, longitude, maximum_people, meeting_place, start_date, status, title)
values ('DOG_CAFE', 'description', 'WOMAN', '2022-02-05', 37.506706, 126.961290, 3, 'ㅁㄴㅇ', '2022-02-01', 'AVAILABLE', '산책할사람 코코랑');

-- 동작대교
insert into club (category, description, eligible_sex, end_date, latitude, longitude, maximum_people, meeting_place, start_date, status, title)
values ('PLAY_GROUND', 'description', 'ALL', '2022-01-05', 37.510033, 126.981571, 3, 'ㅁㄴㅇ', '2022-01-01', 'AVAILABLE', '코코랑 산책');

insert into account_club (leader, account_id, club_id) values (true, 1, 1);
insert into account_club (leader, account_id, club_id) values (false , 2, 1);
insert into account_club (leader, account_id, club_id) values (true, 3, 2);
insert into account_club (leader, account_id, club_id) values (true, 4, 3);

insert into eligible_pet_breeds (club_id, eligible_breeds) values (1, '말티즈');
insert into eligible_pet_breeds (club_id, eligible_breeds) values (1, '리트리버');

insert into eligible_pet_size_types (club_id, eligible_pet_size_types) values (1, 'LARGE');
insert into eligible_pet_size_types (club_id, eligible_pet_size_types) values (1, 'MEDIUM');

insert into pet_image (name, origin_name, path) values ('90419_펫이미지1', '펫이미지1', 'https://acjic.com');

insert into pet (age, birth_month, birth_year, breed, name, neutering, sex, size_type, account_id, pet_image_id)
values (5, '2', '2', '말티즈', '재롱이', true, 'MALE', 'SMALL', 1, 1);
insert into pet (age, birth_month, birth_year, breed, name, neutering, sex, size_type, account_id)
values (4, '1', '1', '리트리버', '밀란', true, 'MALE', 'SMALL', 1);

insert into pet_tag (name, pet_id) values ('tag1', 1);
insert into pet_tag (name, pet_id) values ('tag2', 1);
insert into pet_tag (name, pet_id) values ('tag3', 1);
insert into pet_tag (name, pet_id) values ('tag4', 2);
insert into pet_tag (name, pet_id) values ('tag5', 2);