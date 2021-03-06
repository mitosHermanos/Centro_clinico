package hermanos.Centro.Clinico.dto;


import hermanos.Centro.Clinico.model.Clinic;

public class EditClinicDTO {
    private long id;
    private String name;
    private String description;
    private String street;
    private String number;
    private String city;
    private String postcode;
    private String country;

    public EditClinicDTO() {
        super();
    }

    public EditClinicDTO(Clinic clinic) {
        this.id = clinic.getId();
        this.name = clinic.getName();
        this.description = clinic.getDescription();
        this.street = clinic.getAddress().getStreet();
        this.number = clinic.getAddress().getNumber();
        this.city = clinic.getAddress().getCity();
        this.postcode = clinic.getAddress().getPostcode();
        this.country = clinic.getAddress().getCountry();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
