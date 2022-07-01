package testModel;

import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class RestAssuredClient {

    private String oauthToken;

    protected final String URL = "https://stellarburgers.nomoreparties.site/api";
    protected final RequestSpecification reqSpec = given()
            .baseUri (URL)
            .header("Content-type", "application/json");


    public RequestSpecification getOauthSpec (String oauthToken){

        this.oauthToken = oauthToken;

        RequestSpecification oauthSpec = given()
                .auth().oauth2(oauthToken.replaceFirst("Bearer ", ""))
                .baseUri (URL)
                .header("Content-type", "application/json");;
        return oauthSpec;
    }
}
