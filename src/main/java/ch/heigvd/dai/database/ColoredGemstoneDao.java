package ch.heigvd.dai.database;

import ch.heigvd.dai.models.ColoredGemstone;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindFields;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

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

    @SqlUpdate(
            """
            INSERT INTO diamonds_are_forever.colored_gem_stone (
                lotId,
                weightCt,
                shape,
                length,
                width,
                depth,
                gemType,
                gemColor,
                treatment
            ) VALUES (
                :lotId,
                :weightCt,
                cast(:shape as diamonds_are_forever.shape),
                :length,
                :width,
                :depth,
                cast(:gemType as diamonds_are_forever.gem_type),
                cast(:gemColor as diamonds_are_forever.gem_color),
                cast(:treatment as diamonds_are_forever.treatment)
            )
            """)
    void insertColoredGemstone(@BindFields ColoredGemstone cg);

    @SqlUpdate(
            """
            UPDATE diamonds_are_forever.colored_gem_stone
            SET (
                weightCt,
                shape,
                length,
                width,
                depth,
                gemType,
                gemColor,
                treatment
            ) = (
                :weightCt,
                cast(:shape as diamonds_are_forever.shape),
                :length,
                :width,
                :depth,
                cast(:gemType as diamonds_are_forever.gem_type),
                cast(:gemColor as diamonds_are_forever.gem_color),
                cast(:treatment as diamonds_are_forever.treatment)
            ) WHERE lotId = :lotId
            """)
    void updateColoredGemstone(
            @Bind("lotId") int lotId,
            @Bind("weightCt") double weightCt,
            @Bind("shape") String shape,
            @Bind("length") double length,
            @Bind("width") double width,
            @Bind("depth") double depth,
            @Bind("gemType") String gemType,
            @Bind("gemColor") String gemColor,
            @Bind("treatment") String treatment);
}
