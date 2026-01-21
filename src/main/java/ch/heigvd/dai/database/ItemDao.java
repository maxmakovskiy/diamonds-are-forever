package ch.heigvd.dai.database;

import ch.heigvd.dai.models.Item;
import java.time.OffsetDateTime;
import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindFields;
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
    @GetGeneratedKeys("lotid")
    int insertItem(
            @Bind("stockName") String stockName,
            @Bind("purchaseDate") OffsetDateTime purchaseDate,
            @Bind("origin") String origin,
            @Bind("type") String type);

    @SqlUpdate(
            """
            UPDATE diamonds_are_forever.item
            SET (
                stockName,
                purchaseDate,
                origin,
                updatedAt
            ) = (:stockName, :purchaseDate, :origin, NOW())
            WHERE lotId = :lotId
            """)
    void updateItem(@BindFields Item item);

    @SqlQuery("SELECT * FROM diamonds_are_forever.item ORDER BY purchaseDate")
    List<Item> getAllItems();

    @SqlQuery("SELECT * FROM diamonds_are_forever.item WHERE lotId = :lotId")
    Item getItemByLotId(@Bind("lotId") int lotId);

    @SqlUpdate("DELETE FROM diamonds_are_forever.item WHERE lotId = :lotId")
    void deleteItem(@Bind("lotId") int lotId);

    @SqlQuery(
            """
            SELECT DISTINCT i.*
            FROM diamonds_are_forever.item i
            WHERE NOT EXISTS (
                SELECT 1
                FROM diamonds_are_forever.action a
                WHERE a.lotId = i.lotId
                AND a.category = 'sale'
            )
            ORDER BY i.purchaseDate
            """)
    List<Item> getAvailableItems();

    @SqlQuery(
            """
            SELECT * FROM diamonds_are_forever.item
            WHERE type = cast(:type as diamonds_are_forever.item_category)
            ORDER BY purchaseDate
            """)
    List<Item> getItemsByType(@Bind("type") String type);
}
