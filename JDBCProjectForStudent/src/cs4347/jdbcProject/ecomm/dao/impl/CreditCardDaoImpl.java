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

import cs4347.jdbcProject.ecomm.dao.CreditCardDAO;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.util.DAOException;


public class CreditCardDaoImpl implements CreditCardDAO
{
	private static final String insertSQL = 
            "INSERT INTO CREDIT_CARD (name, cc_number, exp_date, securityCode, CUSTOMER_id) VALUES (?, ?, ?, ?, ?);";
	
	private static final String retrieveSQL =
	    		"SELECT * FROM CREDIT_CARD WHERE CUSTOMER_id = ?;";
	
    private static final String deleteSQL =
    		"DELETE FROM CREDIT_CARD WHERE CUSTOMER_id = ?;";

    @Override
    public CreditCard create(Connection connection, CreditCard creditCard, Long customerID) throws SQLException, DAOException
    {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, creditCard.getName());
            ps.setString(2, creditCard.getCcNumber());
            ps.setString(3, creditCard.getExpDate());
            ps.setString(4, creditCard.getSecurityCode());
            ps.setLong(5, customerID);
            ps.executeUpdate();
            

           
            return creditCard;
        }
        finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public CreditCard retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(retrieveSQL);
        	ps.setLong(1, customerID);
        	ps.executeQuery();
        	
        	ResultSet rs = ps.getResultSet();
        	rs.next();
        	CreditCard card = null;
        	try{
        		
        		card = new CreditCard(rs);
        	 
    		} catch (Exception e)
    		{
    			System.out.println(e.getMessage());
    		}    	

        	return card;
        }
        finally {
        	if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public void deleteForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(deleteSQL);
            ps.setLong(1, customerID);
        	ps.executeUpdate();
        	
        }
        finally {
        	if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
        
    }

}
