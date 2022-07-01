package testModel.User;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserModel {

    private String email;
    private String name;
    private String password;

    public static UserModel getRandom () {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String name = faker.name().fullName();
        String password = RandomStringUtils.randomAlphabetic(10);

        return new UserModel(email, name, password);
    }

    public static UserModel getEmptyPass () {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String name = faker.name().fullName();
        String password = "";

        return new UserModel(email, name, password);
    }

    public static UserModel getEmptyName () {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String name = "";
        String password = RandomStringUtils.randomAlphabetic(10);

        return new UserModel(email, name, password);
    }

    public static UserModel getEmptyEmail () {
        Faker faker = new Faker();
        String email = "";
        String name = faker.name().fullName();
        String password = RandomStringUtils.randomAlphabetic(10);

        return new UserModel(email, name, password);
    }

}
