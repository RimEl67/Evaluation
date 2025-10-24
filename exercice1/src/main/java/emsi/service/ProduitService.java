package emsi.service;

import emsi.classes.Commande;
import emsi.classes.Produit;
import emsi.dao.IDao;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class ProduitService implements IDao<Produit> {
    private final SessionFactory sessionFactory;
    private final PersistenceExceptionTranslator persistenceExceptionTranslator;

    public ProduitService(SessionFactory sessionFactory, PersistenceExceptionTranslator persistenceExceptionTranslator) {
        this.sessionFactory = sessionFactory;
        this.persistenceExceptionTranslator = persistenceExceptionTranslator;
    }

    @Override
    public boolean create(Produit o) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(o);
        return true;
    }

    @Override
    public boolean delete(Produit o) {
        Session session = sessionFactory.getCurrentSession();
        Produit attached = session.contains(o) ? o : session.merge(o);
        session.remove(attached);
        return true;
    }

    @Override
    public boolean update(Produit o) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(o);
        return true;
    }

    @Override
    public Produit findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.find(Produit.class, id);
    }

    @Override
    public List<Produit> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Produit", Produit.class)
                .getResultList();
    }

    public List<Produit> findByCategorie(String categorie) {
        return sessionFactory.getCurrentSession()
                .createQuery("from Produit where categorie.code = :categorie", Produit.class)
                .setParameter("categorie", categorie)
                .getResultList();
    }

    public List<Produit> findByToDates(Date debut, Date fin) {
        if (debut == null || fin == null) {
            throw new IllegalArgumentException("Les dates de début et de fin sont obligatoires");
        }
        return sessionFactory.getCurrentSession()
                .createQuery("select distinct l.produit from LigneCommandeProduit l join l.commande c where c.date between :debut and :fin", Produit.class)
                .setParameter("debut", debut)
                .setParameter("fin", fin)
                .getResultList();
    }

    public List<Produit> findByCommande(Commande commande) {
        if (commande == null) {
            throw new IllegalArgumentException("La commande fournie est nulle");
        }
        return sessionFactory.getCurrentSession()
                .createQuery("select distinct l.produit from LigneCommandeProduit l where l.commande =  :commande", Produit.class)
                .setParameter("commande", commande)
                .getResultList();
    }

    public List<Produit> findByPrixGreaterThan() {
        return sessionFactory.getCurrentSession()
                .createNamedQuery("Produit.findByPrixGreaterThan", Produit.class)
                .getResultList();
    }


}