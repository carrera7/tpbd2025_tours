package unlp.info.bd2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;

@Entity
@Table(name = "revisiones")
public class Review {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="revision_id")
    private Long id;

    @Column(name="puntuacion")
    private int rating;

    @Column(name="comentario")
    private String comment;

    @OneToOne
    @JoinColumn(name = "compras_id", referencedColumnName = "compras_id")
    private Purchase purchase;

    public Review() {
    }

    /**
     * Constructor con parámetros (sin id, ya que se genera automáticamente)
     * @param rating Puntuación de la revisión
     * @param comment Comentario de la revisión
     * @param purchase Compra asociada a la revisión
     */
    public Review(int rating, String comment, Purchase purchase) {
        this.rating = rating;
        this.comment = comment;
        this.purchase = purchase;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }
}
