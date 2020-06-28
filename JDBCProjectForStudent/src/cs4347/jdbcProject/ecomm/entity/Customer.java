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

package cs4347.jdbcProject.ecomm.entity;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer
{

	private Long id;

	private String firstName;
	private String lastName;
	private Character gender;
	private Date dob;
	private String email;
	private Address address;
	private CreditCard creditCard;

	public Customer()
	{
	}
	
	
	public Customer(ResultSet rs) throws SQLException
	{
		this.id = rs.getLong("id");
		this.firstName = rs.getString("first_name");
		this.lastName = rs.getString("last_name");
		this.gender = Character.toUpperCase(rs.getString("gender").charAt(0));
		this.dob = rs.getDate("dob");
		this.email = rs.getString("email");
		this.address = new Address(rs);
		this.creditCard = new CreditCard(rs);
	}
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public Character getGender()
	{
		return gender;
	}

	public void setGender(Character gender)
	{
		this.gender = gender;
	}

	public Date getDob()
	{
		return dob;
	}

	public void setDob(Date dob)
	{
		this.dob = dob;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Address getAddress()
	{
		return address;
	}

	public void setAddress(Address address)
	{
		this.address = address;
	}

	public CreditCard getCreditCard()
	{
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard)
	{
		this.creditCard = creditCard;
	}
}
