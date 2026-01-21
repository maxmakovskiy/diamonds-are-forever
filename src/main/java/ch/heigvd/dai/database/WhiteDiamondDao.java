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
                width,
                depth,
                gemType,
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
                    gemType,
                    whiteScale,
                    clarity
                ) VALUES (
                    :lotId,
                    :weightCt,
                    cast(:shape as diamonds_are_forever.shape),
                    :length,
                    :width,
                    :depth,
                    cast(:gemType as diamonds_are_forever.gem_type),
                    cast(:whiteScale as diamonds_are_forever.white_scale),
                    cast(:clarity as diamonds_are_forever.clarity)
                )
            """)
    void insertWhiteDiamond(@BindFields WhiteDiamond wd);

    @SqlUpdate(
            """
                UPDATE diamonds_are_forever.white_diamond
                SET (
                    weightCt,
                    shape,
                    length,
                    width,
                    depth,
                    whiteScale,
                    clarity
                ) = (
                    :weightCt,
                    cast(:shape as diamonds_are_forever.shape),
                    :length,
                    :width,
                    :depth,
                    cast(:gemType as diamonds_are_forever.gem_type),
                    cast(:whiteScale as diamonds_are_forever.white_scale),
                    cast(:clarity as diamonds_are_forever.clarity)
                ) WHERE lotId = :lotId
            """)
    void updateWhiteDiamond(
            @Bind("lotId") int lotId,
            @Bind("weightCt") double weightCt,
            @Bind("shape") String shape,
            @Bind("length") double length,
            @Bind("width") double width,
            @Bind("depth") double depth,
            @Bind("gemType") String gemType,
            @Bind("whiteScale") String whiteScale,
            @Bind("clarity") String clarity);
}
