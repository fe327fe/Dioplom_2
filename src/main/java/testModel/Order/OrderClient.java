package testModel.Order;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import testModel.RestAssuredClient;

public class OrderClient extends RestAssuredClient{
    private final String ORDERS = "/orders";
    private final String HISTORY = "/orders/user?={user}";

    public Response createNewOrder(OrderModel order, String bearerToken) {
        RequestSpecification oauthSpec = getOauthSpec(bearerToken);

        return oauthSpec
                .body(order)
                .when()
                .post(ORDERS);
    }

    public Response getOrderHistory(String user, String bearerToken) {
        RequestSpecification oauthSpec = getOauthSpec(bearerToken);

        return oauthSpec
                .pathParams("user", user)
                .when()
                .get(HISTORY);
    }

    public Response getOrders() {

        return reqSpec
                .when()
                .get(ORDERS);
    }
}
