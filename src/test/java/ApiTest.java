import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.exe.MyDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;

public class ApiTest {
    static Map<String, String> headers = new HashMap<>();
    static Properties prop = new Properties();

    ResponseSpecification responseSpecification = null;


    @BeforeAll
    static void setUp() throws IOException {
        FileInputStream fis;
        fis= new FileInputStream("src/main/resources/my.properties");
        prop.load(fis);
        RestAssured.filters(new AllureRestAssured());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();


    }
    @BeforeEach
    void respSpec(){
        responseSpecification =  new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(JSON)
                .expectResponseTime(Matchers.lessThan(5000l))
                .build();
    }


    @Test
    void coronaV(){

        given().log().all()

                .headers(headers)
                .when()
                .contentType(JSON)
                .get((String) prop.get("getUrl"))
                .prettyPeek()
                .then()
                .body("status", equalTo("success"));


    }
    @Test

    void coronaCountry () {

        given().log().method()
                .headers(headers)
                .when()
                .contentType(JSON)
                .get((String) prop.get("getUrl2"))
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .body("status", equalTo("success"));




    }

    @Test



    void reqresCreate(){
        MyDTO myDTO = new MyDTO();
        myDTO.setName("morpheus");
        myDTO.setJob("leader");

        given().log().all()
                .when()
                .body(myDTO)
                .contentType(JSON)
                .request("POST", "https://reqres.in/api/users")
                .prettyPeek()
                .then()
                .statusCode(201)
                .body("name", equalTo("morpheus"));



    }

    @Test
    void reqresRegister(){
        MyDTO myDTO = new MyDTO();
        myDTO.setEmail("eve.holt@reqres.in");
        myDTO.setPassword("pistol");
        given().log().all()
                .when()
                .body(myDTO)
                .contentType(JSON)
                .request("POST", "https://reqres.in/api/register")
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .body("id", equalTo(4));

    }

}


