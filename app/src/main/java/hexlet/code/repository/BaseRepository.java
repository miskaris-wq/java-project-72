package hexlet.code.repository;
import javax.sql.DataSource;

public abstract class BaseRepository {
    public static DataSource dataSource;

    protected final DataSource ds;

    protected BaseRepository() {
        this.ds = dataSource;
    }

    protected BaseRepository(DataSource dataSource) {
        this.ds = dataSource;
    }
}
