package ch.heigvd.dai.item;

import java.time.OffsetDateTime;
import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindFields;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterFieldMapper(Item.class)
public interface ItemDao {
    @SqlUpdate(
            """
                INSERT INTO diamonds_are_forever.item (stockName, purchaseDate, origin, type) VALUES (
                    :stockName,
                    :purchaseDate,
                    :origin,
                    cast(:type as diamonds_are_forever.item_category)
                )
            """)
    void insertItem(
            @Bind("stockName") String stockName,
            @Bind("purchaseDate") OffsetDateTime purchaseDate,
            @Bind("origin") String origin,
            @Bind("type") String type);

    @SqlUpdate(
            """
                INSERT INTO diamonds_are_forever.item (stockName, purchaseDate, origin, type) VALUES (
                    :stockName,
                    :purchaseDate,
                    :origin,
                    cast(:type as diamonds_are_forever.item_category)
                )
            """)
    void insertItem(@BindFields Item item);

    @SqlQuery("SELECT * FROM diamonds_are_forever.item ORDER BY purchaseDate")
    List<Item> getAllItems();
}
