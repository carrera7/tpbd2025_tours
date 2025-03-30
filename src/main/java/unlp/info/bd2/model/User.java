package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "usuarios_id")
    private Long id;

    @Column(name = "nombre_usuario")
    private String username;

    @Column(name = "contraseña")
    private String password;

    @Column(name = "nombre")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "fecha_de_naciemiento")
    private Date birthdate;

    @Column(name = "numero_de_telefono")
    private String phoneNumber;

    @Column(name = "activo")
    private boolean active;

    @OneToMany
    @Column(name = "lista_compras")
    private List<Purchase> purchaseList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Purchase> getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(List<Purchase> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /*
     * agrega un purchase a la lista de compras del usuario
     * si la lista no existe, la crea
     * @param purchase el purchase a agregar
     * @return void 
     */

    public void addPurchase(Purchase purchase) {
        if (this.purchaseList == null) {
            this.purchaseList = new ArrayList<>();
        }
        this.purchaseList.add(purchase);
    }

    /*
     * desactiva el usuario
     * @return boolean true si el usuario se puede desactivar, false si no
     */

    public boolean canBeDesactivated() {
        return this.purchaseList == null || this.purchaseList.isEmpty();
    }
}
