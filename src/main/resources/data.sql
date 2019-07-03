INSERT INTO "public"."role" ("name") VALUES ('ROLE_ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO "public"."role" ("name") VALUES ('ROLE_USER') ON CONFLICT DO NOTHING;
INSERT INTO "public"."department" ("id", "when_created", "when_deleted", "when_updated", "name")
VALUES (100000001, 1561462935000, NULL, 1561462935000, 'Исполнители') ON CONFLICT DO NOTHING;
INSERT INTO "public"."department" ("id", "when_created", "when_deleted", "when_updated", "name") 
VALUES (100000002, 1561462935000, NULL, 1561462935000, 'Заказчик') ON CONFLICT DO NOTHING;
INSERT INTO "public"."addressee" ("id", "when_created", "when_deleted", "when_updated", "email", "name", "department_id") 
VALUES (100000001, 1561462935000, NULL, 561462935000, 'galina.kac@simbirsoft.com', 'Галина', 100000002) ON CONFLICT DO NOTHING;
INSERT INTO "public"."addressee" ("id", "when_created", "when_deleted", "when_updated", "email", "name", "department_id") 
VALUES (100000002, 1561462935000, NULL, 561462935000, 'v555574@ya.ru', 'Влад', 100000001) ON CONFLICT DO NOTHING;
INSERT INTO "public"."addressee" ("id", "when_created", "when_deleted", "when_updated", "email", "name", "department_id") 
VALUES (100000003, 1561462935000, NULL, 561462935000, 'pavel.asdlevel@gmail.com', 'Павел', 100000001) ON CONFLICT DO NOTHING;
INSERT INTO "public"."sender" ("id", "when_created", "when_deleted", "when_updated", "email", "email_password", "password", "port", "smtp", "role_name")
VALUES (100000001, 1561462935000, NULL, 1561462935000, 'itplace2019-g@ya.ru', 'somepassW0rd', '$2a$10$hIbac0gLfteSkp1G2T/aCe1lmu9n7aqfUHd4hu1TudccHTu9l8BWG', 465, 'smtp.yandex.ru', 'ROLE_ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO "public"."sender" ("id", "when_created", "when_deleted", "when_updated", "email", "email_password", "password", "port", "smtp", "role_name")
VALUES (100000002, 1561462935000, NULL, 1561462935000, 'itplace2019-g-sub@ya.ru', '1qaz2wsx123', '$2a$10$hIbac0gLfteSkp1G2T/aCe1lmu9n7aqfUHd4hu1TudccHTu9l8BWG', 465, 'smtp.yandex.ru', 'ROLE_ADMIN') ON CONFLICT DO NOTHING;
