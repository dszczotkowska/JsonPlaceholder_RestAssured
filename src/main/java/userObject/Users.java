package userObject;

import lombok.Data;

@Data
public class Users {
    private String id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
    private Address address;
    private Company company;

    public Users(String name, String username, String email, String website, String phone, Address address, Company company) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.website = website;
        this.address = address;
        this.company = company;
    }

    public Users (){

    }


}