package mg.working.test;

import mg.working.annotations.Column;
import mg.working.annotations.PrimaryKey;
import mg.working.annotations.Table;
import mg.working.utility.IdGenerator;

@Table(name = "test2")
public class Test2 {

   @Column String id;
   @Column String nom;

    public String getId() {
        return id;
    }

    public void setId(IdGenerator idGenerator) {
        String id = "";
        id = idGenerator.generateId("T","test2_id");
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
