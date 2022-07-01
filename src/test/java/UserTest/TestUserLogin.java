package UserTest;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import testModel.User.UserModel;
import testModel.User.UserClient;
import testModel.User.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestUserLogin {

    private UserClient userClient;
    private UserCredentials creds;
    private UserModel user;
    private String bearerToken;
    private String expected;
    private Response response;

    @Before
    public void setUp() {

        userClient = new UserClient();
        user = UserModel.getRandom();
        creds = UserCredentials.from(user);

        userClient.registerNewUser(user);
        bearerToken = userClient.login(creds)
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("accessToken");
    }

    @After
    public void teardown(){
        userClient.delete(user.getEmail(), bearerToken);
    }

    @Test
    @DisplayName("Check user login with an incorrect email fails")
    public void testUserLoginWithIncorrectEmailFails() {
        expected = "email or password are incorrect";
        creds = UserCredentials.fromIncorrectLoginName(user);
        response = userClient.login(creds);

       boolean loginSuccess = response
                .then().log().all()
                .assertThat()
                .statusCode(401)
                .extract()
                .path("success");

        String message = response
                .then().log().all()
                .extract()
                .path("message");

        assertFalse(loginSuccess);
        assertEquals(expected, message);

    }

    @Test
    @DisplayName("Check user login with an incorrect pass fails")
    public void testUserLoginWithIncorrectPassFails() {
        expected = "email or password are incorrect";
        creds = UserCredentials.fromIncorrectPass(user);
        response = userClient.login(creds);

       boolean loginSuccess = response
                .then().log().all()
                .assertThat()
                .statusCode(401)
                .extract()
                .path("success");

        String message = response
                .then().log().all()
                .extract()
                .path("message");

        assertFalse(loginSuccess);
        assertEquals(expected, message);

    }

    @Test
    @DisplayName("Check user login with an empty email fails")
    public void testUserLoginWithEmptyEmailFails() {
        expected = "email or password are incorrect";
        creds = UserCredentials.fromEmptyLoginName(user);
        response = userClient.login(creds);

        boolean loginSuccess = response
                .then().log().all()
                .assertThat()
                .statusCode(401)
                .extract()
                .path("success");

        String message = response
                .then().log().all()
                .extract()
                .path("message");

        assertFalse(loginSuccess);
        assertEquals(expected, message);

    }

    @Test
    @DisplayName("Check user login with an empty pass fails")
    public void testUserLoginWithEmptyPassFails() {
        expected = "email or password are incorrect";
        creds = UserCredentials.fromEmptyPass(user);
        response = userClient.login(creds);

        boolean loginSuccess = response
                .then().log().all()
                .assertThat()
                .statusCode(401)
                .extract()
                .path("success");

        String message = response
                .then().log().all()
                .extract()
                .path("message");

        assertFalse(loginSuccess);
        assertEquals(expected, message);
    }
}

