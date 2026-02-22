/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.esprit.services;

import java.util.List;

import tn.edu.esprit.entities.Enquete;
import tn.edu.esprit.entities.Reclamation;
import tn.edu.esprit.entities.ReponseEnquete;

/**
 *
 * @author abdelazizmezri
 */
public interface IService <T> {
    public void ajouter(T t);
    public void modifier(T t);
    public void supprimer(int id);
    public T getOne(T t);
    public List<T> getAll();

}
