package org.alcibiade.pandiscovery.db;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Sample table creation utility.
 */
public class TableGenerator {

    public static void createTable(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("create table t1 (ix varchar(32) primary key, value varchar(42))");
        jdbcTemplate.execute("insert into t1 values ('key1', 'value 1')");
        jdbcTemplate.execute("insert into t1 values ('key2', 'value 2')");
        jdbcTemplate.execute("insert into t1 values ('key3', 'value 3')");
        jdbcTemplate.execute("insert into t1 values ('key4', 'value 4')");

        jdbcTemplate.execute("insert into t1 values ('Card Visa 1', '4111111111111111')");
        jdbcTemplate.execute("insert into t1 values ('Card Visa 2', '4012888888881881')");
        jdbcTemplate.execute("insert into t1 values ('Card Visa 3', '4111111111111111')");
        jdbcTemplate.execute("insert into t1 values ('Card MC 1', '5105105105105100')");
        jdbcTemplate.execute("insert into t1 values ('Card MC 2', '5555555555554444')");
    }


    public static void cleanup(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("drop table t1");
    }
}
