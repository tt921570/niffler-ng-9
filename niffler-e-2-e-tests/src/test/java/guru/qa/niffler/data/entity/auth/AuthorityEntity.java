package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.data.dao.Authority;
import lombok.*;

import java.util.UUID;

/**
 * @author Alexander
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityEntity {
    private UUID id;
    private Authority authority;
    private UUID userId;
}
