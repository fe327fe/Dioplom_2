package UserTest;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testModel.User.UserClient;
import testModel.User.UserCredentials;
import testModel.User.UserModel;

import static org.junit.Assert.*;

public class TestModifyUser {

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
    @DisplayName("Test modifying an existing user, without authorization fails")
    public void testModifyExistingUserWthNoAuthFails() {
        expected = "You should be authorised";
        response = userClient.modifyExistingUser(user, "");

        boolean userCreated = response
                .then().log().all()
                .assertThat()
                .statusCode(401)
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
    @DisplayName("Test successfully modifying an existing user email field")
    public void testSuccessfullyModifyExistingUserEmail() {

        Faker faker = new Faker();
        String expectedEmail = faker.internet().emailAddress();
        user.setEmail(expectedEmail);
        response = userClient.modifyExistingUser(user, bearerToken);

        boolean userUpdated = response
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("success");

        String actualEmail = response
                .then().log().all()
                .extract()
                .path("user.email");

        assertTrue(userUpdated);
        assertEquals(expectedEmail, actualEmail);
    }

    @Test
    @DisplayName("Test successfully modifying an existing User name field")
    public void testSuccessfullyModifyExistingUserName() {

        Faker faker = new Faker();
        String expectedName = faker.name().fullName();
        user.setName(expectedName);
        response = userClient.modifyExistingUser(user, bearerToken);

        boolean userUpdated = response
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("success");

        String actualName = response
                .then().log().all()
                .extract()
                .path("user.name");

        assertTrue(userUpdated);
        assertEquals(expectedName, actualName);
    }
}
