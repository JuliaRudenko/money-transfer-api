
CREATE TABLE IF NOT EXISTS user(
  id BIGINT primary key,
  userName VARCHAR(255),
  email VARCHAR(255),
  createdAt TIMESTAMP
);

CREATE TABLE IF NOT EXISTS account(
  id BIGINT primary key,
  userId BIGINT,
  balance DECIMAL,
  currency VARCHAR(30),
  createdAt TIMESTAMP,
  foreign key (userId) references user(id)
);

CREATE TABLE IF NOT EXISTS transfer(
  id BIGINT primary key,
  sourceAccountId BIGINT,
  destinationAccountId BIGINT,
  sum DECIMAL,
  currency VARCHAR(30),
  createdAt TIMESTAMP,
  status VARCHAR(30),
  foreign key (sourceAccountId) references account(id),
  foreign key (destinationAccountId) references account(id)
);

-- fill tables with data
INSERT INTO user(id, userName, email, createdAt) VALUES (1, 'Julia Rudenko', 'juli@gmail.com', '2019-05-01 17:00:00');
INSERT INTO user(id, userName, email, createdAt) VALUES (2, 'John Snow', 'john@gmail.com', '2019-04-01 10:00:00');

INSERT INTO account(id, userId, balance, currency, createdAt) VALUES (1, 1, 100, 'UAH', '2019-05-17 17:00:00');
INSERT INTO account(id, userId, balance, currency, createdAt) VALUES (2, 2, 200, 'UAH', '2019-05-07 07:00:00');

INSERT INTO transfer(id, sourceAccountId, destinationAccountId, sum, currency, createdAt, status)
VALUES (1, 1, 2, 100, 'UAH', '2019-05-17 17:00:00', 'EXECUTED');