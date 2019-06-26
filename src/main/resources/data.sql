
INSERT INTO ROLE(name) VALUES('ADMIN'), ('USER');

-- DEBUG DATA

INSERT INTO Department (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED, NAME)
values (1, 1561462935000, null, 1561462935000, 'Маркетологи');
INSERT INTO Department (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED, NAME)
values (2, 1561462935000, null, 1561462935000, 'Покупатель');
INSERT INTO ADDRESSEE (id, WHEN_CREATED, WHEN_UPDATED, EMAIL, LOCALE, NAME, DEPARTMENT_ID)
values (1, 1561462935000, 1561462935000, 'galina.kac@simbirsoft.com', 'RU', 'Галина', 2);
INSERT INTO ADDRESSEE (id, WHEN_CREATED, WHEN_UPDATED, EMAIL, LOCALE, NAME, DEPARTMENT_ID)
values (2, 1561462935000, 1561462935000, 'v555574@ya.ru', 'RU', 'Влад', 1);
INSERT INTO ADDRESSEE (id, WHEN_CREATED, WHEN_UPDATED, EMAIL, LOCALE, NAME, DEPARTMENT_ID)
values (3, 1561462935000, 1561462935000, 'pavel.asdlevel@gmail.com', 'RU', 'Герасимчик Павел Анатольевич', 1);

INSERT INTO LOCALIZED_STRING (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED, LOCALE, VALUE)
values (1, 1561462935000, null, 1561462935000, 'RU',
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Tortor condimentum lacinia quis vel eros. Ornare massa eget egestas purus viverra accumsan in. Vitae tempus quam pellentesque nec nam. Arcu dictum varius duis at. Et tortor at risus viverra adipiscing. Vestibulum lorem sed risus ultricies tristique nulla aliquet enim. Ornare arcu dui vivamus arcu felis bibendum ut tristique. Nulla aliquet porttitor lacus luctus accumsan tortor posuere. Viverra justo nec ultrices dui sapien eget mi. Morbi tristique senectus et netus et malesuada.');
INSERT INTO LOCALIZED_STRING (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED, LOCALE, VALUE)
values (2, 1561462935000, null, 1561462935000, 'EN',
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Tortor condimentum lacinia quis vel eros. Ornare massa eget egestas purus viverra accumsan in. Vitae tempus quam pellentesque nec nam. Arcu dictum varius duis at. Et tortor at risus viverra adipiscing. Vestibulum lorem sed risus ultricies tristique nulla aliquet enim. Ornare arcu dui vivamus arcu felis bibendum ut tristique. Nulla aliquet porttitor lacus luctus accumsan tortor posuere. Viverra justo nec ultrices dui sapien eget mi. Morbi tristique senectus et netus et malesuada.');
INSERT INTO MESSAGE (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED)
VALUES (1, 1561462935000, null, 1561462935000);
INSERT INTO MESSAGE_TEXT (MESSAGE_ID, TEXT_ID)
VALUES (1, 1);
INSERT INTO MESSAGE_TEXT (MESSAGE_ID, TEXT_ID)
VALUES (1, 2);
INSERT INTO SENDER (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED, EMAIL, PASSWORD, PORT, SMTP, ROLE_NAME)
VALUES (1, 1561462935000, null, 1561462935000, 'pavel.asdlevel@gmail.com', 'repytxbr', '22', 'smtp.gmail.com', 'ADMIN');
INSERT INTO SENDER (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED, EMAIL, PASSWORD, PORT, SMTP, ROLE_NAME)
VALUES (2, 1561462935000, null, 1561462935000, 'some@gmail.com', 'pass', '22', 'smtp.gmail.com', 'USER');
INSERT INTO MAIL (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED, STATUS, SUBJECT, MESSAGE_ID, SENDER_ID)
VALUES (1, 1561462935000, null, 1561462935000, 'NEW', 'FIRST MAIL', 1, 1);
INSERT INTO MAIL_ADDRESSEE (MAIL_ID, ADDRESSEE_ID)
VALUES (1, 1);
INSERT INTO MAIL_ADDRESSEE (MAIL_ID, ADDRESSEE_ID)
VALUES (1, 2);
INSERT INTO MAIL_ADDRESSEE (MAIL_ID, ADDRESSEE_ID)
VALUES (1, 3);
