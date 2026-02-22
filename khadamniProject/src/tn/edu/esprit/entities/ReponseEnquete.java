package tn.edu.esprit.entities;

import java.util.HashMap;
import java.util.Map;

public class ReponseEnquete {
    private Enquete enquete;
    private Map<Integer, Integer> notes = new HashMap<>(); // clé = idAttribut, valeur = note 1-5

    public ReponseEnquete(Enquete enquete) { this.enquete = enquete; }

    public void ajouterNote(int idAttribut, int note) { notes.put(idAttribut, note); }
    public int getNote(int idAttribut) { return notes.getOrDefault(idAttribut, 0); }
    public Enquete getEnquete() { return enquete; }
    public Map<Integer, Integer> getNotes() { return notes; }
}