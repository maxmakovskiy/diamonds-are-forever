package ch.heigvd.dai.database;

import ch.heigvd.dai.models.Employee;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterFieldMapper(Employee.class)
public interface EmployeeDao {
  @SqlQuery(
      """
      SELECT
         e.employeeId,
         c.counterpartId,
         c.name,
         e.firstName,
         e.lastName,
         e.email,
         e.isTmpPassword,
         e.password,
         e.role,
         e.isActive
      FROM diamonds_are_forever.employee e
      JOIN diamonds_are_forever.counterpart c USING (counterpartId)
      WHERE e.email = :email
      """)
  Employee findByEmail(@Bind("email") String email);

  @SqlQuery(
      """
      SELECT
        e.employeeId,
        c.counterpartId,
        c.name,
        e.firstName,
        e.lastName,
        e.email,
        e.isTmpPassword,
        e.password,
        e.role,
        e.isActive
      FROM diamonds_are_forever.employee e
      JOIN diamonds_are_forever.counterpart c USING (counterpartId)
      WHERE e.employeeId = :employeeId
      """)
  Employee findById(@Bind("employeeId") int employeeId);

  @SqlUpdate(
      """
      UPDATE diamonds_are_forever.employee
      SET (
          password,
          isTmpPassword
      ) = (:password, FALSE)
      WHERE employeeId = :employeeId
      """)
  void changePassword(@Bind("employeeId") int employeeId, @Bind("password") byte[] password);
}
