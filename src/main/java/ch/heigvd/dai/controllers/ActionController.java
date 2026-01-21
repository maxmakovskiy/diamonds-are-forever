package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.ActionDao;
import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.database.ItemDao;
import ch.heigvd.dai.models.Action;
import ch.heigvd.dai.models.Item;
import io.javalin.http.ConflictResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.NotModifiedResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import org.jdbi.v3.core.Handle;

public class ActionController {
    private final ConcurrentMap<Integer, LocalDateTime> lifecyclesCache;

    public ActionController(ConcurrentMap<Integer, LocalDateTime> lifecyclesCache) {
        this.lifecyclesCache = lifecyclesCache;
    }

    public void getAllForItem(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        LocalDateTime lastKnownModification =
                ctx.headerAsClass("If-Modified-Since", LocalDateTime.class).getOrDefault(null);

        if (lastKnownModification != null
                && lifecyclesCache.get(id).equals(lastKnownModification)) {
            throw new NotModifiedResponse();
        }

        try (Handle handle = Database.getInstance().jdbi.open()) {
            ItemDao itemDao = handle.attach(ItemDao.class);
            Item item = itemDao.getItemByLotId(id);

            if (item == null) {
                throw new NotFoundResponse();
            }

            ActionDao actionDao = handle.attach(ActionDao.class);
            List<Action> actions = actionDao.getActionsForItem(id);

            // after having fetched data from DB
            // store put it in cache
            LocalDateTime now;
            if (lifecyclesCache.containsKey(id)) {
                now = lifecyclesCache.get(id);
            } else {
                now = LocalDateTime.now();
                lifecyclesCache.put(id, now);
            }
            ctx.header("Last-Modified", String.valueOf(now));

            ctx.json(actions);
            ctx.status(200);
        }
    }

    public void delete(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        try (Handle handle = Database.getInstance().jdbi.open()) {
            ActionDao actionDao = handle.attach(ActionDao.class);

            Action existingAction = actionDao.getActionById(id);
            if (existingAction == null) {
                throw new NotFoundResponse();
            }

            List<Action> itemActions = actionDao.getActionsForItem(existingAction.lotId);
            if (itemActions.getLast().actionId != existingAction.actionId) {
                throw new ConflictResponse();
            }
            actionDao.deleteActionById(id);

            // NOTE:
            // When deletion is successful we invalidate
            // lifecycle (all the actions) for item (lotId)
            LocalDateTime now = LocalDateTime.now();
            lifecyclesCache.put(existingAction.lotId, now);

            ctx.status(200);
        }
    }

    public void create(Context ctx) {
        Action action =
                ctx.bodyValidator(Action.class)
                        .check(obj -> obj.fromCounterpartId > 0, "Missing source counterpart")
                        .check(obj -> obj.toCounterpartId > 0, "Missing destination counterpart")
                        .check(obj -> obj.category != null, "Missing category")
                        .check(obj -> obj.shipNum != null, "Missing shipment number")
                        .check(obj -> obj.lotId > 0, "Missing lot id")
                        .check(obj -> obj.employeeId > 0, "Missing employee id")
                        .check(obj -> obj.price >= 0, "Incorrect price")
                        .check(obj -> obj.currencyCode != null, "Missing currency code")
                        .get();

        try (Handle handle = Database.getInstance().jdbi.open()) {
            ActionDao actionDao = handle.attach(ActionDao.class);

            // check that action.category is suitable next action
            // for item with lotId = action.lotId
            List<Action> prevActions = actionDao.getActionsForItem(action.lotId);
            if (!prevActions.isEmpty()) {
                Action lastAction = prevActions.getLast();

                // NOTE:
                // Obviously there are many combinations
                // for sake of simplicity we would check just a couple
                boolean isAnythingAfterSale = Objects.equals(lastAction.category, "sale");
                boolean isDoublePurchase =
                        Objects.equals(action.category, "purchase")
                                && Objects.equals(lastAction.category, "purchase");
                boolean isDoubleFactoryTransfer =
                        Objects.equals(action.category, "transfer to factory")
                                && Objects.equals(lastAction.category, "transfer to factory");
                boolean isDoubleLabTransfer =
                        Objects.equals(action.category, "transfer to lab")
                                && Objects.equals(lastAction.category, "transfer to lab");
                // transfer to office can be double since there is no return-action for it

                if (isAnythingAfterSale
                        || isDoublePurchase
                        || isDoubleFactoryTransfer
                        || isDoubleLabTransfer) {
                    // cant be purchase after sale
                    throw new ConflictResponse();
                }
            }

            Action createdAction = actionDao.createAction(action);

            lifecyclesCache.put(createdAction.actionId, LocalDateTime.now());

            // NOTE:
            // The same for creation as for deletion.
            // When creation is successful we invalidate
            // lifecycle (all the actions) for item (lotId)
            LocalDateTime now = LocalDateTime.now();
            lifecyclesCache.put(createdAction.lotId, now);

            ctx.json(createdAction);
            ctx.status(200);
        }
    }
}
