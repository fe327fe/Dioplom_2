package UserTest;

import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;
import testModel.User.UserModel;
import testModel.User.UserClient;
import testModel.User.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCreateUser {

    private UserClient userClient;
    private UserModel user;
    private UserCredentials creds;
    private String bearerToken;
    private Response response;
    private Response loginResponse;
    private String expected;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserModel.getRandom();
        creds = UserCredentials.from(user);
        response = userClient.registerNewUser(user);
        loginResponse = userClient.login(creds);

        bearerToken = loginResponse
                .then().log().all()
                .assertThat()
                .extract()
                .path("accessToken");
    }

    @After
    public void teardown(){
        userClient.delete(user.getEmail(), bearerToken);
    }

    @Test
    @DisplayName("Test successfully creating a new user and login in, using new credentials")
    public void testSuccessfullyCreateNewUserAndLoginUsingNewCreds() {

        boolean userCreated = response
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("success");

        String userName = response
                .then().log().all()
                .extract()
                .path("user.name");

        boolean loginSuccess = loginResponse
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("success");

        assertTrue(userCreated);
        assertTrue(loginSuccess);

        assertEquals(user.getName(), userName);
    }

    @Test
    @DisplayName("Check creating two users with the same login name fails")
    public void testCreateTwoUsersWithTheSameNameFails() {
        expected = "User already exists";
        response = userClient.registerNewUser(user);

        boolean userCreated = response
                .then().log().all()
                .assertThat()
                .statusCode(403)
                .extract()
                .path("success");

        String message = response
                .then().log().all()
                .extract()
                .path("message");

        assertFalse(userCreated);
        assertEquals(expected, message);
    }

    @Test
    @DisplayName("Check creating a user with an empty pass fails")
    public void testCreateUserWithEmptyPassFails() {
        expected = "Email, password and name are required fields";
        UserModel userWithEmptyPass = UserModel.getEmptyPass();
        response = userClient.registerNewUser(userWithEmptyPass);

        boolean userCreated = response
                .then().log().all()
                .assertThat()
                .statusCode(403)
                .extract()
                .path("success");

        String message = response
                .then().log().all()
                .extract()
                .path("message");

        assertFalse(userCreated);
        assertEquals(expected, message);
    }

    @Test
    @DisplayName("Check creating a user with an empty name fails")
    public void testCreateUserWithEmptyNameFails() {
        expected = "Email, password and name are required fields";
        UserModel userWithEmptyName = UserModel.getEmptyName();
        response = userClient.registerNewUser(userWithEmptyName);

        boolean userCreated = response
                .then().log().all()
                .assertThat()
                .statusCode(403)
                .extract()
                .path("success");

        String message = response
                .then().log().all()
                .extract()
                .path("message");

        assertFalse(userCreated);
        assertEquals(expected, message);
    }

    @Test
    @DisplayName("Check creating a user with an empty email fails")
    public void testCreateUserWithEmptyEmailFails() {
        expected = "Email, password and name are required fields";
        UserModel userWithEmptyEmail = UserModel.getEmptyEmail();
        response = userClient.registerNewUser(userWithEmptyEmail);

        boolean userCreated = response
                .then().log().all()
                .assertThat()
                .statusCode(403)
                .extract()
                .path("success");

        String message = response
                .then().log().all()
                .extract()
                .path("message");

        assertFalse(userCreated);
        assertEquals(expected, message);
    }
}
