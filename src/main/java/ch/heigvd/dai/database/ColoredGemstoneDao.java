package ch.heigvd.dai.database;

import ch.heigvd.dai.models.ColoredGemstone;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

@RegisterFieldMapper(value = ColoredGemstone.class)
public interface ColoredGemstoneDao {
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
                gemColor,
                treatment
            FROM diamonds_are_forever.item id
                INNER JOIN diamonds_are_forever.colored_gem_stone cgs
                ON id.lotId = cgs.lotId
            WHERE id.lotId = :lotId
            """)
    ColoredGemstone findByLotId(@Bind("lotId") int lotId);
}
