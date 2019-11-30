package hermanos.Centro.Clinico.model;


import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "CHECKUP_TYPE")
public class CheckupType {

    @ManyToOne
    @JoinColumn(name = "clinic", referencedColumnName = "clinic_id", nullable = true)
    private Clinic clinic;

    @Id
    @Column(nullable = false, unique = true)
    private String name;


    public CheckupType(){
        super();
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
