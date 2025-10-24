package emsi.service;

import emsi.classes.Employe;
import emsi.classes.EmployeTache;
import emsi.classes.Tache;
import emsi.dao.IDao;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class EmployeTacheService implements IDao<EmployeTache> {

    private final SessionFactory sessionFactory;

    public EmployeTacheService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean create(EmployeTache o) {
        Session session = sessionFactory.getCurrentSession();
        if (o.getEmploye() == null) {
            Employe emp = session.find(Employe.class, o.getEmploye().getId());
            o.setEmploye(emp);
        }
        if (o.getTache() == null) {
            Tache tache = session.find(Tache.class, o.getTache().getId());
            o.setTache(tache);
        }
        session.persist(o);
        return true;
    }

    @Override
    public boolean delete(EmployeTache o) {
        Session session = sessionFactory.getCurrentSession();
        EmployeTache tached = session.contains(o) ? o : session.merge(o);
        session.remove(tached);
        return true;
    }

    @Override
    public boolean update(EmployeTache o) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(o);
        return true;
    }

    @Override
    public EmployeTache findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.find(EmployeTache.class, id);
    }

    @Override
    public List<EmployeTache> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from EmployeTache", EmployeTache.class).getResultList();
    }
}