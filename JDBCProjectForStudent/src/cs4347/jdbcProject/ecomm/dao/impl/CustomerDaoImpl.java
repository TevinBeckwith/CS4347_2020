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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import cs4347.jdbcProject.ecomm.dao.CustomerDAO;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class CustomerDaoImpl implements CustomerDAO
{
    private static final String insertSQL = 
            "INSERT INTO CUSTOMER (first_name, last_name, dob, gender, email) VALUES (?, ?, ?, ?, ?);";
    
    private static final String retrieveSQL =
    		"SELECT * FROM CUSTOMER AS C LEFT JOIN ADDRESS AS A ON C.id = A.CUSTOMER_id "
    		+ "LEFT JOIN CREDIT_CARD AS CC ON C.id = CC.CUSTOMER_id WHERE  C.id = ?;";
    
    private static final String updateCustomerSQL =
    		"UPDATE CUSTOMER SET first_name = ?, last_name = ?, dob = ?, gender = ?, email = ? WHERE  id = ?;";
    
    private static final String updateAddressSQL =
    		"UPDATE ADDRESS SET address1 = ?, address2 = ?, city = ?, state = ?, zipcode = ? WHERE CUSTOMER_id = ?;";
    
    private static final String updateCreditCardSQL =
    		"UPDATE CREDIT_CARD SET name = ?, cc_number = ?, exp_date = ?, securityCode = ? WHERE CUSTOMER_id = ?;";
       
    private static final String deleteSQL =
    		"DELETE FROM CUSTOMER WHERE id = ?;";
    
    private static final String retrieveByDOBSQL =
    		"SELECT * FROM CUSTOMER AS C LEFT JOIN ADDRESS AS A ON C.id = A.CUSTOMER_id "
    		+ "LEFT JOIN CREDIT_CARD AS CC ON C.id = CC.CUSTOMER_id WHERE C.dob BETWEEN ? AND ?;";
    
    private static final String retrieveByZipCodeSQL =
    		"SELECT * FROM CUSTOMER AS C LEFT JOIN ADDRESS AS A ON C.id = A.CUSTOMER_id "
    		+ "LEFT JOIN CREDIT_CARD AS CC ON C.id = CC.CUSTOMER_id WHERE A.zipcode = ?;";

    @Override
    public Customer create(Connection connection, Customer customer) throws SQLException, DAOException
    {
        if (customer.getId() != null) {
            throw new DAOException("Trying to insert Customer with NON-NULL ID");
        }

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setDate(3, customer.getDob());
            ps.setString(4, String.valueOf(customer.getGender()));
            ps.setString(5, customer.getEmail());
            ps.executeUpdate();
            

            // Copy the assigned ID to the customer instance.
            ResultSet keyRS = ps.getGeneratedKeys();
            keyRS.next();
            int lastKey = keyRS.getInt(1);
            customer.setId((long) lastKey);
            return customer;
        }
        finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public Customer retrieve(Connection connection, Long id) throws SQLException, DAOException
    {
        PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(retrieveSQL);
        	ps.setLong(1, id);
        	ps.executeQuery();
        	
        	ResultSet rs = ps.getResultSet();
        	rs.next();
        	Customer customer = null;
        	try{
        		
        		customer = new Customer(rs);
        	 
    		} catch (Exception e)
    		{
    			System.out.println(e.getMessage());
    		}    	

        	return customer;
        }
        finally {
        	if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
               
    }

    @Override
    public int update(Connection connection, Customer customer) throws SQLException, DAOException
    {
    	PreparedStatement customerPS = null;
    	PreparedStatement addressPS = null;
    	PreparedStatement creditPS = null;
        try{
        	customerPS = connection.prepareStatement(updateCustomerSQL);
        	customerPS.setString(1, customer.getFirstName());
        	customerPS.setString(2, customer.getLastName());
        	customerPS.setDate(3, customer.getDob());
        	customerPS.setString(4, String.valueOf(customer.getGender()));
        	customerPS.setString(5, customer.getEmail());
        	customerPS.setLong(6, customer.getId());
        	customerPS.executeUpdate();
        	
        	Address address = customer.getAddress();
            
            if (address != null)
            {
            	addressPS = connection.prepareStatement(updateAddressSQL);
            	addressPS.setString(1, address.getAddress1());
            	addressPS.setString(2, address.getAddress2());
            	addressPS.setString(3, address.getCity());
            	addressPS.setString(4, address.getState());
            	addressPS.setString(5, address.getZipcode());
            	addressPS.setLong(6, customer.getId());
            	addressPS.executeUpdate();
            }
            
            CreditCard credit = customer.getCreditCard();
            
            if (credit != null)
            {
            	creditPS = connection.prepareStatement(updateCreditCardSQL);
            	creditPS.setString(1, credit.getName());
            	creditPS.setString(2, credit.getCcNumber());
            	creditPS.setString(3, credit.getExpDate());
            	creditPS.setString(4, credit.getSecurityCode());
            	creditPS.setLong(5, customer.getId());
            	creditPS.executeUpdate();
            }
        	
        	return customerPS.getUpdateCount();
        }
        finally {
        	if (customerPS != null && !customerPS.isClosed()) {
        		customerPS.close();
            }
        	if (addressPS != null && !addressPS.isClosed()) {
        		addressPS.close();
            }
        	if (creditPS != null && !creditPS.isClosed()) {
        		creditPS.close();
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
    public List<Customer> retrieveByZipCode(Connection connection, String zipCode) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(retrieveByZipCodeSQL);
        	ps.setString(1, zipCode);
 
        	ps.executeQuery();
        	
        	ResultSet rs = ps.getResultSet();
        	
        	List<Customer> customers = new ArrayList<Customer>();
        	Customer customer = null;
        	rs.next();
        	while (rs.getRow() != 0)
        	{       	
            	try{
            		
            		customer = new Customer(rs);
            		customers.add(customer);          		
            	 
        		} catch (Exception e)
        		{
        			System.out.println(e.getMessage());
        		} 
            	finally{
            		rs.next();
            	}
        	}
        	  	
        	return customers;
        }
        finally {
        	if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public List<Customer> retrieveByDOB(Connection connection, Date startDate, Date endDate) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(retrieveByDOBSQL);
        	ps.setDate(1, startDate);
        	ps.setDate(2, endDate);
        	ps.executeQuery();
        	
        	ResultSet rs = ps.getResultSet();
        	
        	List<Customer> customers = new ArrayList<Customer>();
        	Customer customer = null;
        	rs.next();
        	while (rs.getRow() != 0)
        	{       	
            	try{
            		
            		customer = new Customer(rs);
            		customers.add(customer);          		
            	 
        		} catch (Exception e)
        		{
        			System.out.println(e.getMessage());
        		} 
            	finally{
            		rs.next();
            	}
        	}
        	  	
        	return customers;
        }
        finally {
        	if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }
	
}
