package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityManagers.em;

/**
 * @author Alexander
 */
public class SpendRepositoryHibernate implements SpendRepository {

    private final EntityManager entityManager = em(Config.getInstance().spendJdbcUrl());

    @Override
    public SpendEntity create(SpendEntity spend) {
        entityManager.joinTransaction();
        entityManager.persist(spend);
        return spend;
    }

    @Override
    public SpendEntity update(SpendEntity spend) {
        return spend;
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        entityManager.joinTransaction();
        entityManager.persist(category);
        return category;
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return Optional.ofNullable(
                entityManager.find(CategoryEntity.class, id)
        );
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String name) {
        try {
            return Optional.ofNullable(
                    entityManager.createQuery("""
                    select c from CategoryEntity c
                    where c.username =: username
                    and c.name =: name
                    """, CategoryEntity.class)
                            .setParameter("username", username)
                            .setParameter("name", name)
                            .getSingleResult()
            );
        } catch (NoResultException nre) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        try {
            return Optional.ofNullable(
                    entityManager.createQuery("""
                            select s from SpendEntity s
                            where s.id =: id
                            """, SpendEntity.class)
                            .setParameter("id", id)
                            .getSingleResult()
            );
        } catch (NoResultException nre) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        try {
            return Optional.ofNullable(
                    entityManager.createQuery("""
                            select s from SpendEntity s
                            where s.username =: username
                            and s.description =: description
                            """, SpendEntity.class)
                            .setParameter("username", username)
                            .setParameter("description", description)
                            .getSingleResult()
            );
        } catch (NoResultException nre) {
            return Optional.empty();
        }
    }

    @Override
    public void remove(SpendEntity spend) {
        entityManager.joinTransaction();
        SpendEntity deletingSpendEntity = entityManager.getReference(SpendEntity.class, spend.getId());
        entityManager.remove(deletingSpendEntity);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        entityManager.joinTransaction();
        CategoryEntity deletingCategoryEntity = entityManager.getReference(CategoryEntity.class, category.getId());
        entityManager.remove(deletingCategoryEntity);
    }
}
