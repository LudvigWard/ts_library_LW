package se.yrgo.libraryapp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.UserId;

public class RoleDao {
    private static Logger logger = LoggerFactory.getLogger(RoleDao.class);
    private DataSource ds;

    @Inject
    RoleDao(DataSource ds) {
        this.ds = ds;
    }

    public List<Role> get(UserId userId) {
        List<Role> roles = new ArrayList<>();
        String query = "SELECT r.role FROM user_role AS ur JOIN role AS r ON ur.role_id = r.id " +
                "WHERE ur.user_id = ?";

        try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    roles.add(Role.fromString(rs.getString("role")));
                }
            }

            return roles;
        } catch (SQLException ex) {
            logger.error("Unable to get roles for user id", ex);
            return List.of();
        }
    }

}
