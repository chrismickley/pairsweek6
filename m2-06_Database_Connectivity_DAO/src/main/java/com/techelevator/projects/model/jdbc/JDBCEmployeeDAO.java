package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;


import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;


public class JDBCEmployeeDAO implements EmployeeDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCEmployeeDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> employees = new ArrayList<>();
		String name = "SELECT employee_id, department_id, first_name, last_name, birth_date, gender, hire_date FROM employee";
		SqlRowSet results = jdbcTemplate.queryForRowSet(name);
		while(results.next()) {
			Employee employee = mapRowToEmployee(results);
			employees.add(employee);
		}
		return  employees;
	}

	@Override
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch) {
		List<Employee> employees = new ArrayList<>();
		String name = "SELECT employee_id, department_id, first_name, last_name, birth_date, gender, hire_date FROM employee WHERE first_name = ? AND last_name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(name, firstNameSearch, lastNameSearch);
		while(results.next()) {
			Employee employee = mapRowToEmployee(results);
			employees.add(employee);
		}
		return employees;
	
	}

	@Override
	public List<Employee> getEmployeesByDepartmentId(long id) {
		List<Employee> employees = new ArrayList<>();
		String name = "SELECT employee.employee_id, department_id, first_name, last_name, birth_date, gender, hire_date FROM employee WHERE department_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(name, id);
		while(results.next()) {
			Employee employee = mapRowToEmployee(results);
			employees.add(employee);
		}
		return employees;
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() {
		List<Employee> employees = new ArrayList<>();
		String name = "SELECT employee.employee_id, department_id, first_name, last_name, birth_date, gender, hire_date FROM employee LEFT JOIN project_employee ON project_employee.employee_id = employee.employee_id WHERE project_employee.employee_id IS NULL";
		SqlRowSet results = jdbcTemplate.queryForRowSet(name);
		while(results.next()) {
			Employee employee = mapRowToEmployee(results);
			employees.add(employee);
		}
		return  employees;
	}

	@Override
	public List<Employee> getEmployeesByProjectId(Long projectId) {
		List<Employee> employees = new ArrayList<>();
		String name = "SELECT employee.employee_id, department_id, first_name, last_name, birth_date, gender, hire_date FROM employee JOIN project_employee ON project_employee.employee_id = employee.employee_id WHERE project_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(name, projectId);
		while(results.next()) {
			Employee employee = mapRowToEmployee(results);
			employees.add(employee);
		}
		return employees;
	}

	@Override
	public void changeEmployeeDepartment(Long employeeId, Long departmentId) {
		String name = "UPDATE employee SET department_id = ? WHERE employee_id = ?";
		jdbcTemplate.update(name, departmentId, employeeId);
		
	}
	
	private Employee mapRowToEmployee(SqlRowSet results) {
		Employee employee;
		employee = new Employee();
		employee.setId(results.getLong("employee_id"));
		employee.setDepartmentId(results.getLong("department_id"));
		employee.setFirstName(results.getString("first_name"));
		employee.setLastName(results.getString("last_name"));
		employee.setBirthDay(results.getDate("birth_date"));
		employee.setGender(results.getString("gender").charAt(0));
		employee.setHireDate(results.getDate("hire_date"));
		return employee;
	}

}
