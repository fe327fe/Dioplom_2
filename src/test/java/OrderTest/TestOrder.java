package OrderTest;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testModel.Order.OrderClient;
import testModel.Order.OrderModel;
import testModel.User.UserClient;
import testModel.User.UserCredentials;
import testModel.User.UserModel;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestOrder {

    private UserClient userClient;
    private UserCredentials creds;
    private UserModel user;
    private String bearerToken;
    private OrderClient orderClient;
    private OrderModel order;
    private Response response;
    private String expected;

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

        orderClient = new OrderClient();
        order = OrderModel.getDefaultOrder();

    }

    @After
    public void teardown(){
        userClient.delete(user.getEmail(), bearerToken);
    }

    @Test
    @DisplayName("Check successfully creating a new order with Authorization")
    public void testSuccessfullyCreateNewOrderWithAuth() {
        String expected = "Бессмертный spicy флюоресцентный бургер";
        response = orderClient.createNewOrder(order, bearerToken);

       boolean orderCreated = response
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("success");

        String actual = response
                .then().log().all()
                .extract()
                .path("name");

        assertTrue(orderCreated);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Check successfully creating a new order without Authorization")
    public void testSuccessfullyCreateNewOrderWithNoAuth() {
        String expected = "Бессмертный spicy флюоресцентный бургер";
        response = orderClient.createNewOrder(order, "");

        boolean orderCreated = response
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("success");

        String actual = response
                .then().log().all()
                .extract()
                .path("name");

        assertTrue(orderCreated);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Check creating a new order with no Ingredients fails")
    public void testCreateNewOrderWithNoIngredientsFails() {
        expected = "Ingredient ids must be provided";
        ArrayList<String> emptyList = new ArrayList<>();
        order.setIngredients(emptyList);

        response = orderClient.createNewOrder(order, bearerToken);

        boolean orderCreated = response
                .then().log().all()
                .assertThat()
                .statusCode(400)
                .extract()
                .path("success");

        String actual = response
                .then().log().all()
                .extract()
                .path("message");

        assertFalse(orderCreated);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Check creating a new order with wrong Ingredients hash fails")
    public void testCreateNewOrderWithWrongIngredientsHashFails() {
        order = OrderModel.getWrongHash();
        orderClient.createNewOrder(order, bearerToken)
                .then().log().all()
                .assertThat()
                .statusCode(500);
    }

    @Test
    @DisplayName("Check successfully getting order history with Authorization")
    public void testSuccessfullyGetOrderHistoryWithAuth() {
        orderClient.createNewOrder(order, bearerToken);
        response = orderClient.getOrderHistory(user.getEmail(), bearerToken);

        boolean historyReceived = response
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("success");

        ArrayList<String> ordersHistory = response
                .then().log().all()
                .extract()
                .path("orders");

        assertTrue(historyReceived);
        assertNotNull(ordersHistory);
    }

    @Test
    @DisplayName("Check getting orders without Authorization fails")
    public void testGetOrderHistoryWithNoAuthFails() {
        expected = "You should be authorised";
        response = orderClient.getOrders();

        boolean historyReceived = response
                .then().log().all()
                .assertThat()
                .statusCode(401)
                .extract()
                .path("success");

        String actual = response
                .then().log().all()
                .extract()
                .path("message");

        assertFalse(historyReceived);
        assertEquals(expected, actual);
    }
}

