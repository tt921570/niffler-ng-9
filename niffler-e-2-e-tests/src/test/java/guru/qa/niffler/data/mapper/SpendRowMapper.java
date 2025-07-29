package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author Alexander
 */
public class SpendRowMapper implements RowMapper<SpendEntity> {

    public static final SpendRowMapper instance = new SpendRowMapper();

    private SpendRowMapper() {
    }

    @Override
    public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpendEntity se = new SpendEntity();
        se.setId(rs.getObject("id", UUID.class));
        se.setUsername(rs.getString("username"));
        se.setSpendDate(rs.getDate("spend_date"));
        se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        se.setAmount(rs.getDouble("amount"));
        se.setDescription(rs.getString("description"));
        CategoryEntity ce = new CategoryEntity();
        ce.setId(rs.getObject("category_id", UUID.class));
        se.setCategory(ce);
        return null;
    }
}
