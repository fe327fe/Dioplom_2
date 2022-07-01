package testModel.User;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import testModel.RestAssuredClient;

public class UserClient extends RestAssuredClient{
    private final String REGISTER = "/auth/register";
    private final String LOGIN = "/auth/login";
    private final String DELETE = "/auth/user?={user}";
    private final String USER = "/auth/user";

    public Response registerNewUser(UserModel user) {
        return reqSpec
                .body(user)
                .when()
                .post(REGISTER);
    }

    public Response login(UserCredentials creds) {
        return reqSpec
                .body(creds)
                .when()
                .post(LOGIN);
    }
    public Response modifyExistingUser(UserModel user, String bearerToken) {
        RequestSpecification oauthSpec = getOauthSpec(bearerToken);

        return oauthSpec
                .body(user)
                .when()
                .patch(USER);
    }

    public void delete(String user, String bearerToken) {
        RequestSpecification oauthSpec = getOauthSpec(bearerToken);

        oauthSpec
                .pathParams("user", user)
                .when()
                .delete(DELETE)
                .then().log().all()
                .assertThat()
                .statusCode(202);
    }
}
