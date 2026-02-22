package tn.edu.esprit.entities;

public class AttributEnquete {
	
    private int idAttributEnquete;
    private String nom; // Texte de la question

    public AttributEnquete() {}
    public AttributEnquete(String nom) { this.nom = nom; }

    public int getIdAttributEnquete() { return idAttributEnquete; }
    public void setIdAttributEnquete(int id) { this.idAttributEnquete = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}