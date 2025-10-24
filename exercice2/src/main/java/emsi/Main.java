package emsi;

import emsi.classes.Employe;
import emsi.classes.EmployeTache;
import emsi.classes.Projet;
import emsi.classes.Tache;
import emsi.dao.IDao;
import emsi.service.ProjetService;
import emsi.service.EmployeService;
import emsi.util.HibernateUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(HibernateUtil.class);

        // DAOs
        IDao<Employe> employeIDao = context.getBean("employeService", IDao.class);
        IDao<EmployeTache> employeTacheIDao = context.getBean("employeTacheService", IDao.class);
        IDao<Projet> projetIDao = context.getBean("projetService", IDao.class);
        IDao<Tache> tacheIDao = context.getBean("tacheService", IDao.class);

        ProjetService projetService = context.getBean("projetService", ProjetService.class);
        EmployeService employeService = context.getBean("employeService", EmployeService.class);

        // 1️⃣ Create Employees
        Employe e1 = new Employe("Rim", "EL ABBASSI", "0607077763");
        Employe e2 = new Employe("Rimi", "EL", "0607077763");
        Employe e3 = new Employe("Lina", "Aily", "0607077763");
        Employe e4 = new Employe("Doha", "Amm", "0607077763");

        employeIDao.create(e1);
        employeIDao.create(e2);
        employeIDao.create(e3);
        employeIDao.create(e4);

        // 2️⃣ Create Project with fixed ID & date
        Projet projet = new Projet();
        projet.setId(4); // fixed ID
        projet.setNom("Gestion de stock");
        projet.setDateDebut(LocalDate.of(2013, 1, 14)); // 14 Janvier 2013
        projet.setDateFin(LocalDate.of(2013, 5, 30));
        projet.setChefProjet(e1);
        projetIDao.create(projet);

        // 3️⃣ Create Tasks
        Tache t1 = new Tache();
        t1.setId(12);
        t1.setNom("Analyse");
        t1.setDateDebut(LocalDate.of(2013, 2, 10));
        t1.setDateFin(LocalDate.of(2013, 2, 20));
        t1.setProjet(projet);

        Tache t2 = new Tache();
        t2.setId(13);
        t2.setNom("Conception");
        t2.setDateDebut(LocalDate.of(2013, 3, 10));
        t2.setDateFin(LocalDate.of(2013, 3, 15));
        t2.setProjet(projet);

        Tache t3 = new Tache();
        t3.setId(14);
        t3.setNom("Développement");
        t3.setDateDebut(LocalDate.of(2013, 4, 10));
        t3.setDateFin(LocalDate.of(2013, 4, 25));
        t3.setProjet(projet);

        tacheIDao.create(t1);
        tacheIDao.create(t2);
        tacheIDao.create(t3);

        // 4️⃣ Assign Employees to Tasks
        EmployeTache et1 = new EmployeTache();
        et1.setEmploye(e2);
        et1.setTache(t1);
        et1.setDateDebutReelle(t1.getDateDebut());
        et1.setDateFinReelle(t1.getDateFin());
        employeTacheIDao.create(et1);

        EmployeTache et2 = new EmployeTache();
        et2.setEmploye(e3);
        et2.setTache(t2);
        et2.setDateDebutReelle(t2.getDateDebut());
        et2.setDateFinReelle(t2.getDateFin());
        employeTacheIDao.create(et2);

        EmployeTache et3 = new EmployeTache();
        et3.setEmploye(e4);
        et3.setTache(t3);
        et3.setDateDebutReelle(t3.getDateDebut());
        et3.setDateFinReelle(t3.getDateFin());
        employeTacheIDao.create(et3);

        // 5️⃣ Print Project & Tasks
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

        System.out.println("Projet : " + projet.getId() + "      Nom : " + projet.getNom() + "     Date début : " + projet.getDateDebut().format(formatter));
        System.out.println("Liste des tâches:");
        System.out.println("Num  Nom            Date Début Réelle   Date Fin Réelle");

        List<EmployeTache> employeTaches = projetService.findByRealDates(projet.getId());
        for (EmployeTache et : employeTaches) {
            System.out.printf("%-4d %-15s %-18s %-18s%n",
                    et.getTache().getId(),
                    et.getTache().getNom(),
                    et.getDateDebutReelle().format(formatter),
                    et.getDateFinReelle().format(formatter));
        }
    }
}
