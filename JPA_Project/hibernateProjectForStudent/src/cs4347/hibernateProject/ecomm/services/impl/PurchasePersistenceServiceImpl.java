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
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import cs4347.hibernateProject.ecomm.entity.Purchase;
import cs4347.hibernateProject.ecomm.services.PurchasePersistenceService;
import cs4347.hibernateProject.ecomm.services.PurchaseSummary;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class PurchasePersistenceServiceImpl implements PurchasePersistenceService
{
	@PersistenceContext 
	private EntityManager em; 
	
	public PurchasePersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public void create(Purchase purchase) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			em.persist(purchase);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Purchase retrieve(Long id) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Purchase purchase = em.find(Purchase.class, id);
			em.getTransaction().commit();
			return purchase;
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void update(Purchase purchase) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Purchase p2 = em.find(Purchase.class, purchase.getId());
			p2.setPurchaseDate(purchase.getPurchaseDate());
			p2.setPurchaseAmount(purchase.getPurchaseAmount());
			p2.setCustomer(purchase.getCustomer());
			p2.setProduct(purchase.getProduct());
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
			Purchase purchase = em.find(Purchase.class, id);
			em.remove(purchase);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException
	{
		try {
			String test = String.valueOf(customerID);
			TypedQuery<Purchase> q = em.createQuery(("SELECT p FROM Purchase p WHERE Customer_ID = " + test)
					, Purchase.class);
			List<Purchase> purchases = q.getResultList();
			return purchases;
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException
	{
		PurchaseSummary summary = new PurchaseSummary();
    	
        	List<Purchase> purchases = retrieveForCustomerID(customerID);
        
       	 	summary.maxPurchase = (float)purchases.get(purchases.size() - 1).getPurchaseAmount();
        	summary.minPurchase = (float)purchases.get(0).getPurchaseAmount();
        	float avg = 0;
        	for(int a = 0; a < purchases.size(); a++)
        	{
        		avg = avg+(float)purchases.get(a).getPurchaseAmount();
        	}
        	avg = avg/(float)purchases.size();
        	summary.avgPurchase = avg;
        	return summary; 
	}

	@Override
	public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException
	{
		try {
			String test = String.valueOf(productID);
			TypedQuery<Purchase> q = em.createQuery(("SELECT p FROM Purchase p WHERE Product_ID = " + test)
					, Purchase.class);
			List<Purchase> purchases = q.getResultList();
			return purchases;
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}
}
