package emsi.classes;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LigneCommandeProduit> lignes = new HashSet<>();

    public Commande(Date date) {
        this.date = date;
    }

    public Commande() {
    }

    public int getId() {
        return id;
    }

    public Set<LigneCommandeProduit> getLignes() {
        return lignes;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void addLigne(LigneCommandeProduit ligne) {
        ligne.setCommande(this);
        this.lignes.add(ligne);
    }

    public void removeLigne(LigneCommandeProduit ligne) {
        ligne.setCommande(null);
        this.lignes.remove(ligne);
    }
}