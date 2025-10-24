package ma.projet.service;

import jakarta.persistence.criteria.*;
import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class HommeService implements IDao<Homme> {

    private final SessionFactory sessionFactory;

    public HommeService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean create(Homme o) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(o);
        return true;
    }

    @Override
    public boolean delete(Homme o) {
        Session session = sessionFactory.getCurrentSession();
        Homme attached = session.contains(o) ? o : session.merge(o);
        session.remove(attached);
        return true;
    }

    @Override
    public boolean update(Homme o) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(o);
        return true;
    }

    @Override
    public Homme findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.find(Homme.class, id);
    }

    @Override
    public List<Homme> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Homme", Homme.class)
                .getResultList();
    }

    public List<Femme> findByHommeAndDates(int id, LocalDate dateDebut, LocalDate dateFin) {
        return sessionFactory.getCurrentSession()
                .createQuery("select distinct m.femme from Mariage m where m.homme.id =:id and m.dateDebut >=:dateDebut and m.dateFin <=:dateFin", Femme.class)
                .setParameter("id", id)
                .setParameter("dateDebut", dateDebut)
                .setParameter("dateFin", dateFin)
                .getResultList();

    }


    // language: java
    public long countHommesAvecQuatreFemmesEntreDates(LocalDate dateDebut, LocalDate dateFin) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Homme> cq = cb.createQuery(Homme.class);
        Root<Mariage> mariageRoot = cq.from(Mariage.class);

        Join<Mariage, Homme> hommeJoin = mariageRoot.join("homme");
        Join<Mariage, Femme> femmeJoin = mariageRoot.join("femme");

        Predicate predDateDebut = cb.greaterThanOrEqualTo(mariageRoot.get("dateDebut"), dateDebut);
        Predicate predDateFin = cb.lessThanOrEqualTo(mariageRoot.get("dateFin"), dateFin);

        cq.select(hommeJoin)
                .where(cb.and(predDateDebut, predDateFin))
                .groupBy(hommeJoin.get("id"))
                .having(cb.equal(cb.countDistinct(femmeJoin.get("id")), 4L));

        List<Homme> resultList = session.createQuery(cq).getResultList();
        long count = resultList.size();
        System.out.println("Nombre d'hommes mariés à 4 femmes entre " + dateDebut + " et " + dateFin + " : " + count);
        return count;
    }

    public void afficherMariagesParHomme(int hommeId) {
        Session session = sessionFactory.getCurrentSession();
        Homme homme = session.find(Homme.class, hommeId);
        if (homme == null) {
            System.out.println("Homme introuvable pour l'id : " + hommeId);
            return;
        }

        List<Mariage> mariages = session.createQuery(
                        "from Mariage m where m.homme.id = :id order by m.dateDebut", Mariage.class)
                .setParameter("id", hommeId)
                .getResultList();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate today = LocalDate.now();

        List<Mariage> enCours = new ArrayList<>();
        List<Mariage> echoues = new ArrayList<>();

        for (Mariage m : mariages) {
            LocalDate dateFin = m.getDateFin();
            if (dateFin == null || !dateFin.isBefore(today)) {
                enCours.add(m);
            } else {
                echoues.add(m);
            }
        }

        System.out.println("Nom : " + homme.getNom() + " " + homme.getPrenom());

        System.out.println("Mariages En Cours :");
        int i = 1;
        for (Mariage m : enCours) {
            String debut = m.getDateDebut() != null ? m.getDateDebut().format(fmt) : "";
            String nbr = String.valueOf(m.getNbrEnfant());
            System.out.printf("%d. Femme : %s %s   Date Début : %s    Nbr Enfants : %s%n",
                    i++,
                    m.getFemme().getNom(),
                    m.getFemme().getPrenom(),
                    debut,
                    nbr);
        }
        if (enCours.isEmpty()) {
            System.out.println("Aucun mariage en cours.");
        }

        System.out.println();
        System.out.println("Mariages échoués :");
        i = 1;
        for (Mariage m : echoues) {
            String debut = m.getDateDebut() != null ? m.getDateDebut().format(fmt) : "";
            String fin = m.getDateFin() != null ? m.getDateFin().format(fmt) : "";
            String nbr = String.valueOf(m.getNbrEnfant());
            System.out.printf("%d. Femme : %s %s  Date Début : %s%nDate Fin : %s    Nbr Enfants : %s%n%n",
                    i++,
                    m.getFemme().getNom(),
                    m.getFemme().getPrenom(),
                    debut,
                    fin,
                    nbr);
        }
        if (echoues.isEmpty()) {
            System.out.println("Aucun mariage échoué.");
        }
    }
}

