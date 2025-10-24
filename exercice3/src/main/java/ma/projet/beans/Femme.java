package ma.projet.beans;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedQuery;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("F")
@NamedNativeQuery(name = "nombreEnfantParFemmeBetweenDates", query = "select coalesce(sum(nbrEnfant), 0) as total from mariage where femme_id = :id and dateDebut >= :dateDebut and dateFin <= :dateFin")
@NamedQuery(name = "femmesAvecAuMoinsDeuxMariages", query = "select f from Femme f where (select count(m) from Mariage m where m.femme = f) >= 2")
public class Femme extends Personne {

    public Femme() {
        super();
    }

    public Femme(String nom, String prenom, String telephone, String adresse, LocalDate dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }
}