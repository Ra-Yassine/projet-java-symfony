package tn.edu.esprit.services;

import tn.edu.esprit.entities.ReponseEnquete;
import tn.edu.esprit.tools.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServiceReponseEnquete {

    private Connection cnx = DataSource.getInstance().getConnection();

    public void ajouter(ReponseEnquete r) {
        try {
            // 1️⃣ Calculer la moyenne des notes
            double moyenne = 0;
            if (!r.getNotes().isEmpty()) {
                int somme = 0;
                for (int note : r.getNotes().values()) {
                    somme += note;
                }
                moyenne = (double) somme / r.getNotes().size();
            }

            // 2️⃣ Insérer la réponse avec la moyenne
            String reqRep = "INSERT INTO reponse_enquete(id_enquete, moyenne) VALUES(?, ?)";
            try (PreparedStatement pstRep = cnx.prepareStatement(reqRep)) {
                pstRep.setInt(1, r.getEnquete().getIdEnquete());
                pstRep.setDouble(2, moyenne);
                pstRep.executeUpdate();
            }

            System.out.println("✅ Réponse ajoutée avec moyenne = " + moyenne);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}