package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;


public class V2__create_users_table extends BaseJavaMigration {

    @Override
    public void migrate(Context context) {
        new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true))
                .execute("""
            CREATE TABLE users (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(255),
                        email VARCHAR(255),
                        password VARCHAR(255)
            )
""");

    }
}