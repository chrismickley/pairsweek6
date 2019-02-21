package com.techelevator.projects.view;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;

public class JDBCDepartmentDAOTest extends DAOIntegrationTest {
	JDBCDepartmentDAO testing;
	JdbcTemplate jdbcTemplate;

	@Before
	public void setup() {
		testing = new JDBCDepartmentDAO(getDataSource());
	}

	@Test
	public void getAllDepartments_ShouldReturnFullAmountOfDepartments_WhenDataIsValid() {
		List<Department> departmentList = testing.getAllDepartments();

		assertEquals(4, departmentList.size());
	}

	@Test
	public void searchDepartmentsByName_ShouldReturnDepartment_WhenInputIsName() {
		jdbcTemplate = new JdbcTemplate(getDataSource());
		jdbcTemplate.execute("INSERT INTO department (department_id, name) VALUES(5, 'New Dept')");
		Department department = new Department();
		department.setId((long) 5);
		department.setName("New Dept");

		List<Department> departmentList = testing.searchDepartmentsByName(department.getName());

		assertDepartmentsAreEqual(department, departmentList.get(0));
	}

	@Test
	public void saveDepartment_ShouldUpdateDepartment_WhenDataIsValid() {
		jdbcTemplate = new JdbcTemplate(getDataSource());
		jdbcTemplate.execute("INSERT INTO department (department_id, name) VALUES(5, 'New Dept')");
		Department department = new Department();
		department.setId((long) 5);
		department.setName("Old Dept");

		testing.saveDepartment(department);

		assertDepartmentsAreEqual(department, testing.getDepartmentById(department.getId()));
	}

	@Test
	public void createDepartment_ShouldInsertDepartment_WhenDataIsValid() {
		Department department = new Department();
		department.setName("More Dept");

		testing.createDepartment(department);
		List<Department> departmentList = testing.getAllDepartments();

		assertEquals(5, departmentList.size());
	}

	@Test
	public void getDepartmentsById_ShouldReturnDepartment_WhenInputIsId() {
		jdbcTemplate = new JdbcTemplate(getDataSource());
		jdbcTemplate.execute("INSERT INTO department (department_id, name) VALUES(5, 'New Dept')");
		Department department = new Department();
		department.setId((long) 5);
		department.setName("New Dept");

		Department departmentFromId = testing.getDepartmentById(department.getId());

		assertDepartmentsAreEqual(department, departmentFromId);
	}

	private void assertDepartmentsAreEqual(Department expected, Department actual) {
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getName(), actual.getName());
	}
}