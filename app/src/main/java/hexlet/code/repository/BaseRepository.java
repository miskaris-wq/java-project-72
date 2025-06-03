package hexlet.code.repository;

import javax.sql.DataSource;

public abstract class BaseRepository {
    protected final DataSource dataSource;

    protected BaseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
