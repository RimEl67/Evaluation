package ma.projet.service;

import jakarta.transaction.Transactional;
import ma.projet.beans.Femme;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class FemmeService implements IDao<Femme> {

    private final SessionFactory sessionFactory;

    public FemmeService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean create(Femme o) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(o);
        return true;
    }

    @Override
    public boolean delete(Femme o) {
        Session session = sessionFactory.getCurrentSession();
        Femme attached = session.contains(o) ? o : session.merge(o);
        session.remove(attached);
        return true;
    }

    @Override
    public boolean update(Femme o) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(o);
        return true;
    }

    @Override
    public Femme findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.find(Femme.class, id);
    }

    @Override
    public List<Femme> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Femme", Femme.class)
                .getResultList();
    }

    public long nombreEnfantParFemmeBetweenDates(int id, LocalDate dateDebut, LocalDate dateFin) {
        Object result = sessionFactory.getCurrentSession()
                .getNamedNativeQuery("nombreEnfantParFemmeBetweenDates")
                .setParameter("id", id)
                .setParameter("dateDebut", dateDebut)
                .setParameter("dateFin", dateFin)
                .getSingleResult();
        if (result == null) return 0L;
        if (result instanceof Number) return ((Number) result).longValue();
        try {
            return Long.parseLong(result.toString());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public List<Femme> findFemmesAvecAuMoinsDeuxMariages() {
        return sessionFactory.getCurrentSession()
                .createNamedQuery("femmesAvecAuMoinsDeuxMariages", Femme.class)
                .getResultList();
    }
}