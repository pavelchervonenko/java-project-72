package hexlet.code.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import hexlet.code.model.Url;

public class UrlRepository extends BaseRepository {
    public static void save(Url url) throws Exception {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            url.setCreatedAt(LocalDateTime.now());

            stmt.setString(1, url.getName());
            stmt.setTimestamp(2, Timestamp.valueOf(url.getCreatedAt()));
            stmt.executeUpdate();

            try (var generatedKeys = stmt.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    url.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving an entity");
                }
            }
        }
    }

    public static Optional<Url> findById(Long id) throws Exception {
        String sql = "SELECT * FROM urls WHERE id = ?";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (var resultSet = stmt.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(map(resultSet));
                }
                return Optional.empty();
            }
        }
    }

    public static Optional<Url> findByName(String name) throws Exception {
        String sql = "SELECT * FROM urls WHERE name = ?";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            try (var resultSet = stmt.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(map(resultSet));
                }
                return Optional.empty();
            }
        }
    }

    public static List<Url> getEntities() throws Exception {
        String sql = "SELECT * FROM urls";
        List<Url> result = new ArrayList<>();

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql);
             var resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                result.add(map(resultSet));
            }
        }
        return result;
    }

    private static Url map(ResultSet resultSet) throws Exception {
        var url = new Url();

        url.setId(resultSet.getLong("id"));
        url.setName(resultSet.getString("name"));

        Timestamp ts = resultSet.getTimestamp("created_at");
        if (ts != null) {
            url.setCreatedAt(ts.toLocalDateTime());
        } else {
            url.setCreatedAt(null);
        }

        return url;
    }
}
