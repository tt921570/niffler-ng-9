package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import java.util.UUID;

public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRepository;

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendDbClient(SpendRepository spendRepository) {
        this.spendRepository = spendRepository;
    }

    public SpendJson createSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(
                () -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = spendRepository.createCategory(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            spendRepository.create(spendEntity)
                    );
                }
        );
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(
                () -> {
                    CategoryEntity newCategoryEntity = CategoryEntity.fromJson(category);
                    CategoryEntity createdCategory = spendRepository.createCategory(newCategoryEntity);
                    return CategoryJson.fromEntity(createdCategory);
                }
        );
    }

    @Override
    public SpendJson updateSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(
                () -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    SpendEntity updatedSpendEntity = spendRepository.update(spendEntity);
                    return SpendJson.fromEntity(updatedSpendEntity);
                }
        );
    }

    @Override
    public CategoryJson findCategoryById(UUID id) {
        return xaTransactionTemplate.execute(
                () -> {
                    Optional<CategoryEntity> categoryEntity = spendRepository.findCategoryById(id);
                    return categoryEntity
                            .map(CategoryJson::fromEntity)
                            .orElseThrow(() -> new EntityNotFoundException("Can't find Category with id: " + id));
                }
        );
    }

    @Override
    public CategoryJson findCategoryByUsernameAndCategoryName(String username, String name) {
        return xaTransactionTemplate.execute(
                () -> spendRepository.findCategoryByUsernameAndCategoryName(username, name)
                        .map(CategoryJson::fromEntity)
                        .orElseThrow(() -> new RuntimeException("Can't find Category with username: " + username
                        + " and name: " + name))
        );
    }

    @Override
    public SpendJson findSpendById(UUID id) {
        return xaTransactionTemplate.execute(
                () -> spendRepository.findById(id)
                        .map(SpendJson::fromEntity)
                        .orElseThrow(() -> new RuntimeException("Can't find Spend with id: " + id))
        );
    }

    @Override
    public SpendJson findSpendByUsernameAndDescription(String username, String description) {
        return xaTransactionTemplate.execute(
                () -> spendRepository.findByUsernameAndSpendDescription(username, description)
                        .map(SpendJson::fromEntity)
                        .orElseThrow(() -> new RuntimeException("Can't find Spend with username: " + username
                                + " and description: " + description))
        );
    }

    @Override
    public void deleteSpend(SpendJson spend) {
        xaTransactionTemplate.execute(
                () -> {
                    spendRepository.remove(SpendEntity.fromJson(spend));
                    return null;
                }
        );
    }

    @Override
    public void deleteCategory(CategoryJson category) {
        xaTransactionTemplate.execute(
                () -> {
                    spendRepository.removeCategory(CategoryEntity.fromJson(category));
                    return null;
                }
        );
    }

}
