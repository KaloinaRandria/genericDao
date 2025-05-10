package mg.working.test;

import mg.working.annotations.Column;
import mg.working.annotations.PrimaryKey;
import mg.working.annotations.Table;

@Table(name = "test2")
public class Test2 {
   @PrimaryKey
   @Column String id;
   @Column String nom;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Test2(String nom) {
        this.nom = nom;
    }
}
