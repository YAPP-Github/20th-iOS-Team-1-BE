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

insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlMSIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2NTUxOTExMDMsImlhdCI6MTY1NDU4NjMwMywiYXV0aCI6IlVTRVIifQ.Tzbbr-wgN-ZZubtNjLAijPBJHdGON-PJkLNgejpyNceANk6m3YigBqNiQtAn8BUzgsiyn1iTRBZj7G7RzTAlFQ', 'KAKAO', 'unique1');
insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlMiIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2NTUxOTExMzEsImlhdCI6MTY1NDU4NjMzMSwiYXV0aCI6IlVTRVIifQ._R5djBprrbqZMpqxv7RZVHO_gLFGlyhVNYeRa5Nbfv2YkQENZ71-mjmIlsM0V8NItMs4VsC77QBIuB-ye2Kq2A', 'APPLE', 'unique2');
insert into token (refresh_token, social_type, unique_id_by_social) values ('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ5YXBwIiwic3ViIjoidW5pcXVlMyIsImF1ZCI6IlJFRlJFU0giLCJleHAiOjE2NTUxOTExNDcsImlhdCI6MTY1NDU4NjM0NywiYXV0aCI6IlVTRVIifQ.6iUkhRoI9l2lOsKiMc6CxLylzjWdMpnQB_RyihL4drwVpfGWLlyESLB9UC44V5diQ5vBNkSA-NZPCcLkpYJ3fw', 'KAKAO', 'unique3');

insert into account (age, nickname, sex, token_id) values (10, '재롱잔치', 'MAN', 1);
insert into account (age, nickname, sex, token_id) values (20, '밀란이네 시트콤', 'WOMAN', 2);
insert into account (age, nickname, sex, token_id) values (30, 'yapp', 'ALL', 3);

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