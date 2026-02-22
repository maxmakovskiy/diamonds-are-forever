package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.database.EmployeeDao;
import ch.heigvd.dai.models.Employee;
import ch.heigvd.dai.models.LoginUser;
import ch.heigvd.dai.models.PasswordChange;
import ch.heigvd.dai.utils.Security;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.http.util.NaiveRateLimit;
import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class AuthController {
  public static final String USER_ID = "USER_ID";

  public void login(Context ctx) {
    NaiveRateLimit.requestPerTimeUnit(ctx, 20, TimeUnit.HOURS);

    LoginUser loginUser =
        ctx.bodyValidator(LoginUser.class)
            .check(obj -> obj.email() != null, "Missing email")
            .check(obj -> obj.password() != null, "Missing password")
            .get();

    EmployeeDao dao = Database.getInstance().jdbi.onDemand(EmployeeDao.class);
    Employee user = dao.findByEmail(loginUser.email());

    if (user == null) {
      throw new UnauthorizedResponse();
    }

    byte[] loginPassword = loginUser.password().getBytes(StandardCharsets.UTF_8);

    if (user.isTmpPassword && Arrays.equals(loginPassword, user.password)) {
      ctx.sessionAttribute(USER_ID, String.valueOf(user.employeeId));

      user.password = null;
      ctx.json(user);

      ctx.status(HttpStatus.NO_CONTENT);
      return;
    }

    byte[] salt = Security.extractSalt(user.password);
    byte[] hashed = Security.hash(loginPassword, salt);

    if (Arrays.equals(hashed, user.password)) {
      ctx.sessionAttribute(USER_ID, String.valueOf(user.employeeId));

      user.password = null;
      ctx.json(user);

      ctx.status(HttpStatus.OK);
    } else {
      throw new UnauthorizedResponse();
    }
  }

  public void logout(Context ctx) {
    HttpSession s = ctx.req().getSession(false);
    if (s != null) {
      s.invalidate();
    }
    ctx.status(HttpStatus.NO_CONTENT);
  }

  public void getProfile(Context ctx) {
    String uerId = ctx.sessionAttribute(USER_ID);
    if (uerId == null || uerId.isEmpty()) {
      throw new UnauthorizedResponse();
    }

    EmployeeDao dao = Database.getInstance().jdbi.onDemand(EmployeeDao.class);
    Employee user = dao.findById(Integer.parseInt(uerId));

    if (user == null) {
      throw new UnauthorizedResponse();
    }

    ctx.json(user);
    ctx.status(HttpStatus.OK);
  }

  public void changePassword(Context ctx) {
    NaiveRateLimit.requestPerTimeUnit(ctx, 20, TimeUnit.HOURS);

    String userId = ctx.sessionAttribute(USER_ID);
    EmployeeDao dao = Database.getInstance().jdbi.onDemand(EmployeeDao.class);
    Employee user = dao.findById(Integer.parseInt(userId));

    if (user == null) {
      throw new UnauthorizedResponse();
    }

    PasswordChange passwords =
        ctx.bodyValidator(PasswordChange.class)
            .check(obj -> obj.oldPassword() != null, "Missing old password")
            .check(obj -> obj.newPassword() != null, "Missing new password")
            .get();

    byte[] oldPassword = passwords.oldPassword().getBytes(StandardCharsets.UTF_8);

    boolean isPasswordMatch =
        user.isTmpPassword
            ? Arrays.equals(oldPassword, user.password)
            : Arrays.equals(
                Security.hash(oldPassword, Security.extractSalt(user.password)), user.password);

    if (isPasswordMatch) {
      byte[] hashed =
          Security.hash(
              passwords.newPassword().getBytes(StandardCharsets.UTF_8), Security.generateSalt());
      dao.changePassword(user.employeeId, hashed);
      ctx.status(HttpStatus.OK);
    } else {
      throw new UnauthorizedResponse();
    }
  }
}
