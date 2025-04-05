package unlp.info.bd2.model;


import jakarta.persistence.*;

@Entity
@Table(name = "stop")
public class Stop {

    public Stop(){
        
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // si es autoincremental en la DB
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
