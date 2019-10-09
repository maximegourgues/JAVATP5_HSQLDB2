package simplejdbc;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import testingwithhsqldb.DAO;

public class HsqlDBTest {
	private DAO myDAO; // L'objet à tester
	private DataSource myDataSource; // La source de données à utiliser
	private static Connection myConnection ; // La connection à la BD de test
	

	@Before
	public void setUp() throws SQLException, IOException, SqlToolError {
		// On utilise la base de données de test
		myDataSource = getTestDataSource();
		myConnection = myDataSource.getConnection();
		// On crée le schema de la base de test
		executeSQLScript(myConnection, "schema.sql");
		// On y met des données
		executeSQLScript(myConnection, "bigtestdata.sql");		
		
		myDAO = new DAO(myDataSource);
	}

	@After
	public void tearDown() throws IOException, SqlToolError, SQLException {
		myConnection.close(); // La base de données de test est détruite ici
             	myDAO = null; // Pas vraiment utile
	}
	
	private void executeSQLScript(Connection connexion, String filename)  throws IOException, SqlToolError, SQLException {
		// On initialise la base avec le contenu d'un fichier de test
		String sqlFilePath = HsqlDBTest.class.getResource(filename).getFile();
		SqlFile sqlFile = new SqlFile(new File(sqlFilePath));

		sqlFile.setConnection(connexion);
		sqlFile.execute();
		sqlFile.closeReader();		
	}
	
	/**
	 * Test of numberOfCustomers method, of class DAO.
	 * @throws simplejdbc.DAOException
	 */
	@Test
	public void testNumberOfCustomers() throws DAOException {
		int result = myDAO.numberOfCustomers();
		assertEquals(13, result);
	}

	/**
	 * Test of numberOfOrdersForCustomer method, of class DAO.
	 * @throws simplejdbc.DAOException
	 */
	@Test
	public void testNumberOfOrdersForCustomer() throws DAOException {
		int customerId = 36;
		int expResult = 2;
		int result = myDAO.numberOfOrdersForCustomer(customerId);
		assertEquals(expResult, result); // Le client 36 a 2 bons de commande
	}

	/**
	 * Test of findCustomer method, of class DAO.
	 * @throws simplejdbc.DAOException
	 */
	@Test
	public void testFindCustomer() throws DAOException {
		int customedID = 1;
		CustomerEntity result = myDAO.findCustomer(customedID);
		assertEquals("Jumbo Eagle Corp", result.getName());
	}

	/**
	 * Test of customersInState method, of class DAO.
	 * @throws simplejdbc.DAOException
	 */
	@Test
	public void testCustomersInState() throws DAOException {
		String state = "CA";
		List<CustomerEntity> result = myDAO.customersInState(state);
		assertEquals(4, result.size());
	}

	/**
	 * Test of deleteCustomer method, of class DAO.
	 * @throws simplejdbc.DAOException
	 */
	@Test
	public void testDeleteUnknownCustomer () throws DAOException {
		int id = 999;
		assertEquals(0, myDAO.deleteCustomer(id));
	}

	/**
	 * Test of deleteCustomer method, of class DAO.
	 * @throws simplejdbc.DAOException
	 */
	@Test
	public void testDeleteCustomerWithoutOrder () throws DAOException {
		int id = 25; // Le client 25 n'a pas de bon de commande
		assertEquals(1, myDAO.deleteCustomer(id));
	}

	/**
	 * Test of deleteCustomer method, of class DAO.
	 */
	@Test
	public void testDeleteCustomerWithOrder () {
		int id = 1; // Le client 1 a des bons de commande
		try {
			myDAO.deleteCustomer(id); // Cette ligne doit lever une exception
			fail(); // On ne doit pas passer par ici
		} catch (DAOException e) {
			// On doit passer par ici, violation d'intégrité référentielle
		}
	}

	public static DataSource getTestDataSource() {
		org.hsqldb.jdbc.JDBCDataSource ds = new org.hsqldb.jdbc.JDBCDataSource();
		ds.setDatabase("jdbc:hsqldb:mem:testcase;shutdown=true");
		ds.setUser("sa");
		ds.setPassword("sa");
		return ds;
	}	
	
}
