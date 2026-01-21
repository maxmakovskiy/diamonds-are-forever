package ch.heigvd.dai.database;

import ch.heigvd.dai.models.ColoredDiamond;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindFields;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

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

    @SqlUpdate(
            """
            INSERT INTO diamonds_are_forever.colored_diamond (
                lotId,
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
            ) VALUES (
                :lotId,
                :weightCt,
                cast(:shape as diamonds_are_forever.shape),
                :length,
                :width,
                :depth,
                cast(:gemType as diamonds_are_forever.gem_type),
                cast(:fancyIntensity as diamonds_are_forever.fancy_intensity),
                :fancyOvertone,
                cast(:fancyColor as diamonds_are_forever.fancy_color),
                cast(:clarity as diamonds_are_forever.clarity)
            )
            """)
    void insertColoredDiamond(@BindFields ColoredDiamond cd);

    @SqlUpdate(
            """
            UPDATE diamonds_are_forever.colored_diamond
            SET (
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
            ) = (
                :weightCt,
                cast(:shape as diamonds_are_forever.shape),
                :length,
                :width,
                :depth,
                cast(:gemType as diamonds_are_forever.gem_type),
                cast(:fancyIntensity as diamonds_are_forever.fancy_intensity),
                :fancyOvertone,
                cast(:fancyColor as diamonds_are_forever.fancy_color),
                cast(:clarity as diamonds_are_forever.clarity)
            ) WHERE lotId = :lotId
            """)
    void updateColoredDiamond(
            @Bind("lotId") int lotId,
            @Bind("weightCt") double weightCt,
            @Bind("shape") String shape,
            @Bind("length") double length,
            @Bind("width") double width,
            @Bind("depth") double depth,
            @Bind("gemType") String gemType,
            @Bind("fancyIntensity") String fancyIntensity,
            @Bind("fancyOvertone") String fancyOvertone,
            @Bind("fancyColor") String fancyColor,
            @Bind("clarity") String clarity);
}
