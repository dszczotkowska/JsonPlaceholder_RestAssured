package base;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class BaseTests {
    protected static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    protected static final String USERS = "users";
    protected static final String POSTS = "posts";
    protected static final String COMMENTS = "comments";
    protected static final String ALBUMS = "albums";

    protected static RequestSpecBuilder reqBuilder;
    protected static RequestSpecification reqSpec;

    protected static Faker faker;

    @BeforeAll
    public static void beforeAll() {
        reqBuilder = new RequestSpecBuilder();
        reqBuilder.setContentType("application/json");

        reqSpec = reqBuilder.build();

        faker = new Faker();

    }
}