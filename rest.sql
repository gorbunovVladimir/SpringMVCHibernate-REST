CREATE USER gorbunov@localhost identified BY '123456';
GRANT usage ON *.* TO gorbunov@localhost identified BY '123456';
FLUSH PRIVILEGES;