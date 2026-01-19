package ch.heigvd.dai.database;

import ch.heigvd.dai.models.Item;
import java.time.OffsetDateTime;
import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterFieldMapper(Item.class)
public interface ItemDao {
    // `cast (item as item_type)`
    // explained: https://stackoverflow.com/a/73957533
    @SqlUpdate(
            """
                INSERT INTO diamonds_are_forever.item (stockName, purchaseDate, origin, type) VALUES (
                    :stockName,
                    :purchaseDate,
                    :origin,
                    cast(:type as diamonds_are_forever.item_category)
                )
            """)
    @GetGeneratedKeys("diamonds_are_forever.item.lotId")
    int insertItem(
            @Bind("stockName") String stockName,
            @Bind("purchaseDate") OffsetDateTime purchaseDate,
            @Bind("origin") String origin,
            @Bind("type") String type);

    @SqlQuery("SELECT * FROM diamonds_are_forever.item ORDER BY purchaseDate")
    @RegisterFieldMapper(Item.class)
    List<Item> getAllItems();
}
