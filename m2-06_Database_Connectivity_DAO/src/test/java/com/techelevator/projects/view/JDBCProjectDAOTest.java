package com.techelevator.projects.view;

import static org.junit.Assert.*;


import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;


import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.jdbc.JDBCProjectDAO;

public class JDBCProjectDAOTest extends DAOIntegrationTest{
	JDBCProjectDAO testing;
	JdbcTemplate jdbcTemplate;
	String projectCount;
	

	@Before
	public void setup() {
		testing = new JDBCProjectDAO(getDataSource());
//		jdbcTemplate = new JdbcTemplate(getDataSource());

	}
	
	@Test
	public void getAllActiveProjectsTest() {
		List <Project> project = testing.getAllActiveProjects();
		assertEquals(6, project.size());
	}
	
	@Test
	public void removeEmployeeFromProjectTest() {
		jdbcTemplate = new JdbcTemplate(getDataSource());
		testing.removeEmployeeFromProject((long)6, (long)11);
		SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT COUNT(*) FROM project_employee");
		result.next();
		assertEquals(11, result.getInt("count"));
	}
	

	@Test
	public void addEmployeeToProject() {
		jdbcTemplate = new JdbcTemplate(getDataSource());
		testing.addEmployeeToProject((long)1, (long)1);
		
		SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT COUNT(*) FROM project_employee");
		result.next();
		assertEquals(13, result.getInt("count"));

	}
	

}
