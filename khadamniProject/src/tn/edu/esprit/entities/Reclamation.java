

package tn.edu.esprit.entities;
import java.time.LocalDateTime;
public class Reclamation {

   
    private int idReclamation;
    private String sujet ;
    private String description ;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    //constructeur pour initialisation (form javaFx par exemple)
    public Reclamation(){
    	this.dateCreation = LocalDateTime.now();
        
    }
    
    //constructeur pour creation des objets 
    public Reclamation(String sujet, String description) {
        this.sujet = sujet;
        this.description = description;
        this.dateCreation = LocalDateTime.now();
    }
    
    //constructeur pour update
    public Reclamation(int idReclamation, String sujet, String description) {
    this.idReclamation = idReclamation;
    this.sujet = sujet;
    this.description = description;
    }

    public Reclamation(int idReclamation, String sujet, String description,LocalDateTime dateCreation) {
        this.idReclamation = idReclamation;
        this.sujet = sujet;
        this.description = description;
        this.dateCreation =dateCreation;
    }

    public int getIdReclamation() {
        return idReclamation;
    }

    public void setIdReclamation(int idReclamation) {
        this.idReclamation= idReclamation;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String Description) {
        this.description = Description;
    }
    

    public LocalDateTime getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(LocalDateTime dateCreation) {
		this.dateCreation = dateCreation;
	}
	

	public LocalDateTime getDateModification() {
		return dateModification;
	}

	public void setDateModification(LocalDateTime dateModification) {
		this.dateModification = dateModification;
	}

	@Override
    public String toString() {
        return "Reclamation{" +
               "sujet=" + sujet +
               ", description=" + description +
               ", date de Creation=" + dateCreation +
               '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.idReclamation;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Reclamation other = (Reclamation) obj;
        if (this.idReclamation != other.idReclamation) {
            return false;
        }
        return true;
    }
    
}