#!/bin/bash

java \
    -cp ~/Dropbox/JDBC/PostgreSQL/postgresql-9.4.1212.jar \
    -Dspring.datasource.url=jdbc:postgresql://database:5432/chess1 \
    -Dspring.datasource.username=chess \
    -Dspring.datasource.password=chess \
    -jar target/pan-discovery-db-1.0.0-SNAPSHOT.jar

