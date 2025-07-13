package guru.qa.niffler.model;

import guru.qa.niffler.data.dao.Authority;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.EnumSet;
import java.util.UUID;

/**
 * @author Alexander
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID authId;
    private UUID dataId;
    // todo: Объединить в дальнейших итерациях. Разделено для теста распределенных транзакций.
    private String authUsername;
    private String userdataUsername;
    private String password;
    private String fullname;
    private String firstName;
    private String surname;
    @Builder.Default
    private Boolean enabled = true;
    @Builder.Default
    private Boolean accountNonExpired = true;
    @Builder.Default
    private Boolean accountNonLocked = true;
    @Builder.Default
    private Boolean credentialsNonExpired = true;
    @Builder.Default
    private CurrencyValues currency = CurrencyValues.RUB;
    private byte[] photo;
    private byte[] photoSmall;
    @Builder.Default
    private EnumSet<Authority> authorities = EnumSet.of(Authority.read, Authority.write);
}
