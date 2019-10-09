package testingwithhsqldb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class DAO {
	private final DataSource myDataSource;
	
	public DAO(DataSource dataSource) {
		myDataSource = dataSource;
	}

	/**
	 * Renvoie le nom d'un client à partir de son ID
	 * @param id la clé du client à chercher
	 * @return le nom du client (LastName) ou null si pas trouvé
	 * @throws SQLException 
	 */
	public String nameOfProduct(int id) throws SQLException {
		String result = null;
		
		String sql = "SELECT Name FROM Product WHERE ID = ?";
		try (Connection myConnection = myDataSource.getConnection(); 
		     PreparedStatement statement = myConnection.prepareStatement(sql)) {
			statement.setInt(1, id); // On fixe le 1° paramètre de la requête
			try ( ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					// est-ce qu'il y a un résultat ? (pas besoin de "while", 
                                        // il y a au plus un enregistrement)
					// On récupère les champs de l'enregistrement courant
					result = resultSet.getString("Name");
				}
			}
		}
		// dernière ligne : on renvoie le résultat
		return result;
        
	}

        public void newProduct(Product product) throws DAOException{
            
            String sql = "INSERT INTO Product VALUES(?,?,?)";
            try (Connection connection = myDataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql)){
                        stmt.setInt(1, product.getProductId());
                        stmt.setString(2, product.getName());
                        stmt.setFloat(3, product.getPrice());
                        stmt.executeQuery();
                        
			// Définir la valeur du paramètre
			
		} catch (SQLException ex) {
			Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
			throw new DAOException(ex.getMessage());
		}
        
	}
        
        Product findProduct(int productID) throws DAOException {
            
            String sql = "SELECT Name,Price FROM PRODUCT WHERE ID = ?";
                try (Connection connection = myDataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql)){
                        stmt.setInt(1, productID);
                        ResultSet rs = stmt.executeQuery();
                        if(rs.next()){
                            String name = rs.getString("Name");
                            float price = rs.getFloat("Price");
                            Product result = new Product(productID,name,price);
                            return result;
                        }
			// Définir la valeur du paramètre
			return null;
		} catch (SQLException ex) {
			Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
			throw new DAOException(ex.getMessage());
		}
	}
	
}
