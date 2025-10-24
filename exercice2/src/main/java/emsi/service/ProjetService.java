package emsi.service;

import emsi.classes.EmployeTache;
import emsi.classes.Projet;
import emsi.classes.Tache;
import emsi.dao.IDao;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class ProjetService implements IDao<Projet> {

    private final SessionFactory sessionFactory;

    public ProjetService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean create(Projet o) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(o);
        return true;
    }

    @Override
    public boolean delete(Projet o) {
        Session session = sessionFactory.getCurrentSession();
        Projet attached = session.contains(o) ? o : session.merge(o);
        session.remove(attached);
        return true;
    }

    @Override
    public boolean update(Projet o) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(o);
        return true;
    }

    @Override
    public Projet findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.find(Projet.class, id);
    }

    @Override
    public List<Projet> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from Projet", Projet.class).getResultList();
    }

    public List<Tache> findByProjet(int idProjet) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Tache t where t.projet.id = :idProjet", Tache.class)
                .setParameter("idProjet", idProjet)
                .getResultList();
    }

    public List<EmployeTache> findByRealDates(int idProjet) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select et from EmployeTache et join fetch et.tache t where t.projet.id = :idProjet", EmployeTache.class)
                .setParameter("idProjet", idProjet)
                .getResultList();
    }
}