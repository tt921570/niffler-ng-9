package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

/**
 * @author Alexander
 */
public class AuthUserDaoJdbc implements AuthUserDao {

    private static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private static final Config CFG = Config.getInstance();

    @Override
    public AuthUserEntity create(AuthUserEntity authUserEntity) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, \"password\", enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES ( ?, ?, ?, ?, ?, ?);",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, authUserEntity.getUsername());
            ps.setString(2, PASSWORD_ENCODER.encode(authUserEntity.getPassword()));
            ps.setBoolean(3, authUserEntity.getEnabled());
            ps.setBoolean(4, authUserEntity.getAccountNonExpired());
            ps.setBoolean(5, authUserEntity.getAccountNonLocked());
            ps.setBoolean(6, authUserEntity.getCredentialsNonExpired());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            authUserEntity.setId(generatedKey);
            return authUserEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findUserByUserName(String username) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE username = ?;"
        )) {
            ps.setObject(1, username);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    AuthUserEntity ue = new AuthUserEntity();
                    ue.setId(rs.getObject("id", UUID.class));
                    ue.setUsername(rs.getString("username"));
                    ue.setEnabled(rs.getBoolean("enabled"));
                    ue.setAccountNonExpired(rs.getBoolean("accountNonExpired"));
                    ue.setAccountNonLocked(rs.getBoolean("accountNonLocked"));
                    ue.setCredentialsNonExpired(rs.getBoolean("credentialsNonExpired"));
                    return Optional.of(ue);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthUserEntity> findUsersByActiveProperties(boolean isEnabled, boolean isAccountNonExpired,
                                                            boolean isAccountNonLocked, boolean isCredentialsNonExpired) {
        List<AuthUserEntity> userEntities = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE enabled = ? AND accountNonExpired = ? " +
                        "AND accountNonLocked = ? AND credentialsNonExpired = ?;"
        )) {
            ps.setBoolean(1, isEnabled);
            ps.setBoolean(2, isAccountNonExpired);
            ps.setBoolean(3, isAccountNonLocked);
            ps.setBoolean(4, isCredentialsNonExpired);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    AuthUserEntity ue = new AuthUserEntity();
                    ue.setId(rs.getObject("id", UUID.class));
                    ue.setUsername(rs.getString("username"));
                    ue.setEnabled(rs.getBoolean("enabled"));
                    ue.setAccountNonExpired(rs.getBoolean("accountNonExpired"));
                    ue.setAccountNonLocked(rs.getBoolean("accountNonLocked"));
                    ue.setCredentialsNonExpired(rs.getBoolean("credentialsNonExpired"));
                    userEntities.add(ue);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userEntities;
    }

    @Override
    public List<AuthUserEntity> findAll() {
        List<AuthUserEntity> userEntities = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\";"
        )) {
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    AuthUserEntity ue = new AuthUserEntity();
                    ue.setId(rs.getObject("id", UUID.class));
                    ue.setUsername(rs.getString("username"));
                    ue.setEnabled(rs.getBoolean("enabled"));
                    ue.setAccountNonExpired(rs.getBoolean("accountNonExpired"));
                    ue.setAccountNonLocked(rs.getBoolean("accountNonLocked"));
                    ue.setCredentialsNonExpired(rs.getBoolean("credentialsNonExpired"));
                    userEntities.add(ue);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userEntities;
    }

    @Override
    public void deleteUser(AuthUserEntity authUserEntity) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "DELETE FROM \"user\" WHERE id = ?;"
        )) {
            ps.setObject(1, authUserEntity.getId());

            int rowsDeleted = ps.executeUpdate();

            if (rowsDeleted < 0) {
                throw new SQLException(String.format("Can`t delete user [%s] because it's not found", authUserEntity));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE id = ?;"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    AuthUserEntity ue = new AuthUserEntity();
                    ue.setId(rs.getObject("id", UUID.class));
                    ue.setUsername(rs.getString("username"));
                    ue.setEnabled(rs.getBoolean("enabled"));
                    ue.setAccountNonExpired(rs.getBoolean("accountNonExpired"));
                    ue.setAccountNonLocked(rs.getBoolean("accountNonLocked"));
                    ue.setCredentialsNonExpired(rs.getBoolean("credentialsNonExpired"));
                    return Optional.of(ue);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
