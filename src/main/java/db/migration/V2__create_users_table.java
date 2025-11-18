package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;


public class V2__create_users_table extends BaseJavaMigration {

    @Override
    public void migrate(Context context) {
        JdbcTemplate jdbc = new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));
        jdbc.execute("CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 1");
        jdbc.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT PRIMARY KEY DEFAULT nextval('users_seq'),
                username VARCHAR(255) NOT NULL UNIQUE,
                email VARCHAR(255) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL
            )
        """);
    }
}