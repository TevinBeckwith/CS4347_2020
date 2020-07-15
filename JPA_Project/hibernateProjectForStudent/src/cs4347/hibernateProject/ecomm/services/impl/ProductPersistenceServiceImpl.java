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

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.entity.Product;
import cs4347.hibernateProject.ecomm.services.ProductPersistenceService;
import cs4347.hibernateProject.ecomm.util.DAOException;


public class ProductPersistenceServiceImpl implements ProductPersistenceService
{
	@PersistenceContext 
	private EntityManager em; 
	
	public ProductPersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public void create(Product product) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			em.persist(product);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Product retrieve(Long id) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Product product = em.find(Product.class, id);
			em.getTransaction().commit();
			return product;
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void update(Product product) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Product prod = em.find(Product.class, product.getId());
			prod.setProdName(product.getProdName());
			prod.setProdDescription(product.getProdDescription());
			prod.setProdCategory(product.getProdCategory());
			prod.setProdUPC(product.getProdUPC());
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
			Product product = em.find(Product.class, id);
			em.remove(product);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Product retrieveByUPC(String prodUPC) throws SQLException, DAOException
	{
		try {
			TypedQuery<Product> q = em.createQuery(("SELECT p FROM Product p WHERE prodUPC = " + prodUPC)
					, Product.class);
			Product product = q.getSingleResult();
			return product;
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public List<Product> retrieveByCategory(int prodCategory) throws SQLException, DAOException
	{
		try {
			TypedQuery<Product> q = em.createQuery(("SELECT p FROM Product p WHERE prodCategory = " + Integer.toString(prodCategory))
					, Product.class);
			List<Product> products = q.getResultList();
			return products;
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}
		

}
