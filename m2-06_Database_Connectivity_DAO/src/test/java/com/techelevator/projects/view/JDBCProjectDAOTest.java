package com.techelevator.projects.view;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.jdbc.JDBCProjectDAO;

public class JDBCProjectDAOTest extends DAOIntegrationTest{
	JDBCProjectDAO testing;
	JdbcTemplate jdbcTemplate;
	
	

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
	
//	@Test
//	public void removeEmployeeFromProjectTest() {
//		fail("Not yet implemented");
//	}

//	@Test
//	public void addEmployeeToProject(Long projectId, Long employeeId) {
//	fail("Not yet implemented");
//	}
	
}
