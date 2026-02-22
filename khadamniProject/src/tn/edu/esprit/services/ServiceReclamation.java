package tn.edu.esprit.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import tn.edu.esprit.entities.Reclamation;
import tn.edu.esprit.tools.DataSource;

public class ServiceReclamation implements IService<Reclamation> {
    Connection cnx;

    public ServiceReclamation() {
        this.cnx = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouter(Reclamation t) {
        try {
            // Validation : sujet obligatoire
            if (t.getSujet() == null || t.getSujet().trim().isEmpty()) {
                throw new IllegalArgumentException("Le sujet est obligatoire !");
            }

            String req = "INSERT INTO reclamation(sujet, description, date_creation) VALUES(?, ?, ?)";
            PreparedStatement pst = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, t.getSujet());
            pst.setString(2, t.getDescription());
            pst.setTimestamp(3, Timestamp.valueOf(t.getDateCreation()));

            int affectedRows = pst.executeUpdate();
            System.out.println("Lignes affectées: " + affectedRows);

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                t.setIdReclamation(generatedId);
                System.out.println("ID généré: " + generatedId);
            }

            System.out.println("Réclamation ajoutée avec succès !");
        } catch (IllegalArgumentException ex) {
            System.out.println("Erreur de validation: " + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
public void modifier(Reclamation t) {
    try {
        if (t.getIdReclamation() <= 0) {
            throw new RuntimeException(" Tentative UPDATE sans ID !");
        }

        String req = "UPDATE reclamation SET sujet=?, description=? WHERE id_reclamation=?";
        PreparedStatement pst = cnx.prepareStatement(req);

        System.out.println("===== UPDATE DEBUG =====");
        System.out.println("ID = " + t.getIdReclamation());

        pst.setString(1, t.getSujet());
        pst.setString(2, t.getDescription());
        pst.setInt(3, t.getIdReclamation());

        int rows = pst.executeUpdate();
        System.out.println("Rows affected = " + rows);

    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

    @Override
    public void supprimer(int id) {
        try {
            String req = "DELETE FROM reclamation WHERE id_reclamation = ?";
            PreparedStatement pst = cnx.prepareStatement(req);

            pst.setInt(1, id);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println(" Réclamation supprimée avec succès ! (ID: " + id + ")");
            } else {
                System.out.println("Aucune réclamation trouvée avec cet ID : " + id);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la suppression: " + ex.getMessage());
        }
    }

    @Override
    public Reclamation getOne(Reclamation t) {
        try {
            String req = "SELECT id_reclamation, sujet, description, dateCreation, dateModification FROM reclamation WHERE id_reclamation = ?";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, t.getIdReclamation());
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Reclamation rec = new Reclamation();
                rec.setIdReclamation(rs.getInt("id_reclamation"));
                rec.setSujet(rs.getString("sujet"));
                rec.setDescription(rs.getString("description"));
                
                // Récupérer et convertir les dates
                Timestamp creationTimestamp = rs.getTimestamp("dateCreation");
                if (creationTimestamp != null) {
                    rec.setDateCreation(creationTimestamp.toLocalDateTime());
                }
                
                Timestamp modificationTimestamp = rs.getTimestamp("dateModification");
                if (modificationTimestamp != null) {
                    rec.setDateModification(modificationTimestamp.toLocalDateTime());
                }
                
                return rec;
            } else {
                System.out.println(" Aucune réclamation trouvée avec l'ID: " + t.getIdReclamation());
            }
        } catch (SQLException ex) {
            System.out.println(" Erreur lors de la recherche: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    public List<Reclamation> getAll() {
        String req = "SELECT id_reclamation, sujet, description, dateCreation, dateModification FROM reclamation ORDER BY dateCreation DESC";
        List<Reclamation> reclamations = new ArrayList<>();

        try (Statement stm = this.cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            while (rs.next()) {
                Reclamation rec = new Reclamation();
                // Utiliser le bon nom de colonne : id_reclamation (pas idReclamation)
                rec.setIdReclamation(rs.getInt("id_reclamation"));
                rec.setSujet(rs.getString("sujet"));
                rec.setDescription(rs.getString("description"));
                
                // Récupérer et convertir les dates
                Timestamp creationTimestamp = rs.getTimestamp("dateCreation");
                if (creationTimestamp != null) {
                    rec.setDateCreation(creationTimestamp.toLocalDateTime());
                }
                
                Timestamp modificationTimestamp = rs.getTimestamp("dateModification");
                if (modificationTimestamp != null) {
                    rec.setDateModification(modificationTimestamp.toLocalDateTime());
                }

                reclamations.add(rec);
            }
            
            System.out.println(reclamations.size() + " réclamation(s) trouvée(s)");
            
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération: " + ex.getMessage());
            ex.printStackTrace();
        }

        return reclamations;
    }
    
    // Méthode supplémentaire pour rechercher par sujet
    public List<Reclamation> rechercherParSujet(String motCle) {
        String req = "SELECT id_reclamation, sujet, description, dateCreation, dateModification FROM reclamation WHERE sujet LIKE ? ORDER BY dateCreation DESC";
        List<Reclamation> reclamations = new ArrayList<>();
        
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, "%" + motCle + "%");
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Reclamation rec = new Reclamation();
                rec.setIdReclamation(rs.getInt("id_reclamation"));
                rec.setSujet(rs.getString("sujet"));
                rec.setDescription(rs.getString("description"));
                
                Timestamp creationTimestamp = rs.getTimestamp("dateCreation");
                if (creationTimestamp != null) {
                    rec.setDateCreation(creationTimestamp.toLocalDateTime());
                }
                
                Timestamp modificationTimestamp = rs.getTimestamp("dateModification");
                if (modificationTimestamp != null) {
                    rec.setDateModification(modificationTimestamp.toLocalDateTime());
                }
                
                reclamations.add(rec);
            }
            
            System.out.println( reclamations.size() + " résultat(s) pour: " + motCle);
            
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la recherche: " + ex.getMessage());
        }
        
        return reclamations;
    }

	
}