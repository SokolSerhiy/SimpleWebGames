package skylab.dao.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.transaction.annotation.Transactional;

import skylab.dao.GeneralDao;

/**
 * General Dao class implementation. Made as an abstract class. C - input class,
 * N - Number for id
 */

public abstract class GeneralDaoImpl<C, N extends Number> implements
		GeneralDao<C, N> {

	protected Class<C> entityClass;

	public GeneralDaoImpl(Class<C> entityClass) {
		this.entityClass = entityClass;
	}

	@PersistenceContext
	protected EntityManager em;

	/**
	 * Creates new entry of the entity in DB
	 */
	@Transactional
	public void create(C entity) {
		em.persist(entity);
	}

	/**
	 * Updates the existing entity
	 */
	@Transactional
	public void update(C entity) {
		em.merge(entity);
	}

	/**
	 * Returns the list of all entities
	 */
	@Transactional
	public List<C> getAll() {
		CriteriaQuery<C> cq = em.getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
	}

	/**
	 * Deletes the entity
	 */
	@Transactional
	public void delete(C entity) {
		em.remove(entity);
	}

	/**
	 * Returns the entity by ID-number
	 */
	public C findOneById(N id) {
		return em.find(entityClass, id);
	}
}
