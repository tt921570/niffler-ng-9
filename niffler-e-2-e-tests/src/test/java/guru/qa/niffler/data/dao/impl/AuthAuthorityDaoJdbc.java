package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

/**
 * @author Alexander
 */
public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public void create(AuthorityEntity... authorities) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO authority (user_id, authority) " +
                        "VALUES (?, ?);",
                Statement.RETURN_GENERATED_KEYS
        )) {
            for (AuthorityEntity ae : authorities) {
                ps.setObject(1, ae.getUserId());
                ps.setString(2, ae.getAuthority().name());
                ps.addBatch();
            }

            ps.executeBatch();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                    System.out.println("Generate Authority id: " + generatedKey.toString());
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthorityEntity> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<AuthorityEntity> findAuthoritiesByUserId(UUID userId) {
        List<AuthorityEntity> userAuthorities = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
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
    public List<AuthorityEntity> findAll() {
        List<AuthorityEntity> userAuthorities = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM authority;"
        )) {
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
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
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
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
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
