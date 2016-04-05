
                                                 Задание:
Написать веб-сервис, который реализует следующие методы:

1. String getCreate(String schemaName, String tableName) – возвращает DDL на создание указанной существующей в БД таблицы. Скрипт должен содержать DDL создания первичного ключа.

2. String getSelect(String schemaName, String tableName) – возвращает запрос вида SELECT <список полей через запятую> FROM <указанная таблица> WHERE <поле-первичный ключ> = ? (либо писок таких полей в случае составного ПК)

3. String getUpdate(String schemaName, String tableName) – возвращает запрос вида UPDATE <указанная таблица> SET <поле1>=?, <поле2>=?..... WHERE <поле-первичный ключ >=? (либо писок таких полей в случае составного ПК))

Считается, что у пользователя СУБД имеются необходимые права.



                                                  Решение:
1. Импортируйте проект в IDE (Я использовал Eclipse в сборке SpringSource Tool Suite (http://spring.io/tools))
2. В случае желания измените логин, пароль в TestTaskSpringMVCHibernate\src\main\webapp\WEB-INF\spring\appServlet\servlet-context.xml
3. В случае отсуствия такого желания создайте пользователя с логином - gorbunov, паролем -123456. (Можете воспользоваться положенным в корневой каталог скриптом sql, для этого после авторизации в MySQL с правами администратора введите "source /*путь до скрипта sql*/", например "source D:\rest.sql") 
4. В корневом каталоге приложения с помощью maven выполните "mvn clean compile","mvn install"
5. Запустите проект на tomcat server (я использовал его, но, думаю, можно использовать другие)
6. Web-service доступен по адресам
- http://localhost:8080/SpringMVCHibernate-REST/ws/getCreate/{schemaName}/{tableName}
- http://localhost:8080/SpringMVCHibernate-REST/ws/getSelect/{schemaName}/{tableName}
- http://localhost:8080/SpringMVCHibernate-REST/ws/getUpdate/{schemaName}/{tableName}

7.Базовую аутентификацию (реализованную через Spring Security) проходят следующие учетные записи:
- user name="admin" password="pass" 
- user name="user1" password="1111" 
