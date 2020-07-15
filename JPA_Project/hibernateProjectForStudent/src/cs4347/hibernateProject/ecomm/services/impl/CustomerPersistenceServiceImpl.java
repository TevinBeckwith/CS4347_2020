/* NOTICE: All materials provided by this project, and materials derived 
 * from the project, are the property of the University of Texas. 
 * Project materials, or those derived from the materials, cannot be placed 
 * into publicly accessible locations on the web. Project materials cannot 
 * be shared with other project teams. Making project materials publicly 
 * accessible, or sharing with other project teams will result in the 
 * failure of the team responsible and any team that uses the shared materials. 
 * Sharing project materials or using shared materials will also result 
 * in the reporting of all team members for academic dishonesty. 
 */ 
 
package cs4347.hibernateProject.ecomm.services.impl;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.services.CustomerPersistenceService;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class CustomerPersistenceServiceImpl implements CustomerPersistenceService
{
	@PersistenceContext 
	private EntityManager em; 
	
	public CustomerPersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	/**
	 */
	@Override
	public void create(Customer customer) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			em.persist(customer);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Customer retrieve(Long id) 
	{
		try {
			em.getTransaction().begin();
			Customer cust = em.find(Customer.class, id);
			em.getTransaction().commit();
			return cust;
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void update(Customer c1) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Customer cust = em.find(Customer.class, c1.getId());
			cust.setDob(c1.getDob());
			cust.setEmail(c1.getEmail());
			cust.setFirstName(c1.getFirstName());
			cust.setLastName(c1.getLastName());
			cust.setGender(c1.getGender());
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void delete(Long id) throws SQLException, DAOException
	{
		try {	
			em.getTransaction().begin();
			Customer cust = em.find(Customer.class, id);
			em.remove(cust);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public List<Customer> retrieveByZipCode(String zipCode) throws SQLException, DAOException
	{
		try {
			List<Customer> customers = em.createQuery("SELECT c FROM Customer c INNER JOIN c.addressList WHERE zipcode = :zipcode")
					.setParameter("zipcode", zipCode)
					.getResultList();			
			return customers;
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public List<Customer> retrieveByDOB(Date startDate, Date endDate) throws SQLException, DAOException
	{
		try {
			List<Customer> customers = em.createQuery("SELECT c FROM Customer c INNER JOIN c.addressList WHERE dob BETWEEN :startDate AND :endDate")
					.setParameter("startDate", startDate)
					.setParameter("endDate", endDate)
					.getResultList();			
			return customers;
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}
}
