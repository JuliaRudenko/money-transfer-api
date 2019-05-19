DROP TABLE IF EXISTS transfer;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS user;

CREATE TABLE IF NOT EXISTS user(
  id BIGINT AUTO_INCREMENT primary key,
  userName VARCHAR(255),
  email VARCHAR(255),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS account(
  id BIGINT AUTO_INCREMENT primary key,
  userId BIGINT,
  balance DECIMAL,
  currency VARCHAR(30),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  foreign key (userId) references user(id)
);

CREATE TABLE IF NOT EXISTS transfer(
  id BIGINT AUTO_INCREMENT primary key,
  sourceAccountId BIGINT,
  destinationAccountId BIGINT,
  sum DECIMAL,
  currency VARCHAR(30),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  foreign key (sourceAccountId) references account(id),
  foreign key (destinationAccountId) references account(id)
);

DELETE FROM transfer;
DELETE FROM account;
DELETE FROM user;

-- fill tables with data
INSERT INTO user(id, userName, email, createdAt) VALUES (1, 'Eddard Stark', 'eddard@gmail.com', '2019-05-01 17:00:00');
INSERT INTO user(id, userName, email, createdAt) VALUES (2, 'Arya Stark', 'aria@gmail.com', '2019-04-01 10:00:00');
INSERT INTO user(id, userName, email, createdAt) VALUES (3, 'Sansa Stark', 'sansa@gmail.com', '2019-03-01 14:00:00');

INSERT INTO account(id, userId, balance, currency, createdAt) VALUES (1, 1, 100500, 'UAH', '2019-05-17 17:00:00');
INSERT INTO account(id, userId, balance, currency, createdAt) VALUES (2, 2, 2000, 'UAH', '2019-05-07 07:00:00');
INSERT INTO account(id, userId, balance, currency, createdAt) VALUES (3, 1, 50, 'UAH', '2019-10-08 08:00:00');

INSERT INTO transfer(id, sourceAccountId, destinationAccountId, sum, currency, createdAt)
VALUES (1, 1, 2, 500, 'UAH', '2019-05-17 17:00:00');
INSERT INTO transfer(id, sourceAccountId, destinationAccountId, sum, currency, createdAt)
VALUES (2, 2, 1, 150, 'UAH', '2019-05-19 19:00:00');
INSERT INTO transfer(id, sourceAccountId, destinationAccountId, sum, currency, createdAt)
VALUES (3, 1, 2, 200, 'UAH', '2019-05-20 02:00:00');