package com.techelevator.projects.view;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;

public class JDBCDepartmentDAOTest extends DAOIntegrationTest  {

	JDBCDepartmentDAO testing;
	JdbcTemplate jdbcTemplate;
	
	@Before
	public void setup() {
		testing = new JDBCDepartmentDAO(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
		jdbcTemplate.execute("INSERT INTO department (department_id, name) VALUES(5, 'New Dept')");
	}
	
	@Test
	public void testGetAllDepartments() {

		List <Department> department = testing.getAllDepartments();
		assertEquals(5, department.size());
		}
	
	@Test
	public void searchDepartmentsByNameTest() {
		Department getTheDept = getDepartment(5, "New Dept");
		List<Department> department = testing.searchDepartmentsByName(getTheDept.getName());
		Department departments = department.get(0);
		assertDepartmentsAreEqual(getTheDept, departments);
		
	}
	
	@Test
	public void saveDepartmentTest() {
		Department getTheDept = getDepartment(5, "Old Dept");
		testing.saveDepartment(getTheDept);
		assertDepartmentsAreEqual(getTheDept, testing.getDepartmentById((long) 5));
		
	}
	
	@Test
	public void createNewDeptTest() {
		Department getTheDept = new Department();
		getTheDept.setName("Something");
		testing.createDepartment(getTheDept);
		List <Department> department = testing.getAllDepartments();
		assertEquals(6, department.size());
		
	}
	@Test
	public void searchDepartmentsByIdTest() {
		Department getTheDept = getDepartment(5, "New Dept");
		Department department = testing.getDepartmentById(getTheDept.getId());
		//Department departments = department.get(0);
		assertDepartmentsAreEqual(getTheDept, department);
		
	}
	private Department getDepartment(long id, String name) {
		Department getDepartment = new Department();
		getDepartment.setName(name);
		getDepartment.setId(id);

		return getDepartment;
	}

	private void assertDepartmentsAreEqual(Department expected, Department actual) {
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getName(), actual.getName());

	}
	
	
}
