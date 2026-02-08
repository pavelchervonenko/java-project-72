package hexlet.code.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.sql.Timestamp;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hexlet.code.model.UrlCheck;

public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck check) throws Exception {
        String sql = "INSERT INTO url_checks (url_id, status_code, title, h1, description)"
                     + "VALUES (?, ?, ?, ?, ?)";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


            stmt.setLong(1, check.getUrlId());
            if (check.getStatusCode() == null) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, check.getStatusCode());
            }
            stmt.setString(3, check.getTitle());
            stmt.setString(4, check.getH1());
            stmt.setString(5, check.getDescription());
            stmt.executeUpdate();

            try (var generatedKeys = stmt.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    check.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving an entity");
                }
            }
        }
    }

    public static List<UrlCheck> findByUrlId(Long urlId) throws Exception {
        String sql = "SELECT * "
                     + "FROM url_checks "
                     + "WHERE url_id = ? "
                     + "ORDER BY created_at DESC, id DESC";

        var result = new ArrayList<UrlCheck>();

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, urlId);
            try (var resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    result.add(map(resultSet));
                }
            }
        }

        return result;
    }

    public static Optional<UrlCheck> findLastByUrlId(Long urlId) throws Exception {
        String sql = "SELECT * "
                + "FROM url_checks "
                + "WHERE url_id = ? "
                + "ORDER BY created_at DESC, id DESC "
                + "LIMIT 1";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, urlId);
            try (var resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(map(resultSet));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    public static Map<Long, UrlCheck> findLatestChecks() throws Exception {
        String sql = "SELECT url_checks.* "
                + "FROM url_checks "
                + "JOIN ( "
                + "  SELECT url_id, MAX(id) AS max_id "
                + "  FROM url_checks "
                + "  GROUP BY url_id "
                + ") latest "
                + "ON latest.max_id = url_checks.id ";

        var result = new HashMap<Long, UrlCheck>();

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            try (var resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    var check = map(resultSet);
                    result.put(check.getUrlId(), check);
                }
            }
        }

        return result;
    }

    private static UrlCheck map(ResultSet resultSet) throws Exception {
        var urlCheck = new UrlCheck();

        urlCheck.setId(resultSet.getLong("id"));
        urlCheck.setUrlId(resultSet.getLong("url_id"));

        int sc = resultSet.getInt("status_code");
        if (resultSet.wasNull()) {
            urlCheck.setStatusCode(null);
        } else {
            urlCheck.setStatusCode(sc);
        }

        urlCheck.setTitle(resultSet.getString("title"));
        urlCheck.setH1(resultSet.getString("h1"));
        urlCheck.setDescription(resultSet.getString("description"));

        Timestamp ts = resultSet.getTimestamp("created_at");
        if (ts != null) {
            urlCheck.setCreatedAt(ts.toLocalDateTime());
        } else {
            urlCheck.setCreatedAt(LocalDateTime.now());
        }

        return urlCheck;
    }
}
