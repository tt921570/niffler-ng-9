package guru.qa.niffler.data.entity.auth;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Alexander
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserEntity {
    private UUID id;
    private String username;
    private String password;
    @Builder.Default
    private Boolean enabled = true;
    @Builder.Default
    private Boolean accountNonExpired = true;
    @Builder.Default
    private Boolean accountNonLocked = true;
    @Builder.Default
    private Boolean credentialsNonExpired = true;
    @Builder.Default
    private List<AuthorityEntity> authorities = new ArrayList<>();
}
