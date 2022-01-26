package jsonplaceholderTests;

import base.BaseTests;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import userObject.Address;
import userObject.Company;
import userObject.Geo;
import userObject.Users;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class People extends BaseTests {
    private static String name;
    private static String username;
    private static String email;
    private static String phone;
    private static String website;
    private static String street;
    private static String suite;
    private static String city;
    private static String zipcode;
    private static String lat;
    private static String lng;
    private static String companyName;
    private static String catchPhrase;
    private static String bs;
    private static Company company;
    private static Geo geo;
    private static Address address;
    private static Users newUser;



    @BeforeEach
    public void createNewUserData() {
        name = faker.name().fullName();
        username = faker.name().username();
        email = faker.internet().emailAddress();
        phone = faker.phoneNumber().cellPhone();
        website = faker.internet().url();
        street = faker.address().streetAddress();
        suite = faker.address().buildingNumber();
        city = faker.address().city();
        zipcode = faker.address().zipCode();
        lat = faker.address().latitude();
        lng = faker.address().longitude();
        companyName = faker.company().name();
        catchPhrase = faker.lordOfTheRings().character();
        bs = faker.lordOfTheRings().location();
    }


    @Test
    @Order(1)
    public void getAllPeople() {
        Users[] user = given()
                .spec(reqSpec)
                .when()
                .get(BASE_URL + USERS)
                .as(Users[].class);

        List<Users> allUsers = Arrays.asList(user);

        assertThat(allUsers).hasSize(10);
        assertThat(allUsers.get(0).getName()).isEqualTo("Leanne Graham");
    }

    @Test
    @Order(2)
    public void checkIfTheresNoPlEmail() {
        Response response = given()
                .spec(reqSpec)
                .when()
                .get(BASE_URL + USERS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        List<String> emails = json.getList("email");

        boolean isPLEmail = emails.stream()
                .anyMatch(email -> email.contains(".pl"));

        assertThat(isPLEmail).isFalse();
    }

    @Test
    @Order(3)
    public void createNewEmployee() {
        geo = new Geo(lat, lng);
        company = new Company(companyName, catchPhrase, bs);
        address = new Address(street, suite, city, zipcode, geo);
        newUser = new Users(name, username, email, phone, website, address, company);

        Response response = given()
                .spec(reqSpec)
                .body(newUser)
                .when()
                .post(BASE_URL + USERS)
                .then()
                .statusCode(201)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo(name);
        assertThat(json.getString("username")).isEqualTo(username);
        assertThat(json.getString("address.street")).isEqualTo(street);
        assertThat(json.getString("id")).isEqualTo("11");
    }

    @Test
    @Order(4)
    public void updateOneParameterOfEmployee() throws JSONException {
        JSONObject userName = new JSONObject();
        userName.put("name", name);

        Response response = given()
                .spec(reqSpec)
                .body(userName.toString())
                .when()
                .patch(BASE_URL + USERS + "/1")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertEquals(name, json.get("name"));
    }
    @Test
    @Order(5)
    public void updateWholeEmployee(){

        Users newUser = new Users(name, username,email,
                website, phone, new Address(street,suite, city, zipcode, new Geo(lat,lng)), new Company(companyName, catchPhrase, bs));

        Response response = given()
                .spec(reqSpec)
                .body(newUser)
                .when()
                .put(BASE_URL + USERS + "/1")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();

        // first assert technique
        assertThat(newUser).matches(x -> x.getName() == name && x.getEmail() == email);

        // second  assert technique
        assertThat(json.getString("username"))
                .isNotBlank()
                .isEqualTo(username);
        assertThat(json.getString("website"))
                .isNotBlank()
                .isEqualTo(website)
                        .contains("www.")
                                .doesNotContain("https://");
        assertThat(json.getString("phone"))
                .isNotBlank()
                .isEqualTo(phone);
        assertThat(json.getString("address.street"))
                .isNotBlank()
                .isEqualTo(street);
        assertThat(json.getString("address.suite"))
                .isNotBlank()
                .isEqualTo(suite);
        assertThat(json.getString("address.city"))
                .isNotBlank()
                .isEqualTo(city);
        assertThat(json.getString("address.zipcode"))
                .isNotBlank()
                .isEqualTo(zipcode);
        assertThat(json.getString("address.geo.lat"))
                .isNotBlank()
                .isEqualTo(lat);
        assertThat(json.getString("address.geo.lng"))
                .isNotBlank()
                .isEqualTo(lng);
        assertThat(json.getString("company.name"))
                .isNotBlank()
                .isEqualTo(companyName);
        assertThat(json.getString("company.catchPhrase"))
                .isNotBlank()
                .isEqualTo(catchPhrase);
        assertThat(json.getString("company.bs"))
                .isNotBlank()
                .isEqualTo(bs);
    }
}

