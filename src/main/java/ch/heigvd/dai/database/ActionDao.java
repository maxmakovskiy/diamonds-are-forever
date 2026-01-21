package ch.heigvd.dai.database;

import ch.heigvd.dai.models.Action;
import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindFields;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterFieldMapper(Action.class)
public interface ActionDao {

    @SqlQuery(
            """
            SELECT * FROM diamonds_are_forever.action
            WHERE lotId = :lotId
            ORDER BY createdAt
            """)
    List<Action> getActionsForItem(@Bind("lotId") int lotId);

    @SqlQuery(
            """
            SELECT * FROM diamonds_are_forever.action
            WHERE actionId = :actionId
            """)
    Action getActionById(@Bind("actionId") int actionId);

    @SqlUpdate(
            """
            UPDATE diamonds_are_forever.action
            SET (
                fromCounterpartId,
                toCounterpartId,
                terms,
                category,
                shipNum,
                shipDate,
                lotId,
                employeeId,
                price,
                currencyCode,
                updatedAt
            ) = (
                :fromCounterpartId,
                :toCounterpartId,
                :terms,
                cast(:category as diamonds_are_forever.transfer_category),
                :shipNum,
                :shipDate,
                :lotId,
                :employeeId,
                :price,
                cast(:currencyCode as diamonds_are_forever.code),
                NOW()
            ) WHERE actionId = :actionId
            """)
    @GetGeneratedKeys
    Action updateAction(@BindFields Action action);

    @SqlUpdate(
            """
            DELETE FROM diamonds_are_forever.action
            WHERE actionId = :actionId
            """)
    void deleteActionById(@Bind("actionId") int actionId);

    @SqlUpdate(
            """
            INSERT INTO diamonds_are_forever.action (
                fromCounterpartId,
                toCounterpartId,
                terms,
                category,
                shipNum,
                shipDate,
                lotId,
                employeeId,
                price,
                currencyCode
            ) VALUES (
                :fromCounterpartId,
                :toCounterpartId,
                :terms,
                cast(:category as diamonds_are_forever.transfer_category),
                :shipNum,
                :shipDate,
                :lotId,
                :employeeId,
                :price,
                cast(:currencyCode as diamonds_are_forever.code)
            )
            """)
    @GetGeneratedKeys
    Action createAction(@BindFields Action action);

    @SqlUpdate("DELETE FROM diamonds_are_forever.action WHERE lotId = :lotId")
    void deleteActionsForItem(@Bind("lotId") int lotId);
}
