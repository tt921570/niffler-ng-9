package guru.qa.niffler.data.entity.spend;

import guru.qa.niffler.model.CategoryJson;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity implements Serializable {
  private UUID id;
  private String name;
  private String username;
  private boolean archived;

  public static CategoryEntity fromJson(CategoryJson json) {
    CategoryEntity ce = new CategoryEntity();
    ce.setId(json.id());
    ce.setName(json.name());
    ce.setUsername(json.username());
    ce.setArchived(json.archived());
    return ce;
  }
}
