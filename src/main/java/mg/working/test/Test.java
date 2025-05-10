package mg.working.test;

import mg.working.annotations.Column;
import mg.working.annotations.SearchWithCriteria;
import mg.working.annotations.Table;

@Table(name = "t_test")
public class Test {
    @Column(name = "anarana") String nom;
    @Column(name = "fanampiny") String prenom;

   @SearchWithCriteria
   @Column Integer age;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Test(String nom, String prenom, Integer age) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
    }
}
