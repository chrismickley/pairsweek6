package com.techelevator.projects.view;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.jdbc.JDBCProjectDAO;

public class JDBCProjectDAOTest extends DAOIntegrationTest {
	JDBCProjectDAO testing;
	JdbcTemplate jdbcTemplate;

	
	@Before
	public void setup() {

		testing = new JDBCProjectDAO(getDataSource());
	}

	@Test
	public void getAllActiveProjectsTest() {
		jdbcTemplate = new JdbcTemplate(getDataSource());
		List<Project> project = testing.getAllActiveProjects();
		SqlRowSet newResults = jdbcTemplate.queryForRowSet("SELECT count(*) FROM project");
		newResults.next();
		assertEquals(newResults.getInt("count"), project.size());
	}

//	@Test
//	public void get_all_active_projects_gets_1_project () {
//		// Arrange - setup
//			//Create a New Project that is active
//		List<Project> projects = dao.getAllActiveProjects();
//		int originalSie = projects.size();
//		insertProject("Test","2019-02-01", "2022-03-01"); // This is going to fail in about 3 years
		
//	}
//	private int insertProject (String name, String fromDate, String toDate) {
//		
//		String projectSql = "INSERT INTO...";
//				SqlRowSet results = jdbcTemplate.queryForRowSet(projectSql);
//		results.next();
//		int projectId = results.getInt(1);
//		return projectId;
//	}
	
	@Test
	public void removeEmployeeFromProjectTest() {
		jdbcTemplate = new JdbcTemplate(getDataSource());
		testing.addEmployeeToProject((long) 1, (long) 1);
		SqlRowSet resultsAfterAddBeforeRemove = jdbcTemplate.queryForRowSet("SELECT count(*) FROM project");
		resultsAfterAddBeforeRemove.next();
		testing.removeEmployeeFromProject((long) 1, (long) 1);
		SqlRowSet resultsAfterAddAfterRemove = jdbcTemplate.queryForRowSet("SELECT count(*) FROM project");
		resultsAfterAddAfterRemove.next();
		assertEquals(resultsAfterAddBeforeRemove.getInt("count"), resultsAfterAddAfterRemove.getInt("count"));
	}

	@Test
	public void addEmployeeToProject() {
		jdbcTemplate = new JdbcTemplate(getDataSource());
		testing.addEmployeeToProject((long) 1, (long) 1);
		SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT COUNT(*) FROM project_employee");
		result.next();
		assertEquals(13, result.getInt("count"));
	}
	

}
