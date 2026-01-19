package ch.heigvd.dai.database;

import ch.heigvd.dai.models.Action;
import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

@RegisterFieldMapper(Action.class)
public interface ActionDao {

    @SqlQuery(
            """
            SELECT * FROM diamonds_are_forever.action
            WHERE lotId = :lotId
            ORDER BY createdAt
            """)
    List<Action> getActionForItem(@Bind("lotId") int lotId);
}
