package emsi.service;

import emsi.classes.Commande;
import emsi.classes.LigneCommandeProduit;
import emsi.classes.Produit;
import emsi.dao.IDao;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
@Transactional
public class LigneCommandeService implements IDao<LigneCommandeProduit> {
    private final SessionFactory sessionFactory;

    public LigneCommandeService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean create(LigneCommandeProduit o) {
        Session session = sessionFactory.getCurrentSession();
        if (o.getCommande() == null) {
            Commande c = new Commande();
            c.setDate(new Date());
            session.persist(c);
            o.setCommande(c);
        } else {
            if (!session.contains(o.getCommande())) {
                Commande managed = session.merge(o.getCommande());
                o.setCommande(managed);
            }
        }

        Produit p = o.getProduit();
        if (p != null && !session.contains(p)) {
            Produit managedProd = session.merge(p);
            o.setProduit(managedProd);
        }

        session.persist(o);
        return true;
    }

    @Override
    public boolean delete(LigneCommandeProduit o) {
        Session session = sessionFactory.getCurrentSession();
        LigneCommandeProduit attached = session.contains(o) ? o : session.merge(o);
        session.remove(attached);
        return true;
    }

    @Override
    public boolean update(LigneCommandeProduit o) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(o);
        return true;
    }

    @Override
    public LigneCommandeProduit findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.find(LigneCommandeProduit.class, id);
    }

    @Override
    public List<LigneCommandeProduit> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from LigneCommandeProduit", LigneCommandeProduit.class)
                .getResultList();
    }
}