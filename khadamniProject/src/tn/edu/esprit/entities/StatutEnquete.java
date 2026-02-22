/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tn.edu.esprit.entities;

/**
 *
 * @author LENOVO
 */
public enum StatutEnquete {
    
   BROUILLON,
   ACTIVE,
   CLOTUREE;
}

/*
-- =============================================
-- Script complet corrigé - Base khademni
-- =============================================

CREATE DATABASE IF NOT EXISTS khademni
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE khademni;

-- Table Enquete (avec VARCHAR pour le statut)
CREATE TABLE IF NOT EXISTS enquete (
    id_enquete INT AUTO_INCREMENT PRIMARY KEY,
    sujet VARCHAR(255) NOT NULL,
    description TEXT,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_fin DATETIME,
    date_modification DATETIME ON UPDATE CURRENT_TIMESTAMP,
    statut VARCHAR(20) DEFAULT 'BROUILLON',  -- VARCHAR pour correspondre à l'enum Java
    anonyme BOOLEAN DEFAULT TRUE,
    
    INDEX idx_statut (statut),
    INDEX idx_date_creation (date_creation)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table ReponseEnquete
CREATE TABLE IF NOT EXISTS reponse_enquete (
    id_reponse_enquete INT AUTO_INCREMENT PRIMARY KEY,
    reponse TEXT NOT NULL,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    enquete_id INT NOT NULL,
    
    FOREIGN KEY (enquete_id) REFERENCES enquete(id_enquete) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    
    INDEX idx_enquete_id (enquete_id),
    INDEX idx_date_creation (date_creation)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table Reclamation
CREATE TABLE IF NOT EXISTS reclamation (
    id_reclamation INT AUTO_INCREMENT PRIMARY KEY,
    sujet VARCHAR(255) NOT NULL,
    description TEXT,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_modification DATETIME ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_date_creation (date_creation),
    FULLTEXT INDEX idx_sujet_recherche (sujet)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
*/


