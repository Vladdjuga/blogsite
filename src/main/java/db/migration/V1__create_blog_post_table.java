package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;


public class V1__create_blog_post_table extends BaseJavaMigration {

    @Override
    public void migrate(Context context) {
        JdbcTemplate jdbc = new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));
        jdbc.execute("CREATE SEQUENCE IF NOT EXISTS blog_posts_seq START WITH 1 INCREMENT BY 1");
        jdbc.execute("""
            CREATE TABLE IF NOT EXISTS blog_posts (
                id BIGINT PRIMARY KEY DEFAULT nextval('blog_posts_seq'),
                title VARCHAR(255) NOT NULL,
                content TEXT,
                author VARCHAR(255),
                created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
            )
        """);
    }
}