package emsi.classes;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "produits")
@NamedQuery(name = "Produit.findByPrixGreaterThan", query = "SELECT p FROM Produit p where p.prix > 100")
public class Produit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String reference;
    private float prix;

    @ManyToOne(fetch = FetchType.LAZY)
    private Categorie categorie;
    @OneToMany(mappedBy = "produit")
    private Set<LigneCommandeProduit> lignes = new HashSet<>();

    public Produit(float prix, String reference, Categorie categorie) {
        this.prix = prix;
        this.reference = reference;
        this.categorie = categorie;
    }

    public Produit() {
    }

    public int getId() {
        return id;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Set<LigneCommandeProduit> getLignes() {
        return lignes;
    }
}