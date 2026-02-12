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
      SELECT * FROM diamonds_are_forever.employee
      WHERE email = :email
      """)
  Employee findByEmail(@Bind("email") String email);

  @SqlQuery(
      """
      SELECT * FROM diamonds_are_forever.employee
      WHERE employeeId = :employeeId
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
