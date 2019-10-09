package  testingwithhsqldb;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;
import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


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
     * @throws java.sql.SQLException	 
	 */
	@Test
	public void testNameOfProduct() throws SQLException {
		String result = myDAO.nameOfProduct(1);
		assertEquals("Chair Shoe", result);
	}
	/**
	 * Test of findCustomer method, of class DAO.
     * @throws testingwithhsqldb.DAOException
	 */
	@Test
	public void testFindProduct() throws DAOException {
		int productID = 0;
		Product result = myDAO.findProduct(productID);
		assertEquals("Iron Iron", result.getName());
                assertEquals(54f,result.getPrice(),0.001f);
	}
        
        @Test
        public void testNewProduct() throws DAOException {
            Product result = new Product(54, "Yoyo Cool", 5f);
            myDAO.newProduct(result);
            assertEquals(myDAO.findProduct(54),result);
        }
        
        @Test (expected = DAOException.class)
        public void testProductExisting() throws DAOException {
            Product result = new Product(1,"Yoyo Cool",5f);
            myDAO.newProduct(result);
        }
        
        @Test(expected = DAOException.class)
        public void testPositivePrice() throws SQLException, DAOException {
            Product result = new Product(60,"Yoyo Super",-1f);
            myDAO.newProduct(result);
        }


	public static DataSource getTestDataSource() {
		org.hsqldb.jdbc.JDBCDataSource ds = new org.hsqldb.jdbc.JDBCDataSource();
		ds.setDatabase("jdbc:hsqldb:mem:testcase;shutdown=true");
		ds.setUser("sa");
		ds.setPassword("sa");
		return ds;
	}	
	
}
