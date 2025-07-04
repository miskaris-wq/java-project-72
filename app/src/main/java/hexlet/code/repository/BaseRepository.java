package hexlet.code.repository;

import javax.sql.DataSource;

public abstract class BaseRepository {
    public static DataSource dataSource; // устанавливается в App

    protected final DataSource ds;

    protected BaseRepository() {
        this.ds = dataSource;
    }

    protected BaseRepository(DataSource dataSource) {
        this.ds = dataSource;
    }
}
