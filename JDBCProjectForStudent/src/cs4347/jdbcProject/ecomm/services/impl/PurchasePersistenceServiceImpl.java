/* NOTICE: All materials provided by this project, and materials derived 
 * from the project, are the property of the University of Texas. 
 * Project materials, or those derived from the materials, cannot be placed 
 * into publicly accessible locations on the web. Project materials cannot 
 * be shared with other project teams. Making project materials publicly 
 * accessible, or sharing with other project teams will result in the 
 * failure of the team responsible and any team that uses the shared materials. 
 * Sharing project materials or using shared materials will also result 
 * in the reporting of every team member to the Provost Office for academic 
 * dishonesty. 
 */ 

package cs4347.jdbcProject.ecomm.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import cs4347.jdbcProject.ecomm.dao.AddressDAO;
import cs4347.jdbcProject.ecomm.dao.CreditCardDAO;
import cs4347.jdbcProject.ecomm.dao.CustomerDAO;
import cs4347.jdbcProject.ecomm.dao.PurchaseDAO;
import cs4347.jdbcProject.ecomm.dao.ProductDAO;
import cs4347.jdbcProject.ecomm.dao.impl.AddressDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.CreditCardDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.CustomerDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.PurchaseDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.ProductDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.entity.Purchase;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.services.PurchasePersistenceService;
import cs4347.jdbcProject.ecomm.services.PurchaseSummary;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class PurchasePersistenceServiceImpl implements PurchasePersistenceService
{
	@Override
    public Purchase create(Purchase purchase) throws SQLException, DAOException
    {
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();

        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);  // Starts new Transaction on Connection
            Purchase purch = purchaseDAO.create(connection, purchase);

            if (purch.getCustomerID() == null) {
                throw new DAOException("Purchases must include a Customer instance.");
            }

            if (purch.getProductID() == null) {
                throw new DAOException("Purchases must include a Product instance.");
            }

            connection.commit();
            return purch;
        }
        catch (Exception ex) {
            connection.rollback();
            throw ex;
        }
        finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    @Override
    public Purchase retrieve(Long id) throws SQLException, DAOException
    {
    	 PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
         Connection connection = dataSource.getConnection();
         
         
         try{
         	connection.setAutoCommit(false);
         	Purchase purchase = purchaseDAO.retrieve(connection, id);
             return purchase;
             
         } catch (Exception e) {
         	connection.rollback();
         	throw e;
         }
         finally {
             if (connection != null) {
                 connection.setAutoCommit(true);
             }
             if (connection != null && !connection.isClosed()) {
                 connection.close();
             }
         }
    }

    @Override
    public int update(Purchase purchase) throws SQLException, DAOException
    {
    	PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
    	Connection connection = dataSource.getConnection();
    	
    	try{
    		connection.setAutoCommit(false);
    		return purchaseDAO.update(connection, purchase);
    		
    	} catch (Exception e) {
        	connection.rollback();
        	throw e;
        }
    	finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    @Override
    public int delete(Long id) throws SQLException, DAOException
    {
    	PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
    	Connection connection = dataSource.getConnection();
    	
    	try {
    		connection.setAutoCommit(false);
    		return purchaseDAO.delete(connection, id);
    		
    	} catch (Exception e) {
        	connection.rollback();
        	throw e;
        }
    	finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    @Override
    public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException
    {
    	 PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
         Connection connection = dataSource.getConnection();
         
         try{
         	connection.setAutoCommit(false);
         	List <Purchase> purchase = purchaseDAO.retrieveForCustomerID(connection, customerID);
            return purchase;
             
         } catch (Exception e) {
         	connection.rollback();
         	throw e;
         }
         finally {
             if (connection != null) {
                 connection.setAutoCommit(true);
             }
             if (connection != null && !connection.isClosed()) {
                 connection.close();
             }
         }
    }

    @Override
    public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException
    {
    	PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
        Connection connection = dataSource.getConnection();
        
        try{
        	connection.setAutoCommit(false);
        	PurchaseSummary summary = purchaseDAO.retrievePurchaseSummary(connection, customerID);
           return summary;
            
        } catch (Exception e) {
        	connection.rollback();
        	throw e;
        }
        finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    @Override
    public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException
    {
    	PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
        Connection connection = dataSource.getConnection();
        
        try{
        	connection.setAutoCommit(false);
        	List <Purchase> purchase = purchaseDAO.retrieveForProductID(connection, productID);
           return purchase;
            
        } catch (Exception e) {
        	connection.rollback();
        	throw e;
        }
        finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    private DataSource dataSource;

	public PurchasePersistenceServiceImpl(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

}
