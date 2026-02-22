
package tn.edu.esprit.services;

import tn.edu.esprit.entities.AttributEnquete;
import tn.edu.esprit.tools.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceAttributEnquete {

    Connection cnx = DataSource.getInstance().getConnection();

    public void ajouter(AttributEnquete a, int idEnquete) {
        String req = "INSERT INTO attribut_enquete(id_enquete, nom) VALUES (?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {

            pst.setInt(1, idEnquete);
            pst.setString(2, a.getNom());

            pst.executeUpdate();
            System.out.println("✅ Attribut ajouté !");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<AttributEnquete> getByEnquete(int idEnquete) {
        List<AttributEnquete> list = new ArrayList<>();
        String req = "SELECT * FROM attribut_enquete WHERE id_enquete=?";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {

            pst.setInt(1, idEnquete);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                AttributEnquete a = new AttributEnquete();
                a.setIdAttributEnquete(rs.getInt("idAttributEnquete"));
                a.setNom(rs.getString("nom"));

                list.add(a);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }

    public void supprimer(int id) {
        String req = "DELETE FROM attribut_enquete WHERE id=?";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {

            pst.setInt(1, id);
            pst.executeUpdate();

            System.out.println("✅ Attribut supprimé !");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}