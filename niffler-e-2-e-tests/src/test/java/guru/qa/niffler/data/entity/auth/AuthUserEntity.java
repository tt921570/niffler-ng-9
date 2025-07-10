package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

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

    public static AuthUserEntity fromUser(User user) {
        return AuthUserEntity.builder()
                .id(user.getAuthId())
                .username(user.getAuthUsername())
                .password(user.getPassword())
                .enabled(user.getEnabled())
                .accountNonExpired(user.getAccountNonExpired())
                .accountNonLocked(user.getAccountNonLocked())
                .credentialsNonExpired(user.getCredentialsNonExpired())
                .authorities(user.getAuthorities().stream()
                        .map(authority -> AuthorityEntity.builder()
                                .authority(authority)
                                .build())
                        .collect(toList())
                )
                .build();
    }
}
