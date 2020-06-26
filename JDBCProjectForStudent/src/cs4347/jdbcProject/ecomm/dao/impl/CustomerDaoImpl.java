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
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class CustomerDaoImpl implements CustomerDAO
{
    private static final String insertSQL = 
            "INSERT INTO CUSTOMER (first_name, last_name, dob, gender, email) VALUES (?, ?, ?, ?, ?);";
    
    private static final String retrieveSQL =
    		"SELECT * FROM CUSTOMER WHERE id = ?;";
    
    private static final String updateSQL =
    		"UPDATE CUSTOMER SET first_name = ?, last_name = ?, dob = ?, gender = ?, email = ? WHERE  id = ?;";
    
    private static final String deleteSQL =
    		"DELETE FROM CUSTOMER WHERE id = ?;";
    
    private static final String retrieveByDOBSQL =
    		"SELECT * FROM CUSTOMER WHERE dob BETWEEN ? AND ?;";
    
    private static final String retrieveByZipCodeSQL =
    		"SELECT * FROM CUSTOMER WHERE dob BETWEEN ? AND ?;";

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
    	PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(updateSQL);
        	ps.setString(1, customer.getFirstName());
        	ps.setString(2, customer.getLastName());
        	ps.setDate(3, customer.getDob());
        	ps.setString(4, String.valueOf(customer.getGender()));
            ps.setString(5, customer.getEmail());
            ps.setLong(6, customer.getId());
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
    public List<Customer> retrieveByZipCode(Connection connection, String zipCode) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try{
        	ps = connection.prepareStatement(retrieveByDOBSQL);
 
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
