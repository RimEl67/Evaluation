package ma.projet;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.util.HibernateUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(HibernateUtil.class);

        IDao<Femme> femmeIDao = context.getBean("femmeService", IDao.class);
        IDao<Mariage> mariageIDao = context.getBean("mariageService", IDao.class);
        IDao<Homme> hommeIDao = context.getBean("hommeService", IDao.class);
        HommeService hommeService = context.getBean("hommeService", HommeService.class);
        FemmeService femmeService = context.getBean("femmeService", FemmeService.class);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // 1) Créer 10 femmes
        List<Femme> femmes = new ArrayList<>();
        femmes.add(new Femme("MORADI", "Leila", "0600000001", "Adresse1", LocalDate.of(1980, 1, 10)));
        femmes.add(new Femme("HAMDI", "Nora", "0600000002", "Adresse2", LocalDate.of(1975, 5, 20)));
        femmes.add(new Femme("FARHI", "Sana", "0600000003", "Adresse3", LocalDate.of(1990, 3, 15)));
        femmes.add(new Femme("JABRI", "Lina", "0600000004", "Adresse4", LocalDate.of(1985, 7, 30)));
        femmes.add(new Femme("SAFI", "Rania", "0600000005", "Adresse5", LocalDate.of(1978, 9, 5)));
        femmes.add(new Femme("ELI", "Imane", "0600000006", "Adresse6", LocalDate.of(1988, 11, 12)));
        femmes.add(new Femme("BAOUI", "Meryem", "0600000007", "Adresse7", LocalDate.of(1992, 4, 2)));
        femmes.add(new Femme("ZAKI", "Karima", "0600000008", "Adresse8", LocalDate.of(1982, 2, 28)));
        femmes.add(new Femme("OUFIR", "Fatima", "0600000009", "Adresse9", LocalDate.of(1995, 6, 7)));
        femmes.add(new Femme("REHMANI", "Ikram", "0600000010", "Adresse10", LocalDate.of(1998, 12, 1)));

        for (Femme f : femmes) {
            femmeIDao.create(f);
        }

        // 2) Créer 5 hommes
        List<Homme> hommes = new ArrayList<>();
        hommes.add(new Homme("BENALI", "Rami", "0637269069", "Marrakech", LocalDate.of(1970, 4, 16)));
        hommes.add(new Homme("OUARDA", "Sofiane", "0607077763", "Casablanca", LocalDate.of(1975, 10, 6)));
        hommes.add(new Homme("FAHMI", "Yassine", "0675425516", "Paris", LocalDate.of(1980, 2, 17)));
        hommes.add(new Homme("GHARBI", "Omar", "0612345678", "Rabat", LocalDate.of(1968, 8, 8)));
        hommes.add(new Homme("RAHMANI", "Adel", "0698765432", "Tanger", LocalDate.of(1972, 3, 3)));

        for (Homme h : hommes) {
            hommeIDao.create(h);
        }

        // 3) Créer des mariages (homme index 4 marié à 4 femmes)
        LocalDate d1 = LocalDate.of(2017, 4, 12);
        LocalDate d2 = LocalDate.of(2024, 4, 12);
        mariageIDao.create(new Mariage(LocalDate.of(2018, 1, 10), null, 2, hommes.get(4), femmes.get(0)));
        mariageIDao.create(new Mariage(LocalDate.of(2019, 2, 5), null, 1, hommes.get(4), femmes.get(1)));
        mariageIDao.create(new Mariage(LocalDate.of(2020, 3, 20), null, 0, hommes.get(4), femmes.get(2)));
        mariageIDao.create(new Mariage(LocalDate.of(2021, 6, 30), null, 3, hommes.get(4), femmes.get(3)));

        // Femmes mariées deux fois ou plus
        mariageIDao.create(new Mariage(LocalDate.of(2010, 5, 1), LocalDate.of(2013, 5, 1), 0, hommes.get(0), femmes.get(4)));
        mariageIDao.create(new Mariage(LocalDate.of(2014, 6, 1), null, 2, hommes.get(1), femmes.get(4)));

        mariageIDao.create(new Mariage(LocalDate.of(2009, 9, 3), LocalDate.of(2010, 9, 3), 0, hommes.get(2), femmes.get(7)));
        mariageIDao.create(new Mariage(LocalDate.of(2011, 9, 3), LocalDate.of(2012, 9, 3), 0, hommes.get(3), femmes.get(7)));

        // Quelques mariages supplémentaires
        mariageIDao.create(new Mariage(LocalDate.of(2018, 4, 12), LocalDate.of(2020, 4, 12), 1, hommes.get(0), femmes.get(5)));
        mariageIDao.create(new Mariage(LocalDate.of(2022, 1, 1), null, 0, hommes.get(1), femmes.get(6)));
        mariageIDao.create(new Mariage(LocalDate.of(2017, 4, 12), LocalDate.of(2019, 4, 12), 0, hommes.get(2), femmes.get(8)));
        mariageIDao.create(new Mariage(LocalDate.of(2017, 4, 12), LocalDate.of(2018, 4, 12), 0, hommes.get(3), femmes.get(9)));

        // --- Affichages ---
        System.out.println("Liste des femmes :");
        List<Femme> toutesFemmes = femmeIDao.findAll();
        toutesFemmes.forEach(f -> System.out.println("- " + f.getNom() + " " + f.getPrenom() + " (né le " + f.getDateNaissance().format(fmt) + ")"));
        System.out.println();

        toutesFemmes.stream()
                .min(Comparator.comparing(Femme::getDateNaissance))
                .ifPresent(f -> System.out.println("Femme la plus âgée : " + f.getNom() + " " + f.getPrenom() + " (né le " + f.getDateNaissance().format(fmt) + ")\n"));

        // Afficher les épouses de l'homme "RAHMANI Adel"
        List<Homme> hommesEnBase = hommeIDao.findAll();
        Homme adel = hommesEnBase.stream()
                .filter(h -> "RAHMANI".equals(h.getNom()) && "Adel".equals(h.getPrenom()))
                .findFirst()
                .orElse(null);

        if (adel == null) {
            System.out.println("Homme RAHMANI Adel introuvable en base.");
        } else {
            System.out.println("Épouses de l'homme " + adel.getNom() + " " + adel.getPrenom() + " :");
            List<Femme> epouses = hommeService.findByHommeAndDates(adel.getId(), LocalDate.of(2000, 1, 1), LocalDate.now());
            if (epouses.isEmpty()) {
                System.out.println("Aucune épouse trouvée.");
            } else {
                epouses.forEach(f -> System.out.println("- " + f.getNom() + " " + f.getPrenom()));
            }
        }
    }
}
