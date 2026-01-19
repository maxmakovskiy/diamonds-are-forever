package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.ActionDao;
import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.database.ItemDao;
import ch.heigvd.dai.models.Action;
import ch.heigvd.dai.models.Item;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import java.util.List;
import org.jdbi.v3.core.Handle;

public class ActionController {
    public void getAllForItem(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        try (Handle handle = Database.getInstance().jdbi.open()) {
            ItemDao itemDao = handle.attach(ItemDao.class);
            Item item = itemDao.getItemByLotId(id);

            if (item == null) {
                throw new NotFoundResponse();
            }

            ActionDao actionDao = handle.attach(ActionDao.class);
            List<Action> actions = actionDao.getActionForItem(id);
            ctx.json(actions);
            ctx.status(200);
        }
    }
}
