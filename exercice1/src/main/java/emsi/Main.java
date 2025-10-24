package emsi;

import emsi.classes.Categorie;
import emsi.classes.Commande;
import emsi.classes.LigneCommandeProduit;
import emsi.classes.Produit;
import emsi.dao.IDao;
import emsi.service.CommandeService;
import emsi.service.ProduitService;
import emsi.util.HibernateUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {

        // Initialize Spring context
        ApplicationContext context = new AnnotationConfigApplicationContext(HibernateUtil.class);

        IDao<Produit> produitDao = context.getBean("produitService", IDao.class);
        ProduitService produitService = context.getBean("produitService", ProduitService.class);
        IDao<Categorie> categorieIDao = context.getBean("categorieService", IDao.class);
        IDao<Commande> commandeIDao = context.getBean("commandeService", IDao.class);
        IDao<LigneCommandeProduit> ligneCommandeProduitIDao = context.getBean("ligneCommandeService", IDao.class);

        // 1. Create Categories
        Categorie cat1 = new Categorie("Ph", "Phones");
        Categorie cat2 = new Categorie("Lap", "Laptops");
        Categorie cat3 = new Categorie("Imp", "Imprimantes");
        categorieIDao.create(cat1);
        categorieIDao.create(cat2);
        categorieIDao.create(cat3);

        // 2. Create Products
        Produit p1 = new Produit(120, "ES12", cat1);
        Produit p2 = new Produit(100, "ZR85", cat2);
        Produit p3 = new Produit(200, "EE85", cat3);
        produitDao.create(p1);
        produitDao.create(p2);
        produitDao.create(p3);

        // 3. Create Commande
        Commande commande = new Commande();
        commande.setDate(new java.util.Date()); // today’s date
        commandeIDao.create(commande);

        // 4. Create LigneCommandeProduit
        LigneCommandeProduit lc1 = new LigneCommandeProduit();
        lc1.setProduit(p1);
        lc1.setQuantite(7);
        lc1.setCommande(commande);
        ligneCommandeProduitIDao.create(lc1);

        LigneCommandeProduit lc2 = new LigneCommandeProduit();
        lc2.setProduit(p2);
        lc2.setQuantite(14);
        lc2.setCommande(commande);
        ligneCommandeProduitIDao.create(lc2);

        LigneCommandeProduit lc3 = new LigneCommandeProduit();
        lc3.setProduit(p3);
        lc3.setQuantite(5);
        lc3.setCommande(commande);
        ligneCommandeProduitIDao.create(lc3);

        // 5. Print Commande and Products
        Commande fetchedCommande = commandeIDao.findById(commande.getId());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("fr", "FR"));
        String formattedDate = sdf.format(fetchedCommande.getDate());

        System.out.println("Commande : " + fetchedCommande.getId() + " \t Date : " + formattedDate);
        System.out.println("Liste des produits :");
        System.out.println("Référence   Prix    Quantité");

        for (LigneCommandeProduit ligne : fetchedCommande.getLignes()) {
            Produit prod = ligne.getProduit();
            String ref = prod.getReference();
            float prix = prod.getPrix();
            int quantite = ligne.getQuantite();
            System.out.printf("%-10s %-7s %-8d%n", ref, prix + " DH", quantite);
        }
    }
}
