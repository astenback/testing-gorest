import io.restassured.http.ContentType;
import io.restassured.response.Response;

import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;

public class GoRest {

    private static String baseURI;
    private static String bearerToken = "JGPtiMB578pYv3L9Djd_iV7lgvuywh-Owe4n";
    private static String jsonAsString = "";

    private static int port;
    private static int statusCode;
    private static String userId;

    @BeforeTest
    public void setup() {

        GoRest.baseURI = "https://gorest.co.in/public-api";
        GoRest.port = 80;
    }

    @Test
    @Parameters({"path", "expectedStatusCode"})
    public void get(@Optional("/users") String path, @Optional("200") int expectedStatusCode){

        String endpoint = baseURI + path;
        System.out.println(endpoint);

        // Get All Users
        Response response = given()
                .headers("Authorization","Bearer " + bearerToken,"Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                .get(endpoint)
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();

        statusCode = response.getStatusCode();

        System.out.println("Asserting return Status Code for " + path + " equals " + expectedStatusCode);
        assertEquals(statusCode, expectedStatusCode);

        // Print JSON response as string
        jsonAsString = response.asString();
        System.out.println("Response Body: " + jsonAsString);

    }

    @Test
    @Parameters({"path", "firstName", "lastName", "gender", "email", "expectedStatusCode"})
    public void post(@Optional("/users") String path,
                     @Optional("Alan") String firstName,
                     @Optional("Stenback") String lastName,
                     @Optional("male") String gender,
                     @Optional("alan.stenback@1234.com") String email,
                     @Optional("302") int expectedStatusCode){

        String endpoint = baseURI + path;
        System.out.println(endpoint);

        RequestSpecification request = given();

        // Build POST Body
        JSONObject requestParams = new JSONObject();
        requestParams.put("first_name", firstName);
        requestParams.put("last_name", lastName);
        requestParams.put("gender", gender);
        requestParams.put("email",  email);

        // Add the Json to the body of the request
        request.body(requestParams.toJSONString());

        // Post the request and check the response

        Response response = given()
                .headers("Authorization","Bearer " + bearerToken,"Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                .body(requestParams)
                .post(endpoint)
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();

        // Print JSON response as string
        jsonAsString = response.asString();
        System.out.println("Response Body: " + jsonAsString);

        statusCode = response.getStatusCode();

        System.out.println("Asserting return Status Code for " + path + " equals " + expectedStatusCode);
        assertEquals(statusCode, expectedStatusCode);

        // Store userId
        userId = response.jsonPath().getString("result.id");
        System.out.println("userId: " + userId);

    }

    @Test
    @Parameters({"path", "dob", "phone", "website", "address", "status", "expectedStatusCode"})
    public void put(@Optional("/users") String path,
                     @Optional("03/20/1973") String dob,
                     @Optional("phone") String phone,
                     @Optional("https://wwww.alanstenback.us") String website,
                     @Optional("po box 123, fairplay, co 80440") String address,
                     @Optional("active") String status,
                     @Optional("200") int expectedStatusCode){

        String endpoint = baseURI + path + "/" + userId;
        System.out.println(endpoint);

        RequestSpecification request = given();

        // Build POST Body
        JSONObject requestParams = new JSONObject();
        requestParams.put("dob", dob);
        requestParams.put("phone", phone);
        requestParams.put("website", website);
        requestParams.put("address",  address);
        requestParams.put("status",  status);


        // Add the Json to the body of the request
        request.body(requestParams.toJSONString());

        // Put the request and check the response
        Response response = given()
                .headers("Authorization","Bearer " + bearerToken,"Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                .body(requestParams)
                .put(endpoint)
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();

        // Print JSON response as string
        jsonAsString = response.asString();
        System.out.println("Response Body: " + jsonAsString);

        statusCode = response.getStatusCode();

        System.out.println("Asserting return Status Code for " + path + " equals " + expectedStatusCode);
        assertEquals(statusCode, expectedStatusCode);


    }

    @Test
    @Parameters({"path", "expectedStatusCode"})
    public void delete(@Optional("/users") String path, @Optional("200") int expectedStatusCode){

        String endpoint = baseURI + path + "/" + userId;
        System.out.println(endpoint);

        // Put the request and check the response
        Response response = given()
                .headers("Authorization","Bearer " + bearerToken,"Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                .delete(endpoint)
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();

        // Print JSON response as string
        jsonAsString = response.asString();
        System.out.println("Response Body: " + jsonAsString);

        statusCode = response.getStatusCode();

        System.out.println("Asserting return Status Code for " + path + " equals " + expectedStatusCode);
        assertEquals(statusCode, expectedStatusCode);


    }

    /**

    public void getAllUserNames(){

        String endpoint = baseURI + "/users";
        int statusCode;

        // Get All Users
        Response response = given()
                            .headers("Authorization","Bearer " + bearerToken,"Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                            .get(endpoint)
                            .then()
                            .contentType(ContentType.JSON)
                            .extract()
                            .response();

        statusCode = response.getStatusCode();
        assertEquals(statusCode, 200);

        // Print JSON response as string
        jsonAsString = response.asString();
        System.out.println("Reponse Body: " + jsonAsString);

        // Extract meta data
        String success = response.jsonPath().getString("_meta.success");
        String code = response.jsonPath().getString("_meta.code");
        String message = response.jsonPath().getString("_meta.message");
        String totalCount = response.jsonPath().getString("_meta.totalCount");
        String pageCount = response.jsonPath().getString("_meta.pageCount");
        String currentPage = response.jsonPath().getString("_meta.currentPage");
        String perPage = response.jsonPath().getString("_meta.perPage");

        // Print meta data
        System.out.println("Success: " + success);
        System.out.println("Code: " + code);
        System.out.println("Message: " + message);
        System.out.println("totalCount: " + totalCount);
        System.out.println("pageCount: " + pageCount);
        System.out.println("currentPage: " + currentPage);
        System.out.println("perPage: " + perPage);

        // Print Ids
        List<String> ids = response.jsonPath().getList("result.id");
        for(String i:ids) {
            System.out.println(i);
        }

        // Print Names
        List<String> first_names = response.jsonPath().getList("result.first_name");
        for(String i:first_names) {
            System.out.println(i);
        }

        // Print Names
        List<String> last_names = response.jsonPath().getList("result.last_name");
        for(String i:last_names) {
            System.out.println(i);
        }

    }

     **/

}
