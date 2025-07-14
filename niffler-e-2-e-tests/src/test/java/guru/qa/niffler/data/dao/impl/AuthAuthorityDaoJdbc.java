package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Alexander
 */
public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthorityEntity create(AuthorityEntity authorityEntity) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO authority (user_id, authority) " +
                        "VALUES (?, ?);",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setObject(1, authorityEntity.getUser().getId());
            ps.setString(2, authorityEntity.getAuthority().name());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            authorityEntity.setId(generatedKey);
            return authorityEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorityEntity> findAuthoritiesByUserId(UUID userId) {
        List<AuthorityEntity> userAuthorities = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM authority WHERE user_id = ? ;"
        )) {
            ps.setObject(1, userId);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    AuthorityEntity ue = new AuthorityEntity();
                    ue.setId(rs.getObject("id", UUID.class));
                    ue.setAuthority(Authority.valueOf(rs.getString("authority")));
                    userAuthorities.add(ue);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userAuthorities;
    }

    @Override
    public void deleteAuthority(AuthorityEntity authorityEntity) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM authority WHERE id = ?;"
        )) {
            ps.setObject(1, authorityEntity.getId());

            int rowsDeleted = ps.executeUpdate();

            if (rowsDeleted < 0) {
                throw new SQLException(String.format("Can`t delete authority [%s] because it's not found", authorityEntity));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllAuthoritiesByUserId(UUID userId) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM authority WHERE user_id = ?;"
        )) {
            ps.setObject(1, userId);

            int rowsDeleted = ps.executeUpdate();

            if (rowsDeleted < 0) {
                throw new SQLException(String.format("Can`t delete authority bu user_id [%s] because it's not found", userId));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
