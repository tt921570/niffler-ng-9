package guru.qa.niffler.data.entity.userdata;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.User;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Alexander
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserdataUserEntity implements Serializable {
    private UUID id;
    private String username;
    private CurrencyValues currency;
    private String fullname;
    private String firstName;
    private String surname;
    private byte[] photo;
    private byte[] photoSmall;

    public static UserdataUserEntity fromUser(User user) {
        return UserdataUserEntity.builder()
                .id(user.getDataId())
                .username(user.getUserdataUsername())
                .currency(user.getCurrency())
                .fullname(user.getFullname())
                .firstName(user.getFirstName())
                .surname(user.getSurname())
                .photo(user.getPhoto())
                .photoSmall(user.getPhotoSmall())
                .build();
    }
}
