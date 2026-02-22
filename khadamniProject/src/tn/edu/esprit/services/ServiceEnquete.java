package tn.edu.esprit.services;

import tn.edu.esprit.entities.AttributEnquete;
import tn.edu.esprit.entities.Enquete;
import tn.edu.esprit.entities.StatutEnquete;
import tn.edu.esprit.tools.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEnquete {
    private Connection cnx = DataSource.getInstance().getConnection();

    // Ajouter une enquête
    public void ajouter(Enquete e) {
        String req = "INSERT INTO enquete(sujet, description, statut, anonyme, date_creation) VALUES(?,?,?,?,?)";

        try (PreparedStatement pst = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, e.getSujet());
            pst.setString(2, e.getDescription());
            pst.setString(3, e.getStatut().name());
            pst.setBoolean(4, e.isAnonyme());
            pst.setTimestamp(5, Timestamp.valueOf(e.getDateCreation()));

            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int idEnquete = rs.getInt(1);
                e.setIdEnquete(idEnquete);

                if (e.getAttributs() != null) {
                    for (AttributEnquete attr : e.getAttributs()) {
                        ajouterAttribut(attr, idEnquete);
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    // Modifier une enquête
    public void modifier(Enquete e) {
        String req = "UPDATE enquete SET sujet=?, description=?, statut=?, anonyme=? WHERE id_enquete=?";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, e.getSujet());
            pst.setString(2, e.getDescription());
            pst.setString(3, e.getStatut().name());
            pst.setBoolean(4, e.isAnonyme());
            pst.setInt(5, e.getIdEnquete());

            pst.executeUpdate();

            // Supprimer les anciens attributs
            supprimerAttributsByEnquete(e.getIdEnquete());

            // Ajouter les nouveaux attributs
            if (e.getAttributs() != null) {
                for (AttributEnquete attr : e.getAttributs()) {
                    ajouterAttribut(attr, e.getIdEnquete());
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Récupérer toutes les enquêtes
    public List<Enquete> getAll() {
        List<Enquete> enquetes = new ArrayList<>();
        String req = "SELECT * FROM enquete ORDER BY date_creation DESC";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Enquete e = new Enquete();
                e.setIdEnquete(rs.getInt("id_enquete"));
                e.setSujet(rs.getString("sujet"));
                e.setDescription(rs.getString("description"));
                e.setStatut(StatutEnquete.valueOf(rs.getString("statut")));
                e.setAnonyme(rs.getBoolean("anonyme"));
                e.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
                e.setAttributs(getAttributsByEnquete(e.getIdEnquete()));
                enquetes.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return enquetes;
    }

    // Récupérer une enquête par son ID
    public Enquete getOne(int id) {
        String req = "SELECT * FROM enquete WHERE id_enquete=?";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Enquete e = new Enquete();
                e.setIdEnquete(rs.getInt("id_enquete"));
                e.setSujet(rs.getString("sujet"));
                e.setDescription(rs.getString("description"));
                e.setStatut(StatutEnquete.valueOf(rs.getString("statut")));
                e.setAnonyme(rs.getBoolean("anonyme"));
                e.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
                e.setAttributs(getAttributsByEnquete(e.getIdEnquete()));
                return e;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Supprimer une enquête
    public void supprimer(int id) {
        // D'abord supprimer les attributs
        supprimerAttributsByEnquete(id);

        // Ensuite supprimer l'enquête
        String req = "DELETE FROM enquete WHERE id_enquete=?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Activer une enquête
    public void activer(int id) {
        String req = "UPDATE enquete SET statut='ACTIVE' WHERE id_enquete=?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException ex) { 
            ex.printStackTrace(); 
        }
    }

    // Désactiver une enquête
    public void desactiver(int id) {
        String req = "UPDATE enquete SET statut='INACTIVE' WHERE id_enquete=?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException ex) { 
            ex.printStackTrace(); 
        }
    }

    // Récupérer les enquêtes actives
    public List<Enquete> getEnquetesActives() {
        List<Enquete> enquetes = new ArrayList<>();
        String req = "SELECT * FROM enquete WHERE statut='ACTIVE' ORDER BY date_creation DESC";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while(rs.next()) {
                Enquete e = new Enquete();
                e.setIdEnquete(rs.getInt("id_enquete"));
                e.setSujet(rs.getString("sujet"));
                e.setDescription(rs.getString("description"));
                e.setStatut(StatutEnquete.valueOf(rs.getString("statut")));
                e.setAnonyme(rs.getBoolean("anonyme"));
                e.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
                e.setAttributs(getAttributsByEnquete(e.getIdEnquete()));
                enquetes.add(e);
            }
        } catch(SQLException ex) { 
            ex.printStackTrace(); 
        }
        return enquetes;
    }

    // ================= MÉTHODES POUR LES ATTRIBUTS =================
    
    private void ajouterAttribut(AttributEnquete attr, int idEnquete) {
        String req = "INSERT INTO attribut_enquete(nom, id_enquete) VALUES(?,?)";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, attr.getNom());
            pst.setInt(2, idEnquete);
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Récupérer les questions d'une enquête
    public List<AttributEnquete> getAttributsByEnquete(int idEnquete) {
        List<AttributEnquete> attributs = new ArrayList<>();
        String req = "SELECT * FROM attribut_enquete WHERE id_enquete=?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, idEnquete);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                AttributEnquete a = new AttributEnquete();
                a.setIdAttributEnquete(rs.getInt("id_attribut"));
                a.setNom(rs.getString("nom"));
                attributs.add(a);
            }
        } catch(SQLException ex) { 
            ex.printStackTrace(); 
        }
        return attributs;
    }

    // Supprimer les attributs d'une enquête
    private void supprimerAttributsByEnquete(int idEnquete) {
        String req = "DELETE FROM attribut_enquete WHERE id_enquete=?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, idEnquete);
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}