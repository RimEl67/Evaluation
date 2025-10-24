package ma.projet.service;

import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class MariageService implements IDao<Mariage> {
    private final SessionFactory sessionFactory;

    public MariageService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean create(Mariage o) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(o);
        return true;
    }

    @Override
    public boolean delete(Mariage o) {
        Session session = sessionFactory.getCurrentSession();
        Mariage attached = session.contains(o) ? o : session.merge(o);
        session.remove(attached);
        return true;
    }

    @Override
    public boolean update(Mariage o) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(o);
        return true;
    }

    @Override
    public Mariage findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.find(Mariage.class, id);
    }

    @Override
    public List<Mariage> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Mariage", Mariage.class)
                .getResultList();
    }
}