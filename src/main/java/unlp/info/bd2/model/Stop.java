package unlp.info.bd2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Table(name="stops")
public class Stop {

    public Stop(){
        
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //si es auto lo define el motor de la db , si es identitty es acpetablo por SQL y es auto incrementable
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description",columnDefinition = "TEXT") // o ajustá según el tipo real
    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
