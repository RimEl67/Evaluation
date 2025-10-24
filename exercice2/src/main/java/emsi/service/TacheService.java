package emsi.service;

import emsi.classes.Tache;
import emsi.dao.IDao;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class TacheService implements IDao<Tache> {

    private final SessionFactory sessionFactory;

    public TacheService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean create(Tache o) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(o);
        return true;
    }

    @Override
    public boolean delete(Tache o) {
        Session session = sessionFactory.getCurrentSession();
        Tache attached = session.contains(o) ? o : session.merge(o);
        session.remove(attached);
        return true;
    }

    @Override
    public boolean update(Tache o) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(o);
        return true;
    }

    @Override
    public Tache findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.find(Tache.class, id);
    }

    @Override
    public List<Tache> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from Tache", Tache.class).getResultList();
    }

    public List<Tache> findByPriceGreaterThan1000() {
        Session session = sessionFactory.getCurrentSession();
        return session.createNamedQuery("Tache.findByPriceGreaterThan", Tache.class)
                .setParameter("price", 1000)
                .getResultList();
    }

    public List<Tache> findCompletedBetween(LocalDate start, LocalDate end) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "select distinct et.tache from EmployeTache et " +
                                "where et.dateDebutReelle >= :start and et.dateFinReelle <= :end",
                        Tache.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}