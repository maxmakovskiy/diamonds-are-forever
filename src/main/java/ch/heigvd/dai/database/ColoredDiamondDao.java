package ch.heigvd.dai.database;

import ch.heigvd.dai.models.ColoredDiamond;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

@RegisterFieldMapper(value = ColoredDiamond.class)
public interface ColoredDiamondDao {
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
                fancyIntensity,
                fancyOvertone,
                fancyColor,
                clarity
            FROM diamonds_are_forever.item id
                INNER JOIN diamonds_are_forever.colored_diamond cd
                ON id.lotId = cd.lotId
            WHERE id.lotId = :lotId
            """)
    ColoredDiamond findByLotId(@Bind("lotId") int lotId);
}
