FROM mariadb:latest

ADD program_guide.sql /docker-entrypoint-initdb.d/0.sql
ADD 1.5.0_updates.sql /docker-entrypoint-initdb.d/1.sql

ENV MYSQL_ROOT_PASSWORD root
ENV MYSQL_DATABASE program_guide
ENV MYSQL_USER pg
ENV MYSQL_PASSWORD pg

EXPOSE 3306