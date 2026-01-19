package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.ActionDao;
import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.database.ItemDao;
import ch.heigvd.dai.models.Action;
import ch.heigvd.dai.models.Item;
import io.javalin.http.ConflictResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import java.util.List;
import java.util.Objects;

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
            List<Action> actions = actionDao.getActionsForItem(id);
            ctx.json(actions);
            ctx.status(200);
        }
    }

    public void update(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        try (Handle handle = Database.getInstance().jdbi.open()) {
            ActionDao actionDao = handle.attach(ActionDao.class);

            Action existingAction = actionDao.getActionById(id);
            if (existingAction == null) {
                throw new NotFoundResponse();
            }

            List<Action> itemActions = actionDao.getActionsForItem(existingAction.lotId);
            // we do getFirst since getActionsForItem return actions ordered by creation date in descending manner
            if (itemActions.getFirst().actionId != existingAction.actionId) {
                throw new ConflictResponse();
            }

            // TODO:
            // Maybe before going to update an action
            // we should if it's indeed different
            // since querying DB is slow
            Action incomingAction = ctx.bodyValidator(Action.class).get();
            incomingAction.actionId = id;

            Action a = actionDao.updateAction(incomingAction);

            ctx.json(a);

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
            if (itemActions.getFirst().actionId != existingAction.actionId) {
                throw new ConflictResponse();
            }
            actionDao.deleteActionById(id);
            ctx.status(200);
        }
    }

    public void create(Context ctx) {
        Action action = ctx.bodyValidator(Action.class)
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
                Action lastAction = prevActions.getFirst();

                // NOTE:
                // Obviously there are many combinations
                // for sake of simplicity we would check just a couple
                boolean isAnythingAfterSale = Objects.equals(lastAction.category, "sale");
                boolean isDoublePurchase = Objects.equals(action.category, "purchase")
                        && Objects.equals(lastAction.category, "purchase");
                boolean isDoubleFactoryTransfer = Objects.equals(action.category, "transfer to factory")
                        && Objects.equals(lastAction.category, "transfer to factory");
                boolean isDoubleLabTransfer = Objects.equals(action.category, "transfer to lab")
                        && Objects.equals(lastAction.category, "transfer to lab");
                // transfer to office can be double since there is no return-action for it

                if (isAnythingAfterSale || isDoublePurchase ||
                    isDoubleFactoryTransfer || isDoubleLabTransfer) {
                    // cant be purchase after sale
                    throw new ConflictResponse();
                }
            }

            Action createdAction = actionDao.createAction(action);
            ctx.json(createdAction);
            ctx.status(200);
        }
    }

}
