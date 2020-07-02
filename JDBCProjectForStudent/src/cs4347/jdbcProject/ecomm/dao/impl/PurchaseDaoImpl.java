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

package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.PurchaseDAO;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.entity.Purchase;
import cs4347.jdbcProject.ecomm.services.PurchaseSummary;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class PurchaseDaoImpl implements PurchaseDAO
{
	private static final String insertSQL = 
            "INSERT INTO PURCHASE (purchase_date, purchase_amt, CUSTOMER_id, PRODUCT_id) VALUES (?, ?, ?, ?);";
	
	private static final String retrieveSQL =
	    	"SELECT * FROM PURCHASE WHERE id = ?;";
	    
    private static final String updateSQL =
    		"UPDATE PURCHASE SET purchase_date = ?, purchase_amt = ?, CUSTOMER_id = ?, PRODUCT_id = ? WHERE id = ?;";
    
    private static final String deleteSQL =
    		"DELETE FROM PURCHASE WHERE id = ?;";
    
    private static final String retrieveByCustomerIDSQL =
    		"SELECT * FROM PURCHASE WHERE CUSTOMER_id = ? ORDER BY purchase_amt;";
    
    private static final String retrieveByProductIDSQL =
    		"SELECT * FROM PURCHASE WHERE PRODUCT_id = ?;";
    
    private static final String retrievePurchaseSummarySQL =
    		"SELECT AVG(purchase_amt), MIN(purchase_amt), MAX(purchase_amt) FROM PURCHASE WHERE CUSTOMER_id = ?;";
    

    @Override
    public Purchase create(Connection connection, Purchase purchase) throws SQLException, DAOException
    {
    	if (purchase.getId() != null) {
            throw new DAOException("Trying to insert Purchase with NON-NULL ID");
        }

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, purchase.getPurchaseDate());
            ps.setDouble(2, purchase.getPurchaseAmount());
            ps.setLong(3, purchase.getCustomerID());
            ps.setLong(4, purchase.getProductID());
            ps.executeUpdate();
            

            // Copy the assigned ID to the customer instance.
            ResultSet keyRS = ps.getGeneratedKeys();
            keyRS.next();
            int lastKey = keyRS.getInt(1);
            purchase.setId((long) lastKey);
            return purchase;
        }
        finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public Purchase retrieve(Connection connection, Long id) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(retrieveSQL);
        	ps.setLong(1, id);
        	ps.executeQuery();
        	
        	ResultSet rs = ps.getResultSet();
        	rs.next();
        	Purchase purchase = null;
        	try{
        		
        		purchase = new Purchase(rs);
        	 
    		} catch (Exception e)
    		{
    			System.out.println(e.getMessage());
    		}    	

        	return purchase;
        }
        finally {
        	if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public int update(Connection connection, Purchase purchase) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(updateSQL);
        	ps.setDate(1, purchase.getPurchaseDate());
            ps.setDouble(2, purchase.getPurchaseAmount());
            ps.setLong(3, purchase.getCustomerID());
            ps.setLong(4, purchase.getProductID());
            ps.setLong(5, purchase.getId());
            ps.executeUpdate();
        	
        	return ps.getUpdateCount();
        }
        finally {
        	if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public int delete(Connection connection, Long id) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(deleteSQL);
            ps.setLong(1, id);
        	ps.executeUpdate();
        	
        	
        	return ps.getUpdateCount();
        }
        finally {
        	if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public List<Purchase> retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(retrieveByCustomerIDSQL);
        	ps.setLong(1, customerID);
        	ps.executeQuery();
        	
        	ResultSet rs = ps.getResultSet();
        	
        	List<Purchase> purchases = new ArrayList<Purchase>();
        	Purchase purchase = null;
        	rs.next();
        	while (rs.getRow() != 0)
        	{       	
            	try{
            		
            		purchase = new Purchase(rs);
            		purchases.add(purchase);          		
            	 
        		} catch (Exception e)
        		{
        			System.out.println(e.getMessage());
        		} 
            	finally{
            		rs.next();
            	}
        	}
        	  	
        	return purchases;
        }
        finally {
        	if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public List<Purchase> retrieveForProductID(Connection connection, Long productID) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(retrieveByProductIDSQL);
        	ps.setLong(1, productID);
        	ps.executeQuery();
        	
        	ResultSet rs = ps.getResultSet();
        	
        	List<Purchase> purchases = new ArrayList<Purchase>();
        	Purchase purchase = null;
        	rs.next();
        	while (rs.getRow() != 0)
        	{       	
            	try{
            		
            		purchase = new Purchase(rs);
            		purchases.add(purchase);          		
            	 
        		} catch (Exception e)
        		{
        			System.out.println(e.getMessage());
        		} 
            	finally{
            		rs.next();
            	}
        	}
        	  	
        	return purchases;
        }
        finally {
        	if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public PurchaseSummary retrievePurchaseSummary(Connection connection, Long customerID) throws SQLException, DAOException
    {
    	PurchaseSummary summary = new PurchaseSummary();
           
        
        PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(retrievePurchaseSummarySQL);
        	ps.setLong(1, customerID);
        	ps.executeQuery();
        	
        	ResultSet rs = ps.getResultSet();
        	rs.next();
        	
        	summary.avgPurchase = rs.getFloat("AVG(purchase_amt)");
        	summary.maxPurchase = rs.getFloat("MAX(purchase_amt)");
        	summary.minPurchase = rs.getFloat("MIN(purchase_amt)");
        	
        	return summary;
        }
        finally {
        	if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
        
    }
	
}
