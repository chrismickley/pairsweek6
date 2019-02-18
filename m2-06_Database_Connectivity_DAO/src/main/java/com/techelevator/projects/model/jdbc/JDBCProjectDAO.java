package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;

public class JDBCProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCProjectDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Project> getAllActiveProjects() {
		List<Project> projects = new ArrayList<>();
		String name = "SELECT project_id, name, to_date, from_date FROM project";
		SqlRowSet results = jdbcTemplate.queryForRowSet(name);
		while(results.next()) {
			Project project = mapRowToProject(results);
			projects.add(project);
		}
		return  projects;
	}

	@Override
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
	String deleteEmployee = "DELETE FROM project_employee WHERE project_id = ? AND employee_id = ?";
		jdbcTemplate.update(deleteEmployee, projectId, employeeId);	
	}
	

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		String name = "INSERT INTO project_employee (project_id, employee_id)" +
				  "VALUES (?,?)";
	jdbcTemplate.update(name, projectId, employeeId);
	}
	
	private Project mapRowToProject(SqlRowSet results) {
		Project project;
		project = new Project();
		project.setId(results.getLong("project_id"));
		project.setName(results.getString("name"));
		project.setStartDate(results.getDate("from_date"));
		project.setEndDate(results.getDate("to_date"));
		return project;
	}
	


}
