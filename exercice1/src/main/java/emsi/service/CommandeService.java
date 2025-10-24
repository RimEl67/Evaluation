package emsi.service;

import emsi.classes.Commande;
import emsi.dao.IDao;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
@Transactional
public class CommandeService implements IDao<Commande> {
    private final SessionFactory sessionFactory;

    public CommandeService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean create(Commande o) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(o);
        return true;
    }

    @Override
    public boolean delete(Commande o) {
        Session session = sessionFactory.getCurrentSession();
        Commande attached = session.contains(o) ? o : session.merge(o);
        session.remove(attached);
        return true;
    }

    @Override
    public boolean update(Commande o) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(o);
        return true;
    }

    @Override
    public Commande findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        List<Commande> results = session.createQuery(
                        "select distinct c from Commande c " +
                                "left join fetch c.lignes l " +
                                "left join fetch l.produit " +
                                "where c.id = :id", Commande.class)
                .setParameter("id", id)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Commande> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Commande", Commande.class)
                .getResultList();
    }
}