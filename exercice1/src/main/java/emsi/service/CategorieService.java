package emsi.service;

import emsi.classes.Categorie;
import emsi.dao.IDao;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Transactional
public class CategorieService implements IDao<Categorie> {

    private final SessionFactory sessionFactory;

    public CategorieService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean create(Categorie o) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(o);
        return true;
    }

    @Override
    public boolean delete(Categorie o) {
        Session session = sessionFactory.getCurrentSession();
        Categorie attached = session.contains(o) ? o : session.merge(o);
        session.remove(attached);
        return true;
    }

    @Override
    public boolean update(Categorie o) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(o);
        return true;
    }

    @Override
    public Categorie findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.find(Categorie.class, id);
    }

    @Override
    public List<Categorie> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Categorie", Categorie.class)
                .getResultList();
    }
}