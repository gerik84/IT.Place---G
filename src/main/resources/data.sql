
INSERT INTO ROLE(name) VALUES('ADMIN'), ('USER');

-- DEBUG DATA

INSERT INTO Department (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED, NAME) values ('b85cdb1958df4743a5a0df9e48308e1b', 1561462935000, null, 1561462935000, 'Маркетологи');
INSERT INTO ADDRESSEE (id, WHEN_CREATED, WHEN_UPDATED, EMAIL, LOCALE, NAME, DEPARTMENT_ID) values ('41fe806b36014edfb393029169079d92', 1561462935000, 1561462935000, 'pavel.asdlevel@gmail.com', 'RU', 'Герасимчик Павел Анатольевич', 'b85cdb1958df4743a5a0df9e48308e1b');
INSERT INTO LOCALIZED_STRING (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED, LOCALE, VALUE) values ('6cc9002492c74fc48cc63c07ccb8cf59', 1561462935000, null, 1561462935000, 'RU', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Tortor condimentum lacinia quis vel eros. Ornare massa eget egestas purus viverra accumsan in. Vitae tempus quam pellentesque nec nam. Arcu dictum varius duis at. Et tortor at risus viverra adipiscing. Vestibulum lorem sed risus ultricies tristique nulla aliquet enim. Ornare arcu dui vivamus arcu felis bibendum ut tristique. Nulla aliquet porttitor lacus luctus accumsan tortor posuere. Viverra justo nec ultrices dui sapien eget mi. Morbi tristique senectus et netus et malesuada.');
INSERT INTO LOCALIZED_STRING (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED, LOCALE, VALUE) values ('d9979456266c47208989582ff7fb4ca3', 1561462935000, null, 1561462935000, 'EN', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Tortor condimentum lacinia quis vel eros. Ornare massa eget egestas purus viverra accumsan in. Vitae tempus quam pellentesque nec nam. Arcu dictum varius duis at. Et tortor at risus viverra adipiscing. Vestibulum lorem sed risus ultricies tristique nulla aliquet enim. Ornare arcu dui vivamus arcu felis bibendum ut tristique. Nulla aliquet porttitor lacus luctus accumsan tortor posuere. Viverra justo nec ultrices dui sapien eget mi. Morbi tristique senectus et netus et malesuada.');
INSERT INTO MESSAGE (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED) VALUES ('6b226678ba29439a9218db25f3ace470',  1561462935000, null, 1561462935000);
INSERT INTO MESSAGE_TEXT (MESSAGE_ID, TEXT_ID) VALUES ( '6b226678ba29439a9218db25f3ace470', '6cc9002492c74fc48cc63c07ccb8cf59' );
INSERT INTO MESSAGE_TEXT (MESSAGE_ID, TEXT_ID) VALUES ( '6b226678ba29439a9218db25f3ace470', 'd9979456266c47208989582ff7fb4ca3' );
INSERT INTO SENDER (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED, EMAIL, PASSWORD, PORT, SMTP, ROLE_NAME) VALUES ('8b2fb978257b4499bd5a94cb3bd91b99', 1561462935000, null, 1561462935000, 'pavel.asdlevel@gmail.com', 'repytxbr', '22', 'smtp.gmail.com', 'ADMIN');
INSERT INTO MAIL (ID, WHEN_CREATED, WHEN_DELETED, WHEN_UPDATED, STATUS, SUBJECT, ADDRESSEE_ID, MESSAGE_ID, SENDER_ID) VALUES ('771797b8ea8241d799b4207ff3b455b1', 1561462935000, null, 1561462935000, 'NEW', 'FIRST MAIL', '41fe806b36014edfb393029169079d92', '6b226678ba29439a9218db25f3ace470', '8b2fb978257b4499bd5a94cb3bd91b99');