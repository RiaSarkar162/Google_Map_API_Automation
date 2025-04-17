import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiTest {
    public static void main(String[] args) {

        RestAssured.baseURI = "https://rahulshettyacademy.com";

        // Add Place
        String response = given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(Payload.AddPlace())
                .when()
                .post("maps/api/place/add/json")
                .then()
                .assertThat().statusCode(200)
                .body("scope", equalTo("APP"))
                .header("server", "Apache/2.4.52 (Ubuntu)")
                .extract().response().asString();

        System.out.println("Response Body: " + response);

        JsonPath js = new JsonPath(response);
        String placeId = js.getString("place_id");
        System.out.println("Place Id: " + placeId);

        // Update Place
        String newAddress = "70 Summer walk, USA";
        given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\"" + placeId + "\",\n" +
                        "\"address\":\"" + newAddress + "\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when()
                .put("maps/api/place/update/json")
                .then()
                .log().all()
                .statusCode(200)
                .body("msg", equalTo("Address successfully updated"));

        // Get Place
        String getPlaceResponse = given().log().all()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                .when()
                .get("maps/api/place/get/json")
                .then()
                .assertThat().log().all()
                .statusCode(200)
                .extract().response().asString();

        JsonPath js1 = new JsonPath(getPlaceResponse);
        String actualAddress = js1.getString("address");
        System.out.println("Actual Address: " + actualAddress);
    }
}