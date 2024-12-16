package se.yrgo.libraryapp.dao;

import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.pac4j.core.exception.CredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.yrgo.libraryapp.entities.UserId;

public class SessionDao {
    private static Logger logger = LoggerFactory.getLogger(SessionDao.class);
    private static final Duration SESSION_EXPIRATION = Duration.ofDays(7);

    private DataSource ds;

    @Inject
    SessionDao(DataSource ds) {
        this.ds = ds;
    }

    public UUID create(UserId userId) {
        String query = "INSERT INTO session (id, user_id, created) VALUES (?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            UUID uuid = UUID.randomUUID();
            pstmt.setString(1, uuid.toString());
            pstmt.setInt(2, userId.getId());

            pstmt.executeUpdate();
            return uuid;
        } catch (SQLException ex) {
            throw new CredentialsException("Unable to create session", ex);
        }
    }


    public void delete(UUID session) {
        String query = "DELETE FROM session WHERE id = ?";
        try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, session.toString());

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error("Unable to delete session", ex);
        }
    }


    public UserId validate(UUID session) {
        String query = "SELECT user_id, created FROM session WHERE id = ?";
        try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, session.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    Timestamp timestamp = rs.getTimestamp("created");
                    validateExpiration(timestamp);
                    return UserId.of(userId);
                }
            }
        } catch (SQLException ex) {
            throw new CredentialsException("Unable to validate session", ex);
        }

        throw new CredentialsException("Unable to validate session");
    }


    private void validateExpiration(Timestamp timestamp) {
        Instant sessionCreated = timestamp.toInstant();
        Instant now = Instant.now();
        if (sessionCreated.plus(SESSION_EXPIRATION).isBefore(now)) {
            throw new CredentialsException("Session has expired");
        }
    }
}
