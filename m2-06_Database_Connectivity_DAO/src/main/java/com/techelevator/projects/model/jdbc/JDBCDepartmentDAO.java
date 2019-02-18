package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;


import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;

public class JDBCDepartmentDAO implements DepartmentDAO {
	
	private JdbcTemplate jdbcTemplate;


	
	public JDBCDepartmentDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Department> getAllDepartments() {
		List<Department> departments = new ArrayList<>();
		String name = "SELECT department_id, name FROM department";
		SqlRowSet results = jdbcTemplate.queryForRowSet(name);
		while(results.next()) {
			Department theDept = mapRowToDepartment(results);
			departments.add(theDept);
		}
		return  departments;
	}

	@Override
	public List<Department> searchDepartmentsByName(String nameSearch) {
		List<Department> departments = new ArrayList<>();
		String name = "SELECT department_id, name FROM department WHERE name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(name, nameSearch);
		while(results.next()) {
			Department theDept = mapRowToDepartment(results);
			departments.add(theDept);
		}
		return departments;
	}

	@Override
	public void saveDepartment(Department updatedDepartment) {
		String name = "UPDATE department SET name = ? WHERE department_id = ?";
		jdbcTemplate.update(name, updatedDepartment.getName(),
				updatedDepartment.getId());
		
	}

	@Override
	public Department createDepartment(Department newDepartment) {
		String name = "INSERT INTO department(department_id, name)" +
					  "VALUES (?,?)";
		newDepartment.setId(getNextDeptId());
		jdbcTemplate.update(name, newDepartment.getId(), newDepartment.getName());
		return newDepartment;

	}

	@Override
	public Department getDepartmentById(Long id) {
		Department department = new Department();
		String name = "SELECT department_id, name FROM department WHERE department_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(name, id);
		if(results.next()) {
			department = mapRowToDepartment(results);
		}
		return department;
	}

	
	private Department mapRowToDepartment(SqlRowSet results) {
		Department theDept;
		theDept = new Department();
		theDept.setId(results.getLong("department_id"));
		theDept.setName(results.getString("name"));
		return theDept;
	}
	
	private long getNextDeptId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_department_id')");
		if(nextIdResult.next()) {
			return nextIdResult.getLong(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new department");
		}
	}
	
}
