package tn.edu.esprit.entities;

import java.time.LocalDateTime;
import java.util.List;

public class Enquete {
    private int idEnquete;
    private String sujet;
    private String description;
    private StatutEnquete statut;
    private boolean anonyme;
    private List<AttributEnquete> attributs;
    private LocalDateTime dateCreation;

    public Enquete() {
        this.dateCreation = LocalDateTime.now();
        this.statut = StatutEnquete.ACTIVE;
    }

    public Enquete(String sujet, String description, boolean anonyme) {
        this.sujet = sujet;
        this.description = description;
        this.anonyme = anonyme;
        this.dateCreation = LocalDateTime.now();
        this.statut = StatutEnquete.ACTIVE;
    }

    // Getters et setters
    public int getIdEnquete() { return idEnquete; }
    public void setIdEnquete(int idEnquete) { this.idEnquete = idEnquete; }
    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public StatutEnquete getStatut() { return statut; }
    public void setStatut(StatutEnquete statut) { this.statut = statut; }
    public boolean isAnonyme() { return anonyme; }
    public void setAnonyme(boolean anonyme) { this.anonyme = anonyme; }
    public List<AttributEnquete> getAttributs() { return attributs; }
    public void setAttributs(List<AttributEnquete> attributs) { this.attributs = attributs; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    @Override
    public String toString() { return sujet; }
}