package testModel.User;

import lombok.*;

@Builder
@Setter
@Data
@AllArgsConstructor
@ToString

public class UserCredentials {
    private String email;
    private String password;

    public UserCredentials(UserModel user){
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public static UserCredentials from (UserModel user){

        return new UserCredentials(user);
    }

    public static UserCredentials fromIncorrectLoginName (UserModel user){
        UserCredentials creds = UserCredentials.builder()
                .email("thisIsVeryWrong")
                .password(user.getPassword())
                .build();
        return creds;
    }

    public static UserCredentials fromIncorrectPass(UserModel user) {
        UserCredentials creds = UserCredentials.builder()
                .email(user.getEmail())
                .password("iAmAnIncorrectPass")
                .build();
        return creds;
    }

    public static UserCredentials fromEmptyLoginName (UserModel user){
        UserCredentials creds = UserCredentials.builder()
                .email("")
                .password(user.getPassword())
                .build();
        return creds;
    }

    public static UserCredentials fromEmptyPass (UserModel user){
        UserCredentials creds = UserCredentials.builder()
                .email(user.getEmail())
                .password("")
                .build();
        return creds;
    }
}
