package ch.heigvd.dai.database;

import ch.heigvd.dai.models.WhiteDiamond;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindFields;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterFieldMapper(value = WhiteDiamond.class)
public interface WhiteDiamondDao {
    @SqlQuery(
            """
            SELECT
                id.lotId,
                stockName,
                purchaseDate,
                origin,
                type,
                createdAt,
                updatedAt,
                weightCt,
                shape,
                length,
                depth,
                whiteScale,
                clarity
            FROM diamonds_are_forever.item id
                INNER JOIN diamonds_are_forever.white_diamond wd
                ON id.lotId = wd.lotId
            WHERE id.lotId = :lotId
            """)
    WhiteDiamond findByLotId(@Bind("lotId") int lotId);

    @SqlUpdate(
            """
                INSERT INTO white_diamond (
                    lotId
                    weightCt,
                    shape,
                    length,
                    width,
                    depth,
                    whiteScale,
                    clarity
                ) VALUES (
                    :lotId,
                    :weightCt,
                    cast(:shape as diamonds_are_forever.shape),
                    :length,
                    :width,
                    :depth,
                    :whiteScale,
                    :clarity
                )
            """)
    void insertWhiteDiamond(@BindFields WhiteDiamond wd);
}
