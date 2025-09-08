package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.extractor.AuthUserEntityResultSetExtractor;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander
 */
public class AuthUserRepositorySpring implements AuthUserRepository {

    private static final String URL = Config.getInstance().authJdbcUrl();

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(URL));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                        """
                        INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)
                        VALUES (?,?,?,?,?,?);
                        """,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());
            return ps;
        }, kh);

        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        user.setId(generatedKey);

        List<AuthorityEntity> authorities = user.getAuthorities();
        jdbcTemplate.batchUpdate(
                "INSERT INTO authority (user_id, authority) VALUES (? , ?);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, generatedKey);
                        ps.setString(2, authorities.get(i).getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return authorities.size();
                    }
                }
        );
        return user;
    }

    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(URL));
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                """
                UPDATE "user"
                SET
                username = ?,
                "password" = ?,
                enabled = ? ,
                account_non_expired = ?,
                account_non_locked  = ?,
                credentials_non_expired = ?
                WHERE id = ?;
                """
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());
            ps.setObject(7, user.getId());
            return ps;
        });

        jdbcTemplate.update(con -> {
            PreparedStatement deleteAuthoritiesPs = con.prepareStatement(
                    "DELETE FROM \"authority\" WHERE user_id = ?;"
            );
            deleteAuthoritiesPs.setObject(1, user.getId());
            deleteAuthoritiesPs.executeUpdate();
            return deleteAuthoritiesPs;
        });

        List<AuthorityEntity> authorities = user.getAuthorities();
        jdbcTemplate.batchUpdate(
                "INSERT INTO authority (user_id, authority) VALUES (? , ?);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, user.getId());
                        ps.setString(2, authorities.get(i).getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return authorities.size();
                    }
                }
        );
        return user;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return Optional.ofNullable(
                new JdbcTemplate(DataSources.dataSource(URL)).query(
                            """
                            SELECT a.id as authority_id,
                                   authority,
                                   user_id,
                                   u.username,
                                   u.password,
                                   u.enabled,
                                   u.account_non_expired,
                                   u.account_non_locked,
                                   u.credentials_non_expired
                            FROM \"user\" u join authority a on u.id = a.user_id WHERE u.id = ?
                            """,
                        AuthUserEntityResultSetExtractor.instance,
                        id
                )
        );
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        return Optional.ofNullable(
                new JdbcTemplate(DataSources.dataSource(URL)).query(
                        """
                        SELECT u.*,
                               a.id as authority_id,
                               a.authority
                        FROM \"user\" u
                            JOIN authority a ON u.id = a.user_id
                        WHERE u.username = ?;
                        """,
                        AuthUserEntityResultSetExtractor.instance,
                        username
                )
        );
    }

    @Override
    public void remove(AuthUserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(URL));

        jdbcTemplate.update(con -> {
            PreparedStatement deleteAuthoritiesPs = con.prepareStatement(
                    "DELETE FROM \"authority\" WHERE user_id = ?;"
            );
            deleteAuthoritiesPs.setObject(1, user.getId());
            deleteAuthoritiesPs.executeUpdate();
            return deleteAuthoritiesPs;
        });

        jdbcTemplate.update(con -> {
            PreparedStatement deleteUserPs = con.prepareStatement(
                    "DELETE FROM \"user\" WHERE id = ?;"
            );
            deleteUserPs.setObject(1, user.getId());
            deleteUserPs.executeUpdate();
            return deleteUserPs;
        });
    }
}
