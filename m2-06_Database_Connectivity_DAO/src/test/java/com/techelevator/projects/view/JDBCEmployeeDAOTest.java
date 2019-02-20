package com.techelevator.projects.view;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;

public class JDBCEmployeeDAOTest extends DAOIntegrationTest {
	JDBCEmployeeDAO testing;
	JdbcTemplate jdbcTemplate;
	DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

	@Before
	public void setup() {
		testing = new JDBCEmployeeDAO(getDataSource());
	}

	@Test
	public void getAllEmployeesTest() {
		List<Employee> employeeList = testing.getAllEmployees();
		assertEquals(12, employeeList.size());

	}

	@Test
	public void searchEmployeesByNameTest() throws Exception {
		Date birthDate = formatDate.parse("1999-01-01");
		Date hireDate = formatDate.parse("2019-01-01");
		jdbcTemplate = new JdbcTemplate(getDataSource());
		jdbcTemplate.execute(
				"INSERT INTO employee (employee_id, department_id, first_name, last_name, birth_date, gender, hire_date) VALUES(13, 1, 'Alpha', 'Bravo', '1999-01-01', 'M', '2019-01-01')");
		Employee employee = setEmployee((long) 13, (long) 1, "Alpha", "Bravo", birthDate, 'M', hireDate);
		List<Employee> employeeList = testing.searchEmployeesByName(employee.getFirstName(), employee.getLastName());
		assertEmployeesAreEqual(employee, employeeList.get(0));
	}

	@Test
	public void getEmployeesByDepartmentIdTest() throws ParseException {
		List<Employee> employeeList = testing.getEmployeesByDepartmentId((long) 3);
		assertEquals(5, employeeList.size());
	}

	@Test
	public void getEmployeesWithoutProjects() {
		List<Employee> employeeList = testing.getEmployeesWithoutProjects();
		assertEquals(2, employeeList.size());
	}

	@Test
	public void getEmployeesByProjectId() {
		List<Employee> employeeList = testing.getEmployeesByProjectId((long) 6);
		assertEquals(3, employeeList.size());
	}

	@Test
	public void changeEmployeeDepartmentTest_() {
		jdbcTemplate = new JdbcTemplate(getDataSource());
		jdbcTemplate.execute("INSERT INTO employee (employee_id, department_id, first_name, last_name, birth_date, gender, hire_date) VALUES(13, 1, 'Alpha', 'Bravo', '1999-01-01', 'M', '2019-01-01')");
		    testing.changeEmployeeDepartment((long)13, (long)2);
		    SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT department_id AS num FROM employee WHERE employee_id = 13");
		    result.next();
		    assertEquals(2, result.getInt("num"));
	}

	private Employee setEmployee(Long employeeId, Long departmentId, String firstName, String lastName, Date birthDay,
			char gender, Date hireDate) {
		Employee employee = new Employee();
		employee.setId(employeeId);
		employee.setDepartmentId(departmentId);
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setBirthDay(birthDay);
		employee.setGender(gender);
		employee.setHireDate(hireDate);
		return employee;
	}

	private void assertEmployeesAreEqual(Employee expected, Employee actual) {
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getDepartmentId(), actual.getDepartmentId());
		assertEquals(expected.getFirstName(), actual.getFirstName());
		assertEquals(expected.getLastName(), actual.getLastName());
		assertEquals(expected.getBirthDay(), actual.getBirthDay());
		assertEquals(expected.getGender(), actual.getGender());
		assertEquals(expected.getHireDate(), actual.getHireDate());
	}

}
