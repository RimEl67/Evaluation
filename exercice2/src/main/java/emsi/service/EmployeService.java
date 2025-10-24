package emsi.service;

import emsi.classes.Employe;
import emsi.classes.Projet;
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
public class EmployeService implements IDao<Employe> {

    private final SessionFactory sessionFactory;

    public EmployeService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean create(Employe o) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(o);
        return true;
    }

    @Override
    public boolean delete(Employe o) {
        Session session = sessionFactory.getCurrentSession();
        Employe attached = session.contains(o) ? o : session.merge(o);
        session.remove(attached);
        return true;
    }

    @Override
    public boolean update(Employe o) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(o);
        return true;
    }

    @Override
    public Employe findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.find(Employe.class, id);
    }

    @Override
    public List<Employe> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from Employe", Employe.class).getResultList();
    }

    public List<Tache> findByEmployeAccomplishement(int idEmploye) {
        Session session = sessionFactory.getCurrentSession();
        LocalDate today = LocalDate.now();
        return session.createQuery("select distinct emT.tache from EmployeTache emT join emT.employe em where em.id = :idEmploye and emT.tache.dateFin >= :now", Tache.class)
                .setParameter("idEmploye", idEmploye)
                .setParameter("now", today)
                .getResultList();
    }

    public List<Projet> findByManagedEmploye(int idEmploye) {
        Session session = sessionFactory.getCurrentSession();
        LocalDate today = LocalDate.now();
        return session.createQuery("from Projet p where p.chefProjet.id = :idEmploye", Projet.class)
                .setParameter("idEmploye", idEmploye)
                .getResultList();
    }
}