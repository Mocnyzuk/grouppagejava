INSERT INTO user (email, phone, password, is_activated, is_deleted, reset_password_token, authorities, last_online)
VALUES ('fpmoles@fpmoles.pl', 123123123, '$2a$11$dp4wMyuqYE3KSwIyQmWZFeUb7jCsHAdk7ZhFc0qGw6i5J124imQBi',
true, false, '', 'ROLE_ADMIN;ROLE_USER;', current_timestamp);