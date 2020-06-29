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

import cs4347.jdbcProject.ecomm.dao.ProductDAO;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class ProductDaoImpl implements ProductDAO
{
	private static final String insertSQL = 
            "INSERT INTO PRODUCT (prod_name, prod_desc, prod_category, prod_upc) VALUES (?, ?, ?, ?);";
	
	private static final String retrieveSQL =
	    	"SELECT * FROM PRODUCT WHERE id = ?;";
	    
    private static final String updateSQL =
    		"UPDATE PRODUCT SET prod_name = ?, prod_desc = ?, prod_category = ?, prod_upc = ? WHERE id = ?;";
    
    private static final String deleteSQL =
    		"DELETE FROM PRODUCT WHERE id = ?;";
    
    private static final String retrieveByCategorySQL =
    		"SELECT * FROM PRODUCT WHERE prod_category = ?;";
    
    private static final String retrieveByUPCSQL =
    		"SELECT * FROM PRODUCT WHERE prod_upc = ?;";
	
    @Override
    public Product create(Connection connection, Product product) throws SQLException, DAOException
    {
    	if (product.getId() != null) {
            throw new DAOException("Trying to insert Customer with NON-NULL ID");
        }

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getProdName());
            ps.setString(2, product.getProdDescription());
            ps.setInt(3, product.getProdCategory());
            ps.setString(4, String.valueOf(product.getProdUPC()));
            ps.executeUpdate();
            

            // Copy the assigned ID to the customer instance.
            ResultSet keyRS = ps.getGeneratedKeys();
            keyRS.next();
            int lastKey = keyRS.getInt(1);
            product.setId((long) lastKey);
            return product;
        }
        finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public Product retrieve(Connection connection, Long id) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(retrieveSQL);
        	ps.setLong(1, id);
        	ps.executeQuery();
        	
        	ResultSet rs = ps.getResultSet();
        	rs.next();
        	Product product = null;
        	try{
        		
        		product = new Product(rs);
        	 
    		} catch (Exception e)
    		{
    			System.out.println(e.getMessage());
    		}    	

        	return product;
        }
        finally {
        	if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public int update(Connection connection, Product product) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(updateSQL);
        	ps.setString(1, product.getProdName());
            ps.setString(2, product.getProdDescription());
            ps.setInt(3, product.getProdCategory());
            ps.setString(4, String.valueOf(product.getProdUPC()));
            ps.setLong(5, product.getId());
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
    public List<Product> retrieveByCategory(Connection connection, int category) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(retrieveByCategorySQL);
        	ps.setInt(1, category);
        	ps.executeQuery();
        	
        	ResultSet rs = ps.getResultSet();
        	
        	List<Product> products = new ArrayList<Product>();
        	Product product = null;
        	rs.next();
        	while (rs.getRow() != 0)
        	{       	
            	try{
            		
            		product = new Product(rs);
            		products.add(product);          		
            	 
        		} catch (Exception e)
        		{
        			System.out.println(e.getMessage());
        		} 
            	finally{
            		rs.next();
            	}
        	}
        	  	
        	return products;
        }
        finally {
        	if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public Product retrieveByUPC(Connection connection, String upc) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(retrieveByUPCSQL);
        	ps.setString(1, upc);
        	ps.executeQuery();
        	
        	ResultSet rs = ps.getResultSet();

        	Product product = null;
        	rs.next();
        	
        	try{
        			product = new Product(rs);
        	 
    		} catch (Exception e)
    		{
    			System.out.println(e.getMessage());
    		} 
        	
        	return product;
        }
       
        finally {
        	if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

}
