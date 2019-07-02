MERGE INTO ROLE (name) VALUES ('ROLE_ADMIN'),
                              ('ROLE_USER');

-- DEBUG DATA
MERGE INTO Department (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED, NAME)
values (100000001, 1561462935000, null, 1561462935000, 'Исполнители');
MERGE INTO Department (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED, NAME)
values (100000002, 1561462935000, null, 1561462935000, 'Заказчик');
MERGE INTO ADDRESSEE (id, WHEN_CREATED, WHEN_UPDATED, EMAIL, NAME, DEPARTMENT_ID)
values (100000001, 1561462935000, 1561462935000, 'galina.kac@simbirsoft.com', 'Галина', 100000002);
MERGE INTO ADDRESSEE (id, WHEN_CREATED, WHEN_UPDATED, EMAIL, NAME, DEPARTMENT_ID)
values (100000002, 1561462935000, 1561462935000, 'v555574@ya.ru', 'Влад', 100000001);
MERGE INTO ADDRESSEE (id, WHEN_CREATED, WHEN_UPDATED, EMAIL, NAME, DEPARTMENT_ID)
values (100000003, 1561462935000, 1561462935000, 'pavel.asdlevel@gmail.com', 'Павел', 100000001);
MERGE INTO SENDER (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED, EMAIL, PASSWORD, EMAIL_PASSWORD, PORT, SMTP, ROLE_NAME, CONNECTION_OK)
VALUES (100000001, 1561462935000, null, 1561462935000, 'itplace2019-g@ya.ru', '$2a$10$hIbac0gLfteSkp1G2T/aCe1lmu9n7aqfUHd4hu1TudccHTu9l8BWG', 'somepassW0rd', 465, 'smtp.yandex.ru', 'ROLE_USER', 1);
