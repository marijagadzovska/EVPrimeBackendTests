package EVPrimeTests;

import client.EVPrimeClient;
import data.SignUpLoginDataFactory;
import io.restassured.response.Response;
import models.request.SignUpLoginRequest;
import models.response.ErrorResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static objectbuilder.SignUpObjectBuilder.createBodyForSignUp;
import static org.junit.Assert.assertEquals;

public class UnsuccessfulLogInTests {
    SignUpLoginRequest signUpRequest;

    @Test
    public void unsuccessfulLogInWrongEmail(){
        signUpRequest = new SignUpLoginDataFactory(createBodyForSignUp())
                .setEmail("emailNotFoundInDb@mail.com")
                .setPassword(RandomStringUtils.randomAlphabetic(6))
                .createRequest();

        Response response = new EVPrimeClient()
                .login(signUpRequest);

        ErrorResponse loginErrorResponse = response.body().as(ErrorResponse.class);

        assertEquals(401,response.getStatusCode());
        assertEquals("Authentication failed.",loginErrorResponse.getMessage());
    }

    @Test
    public void unsuccessfulLogInWrongPassword(){
        signUpRequest = new SignUpLoginDataFactory(createBodyForSignUp())
                .setEmail("test@email.com")
                .setPassword(RandomStringUtils.randomAlphabetic(5))
                .createRequest();

        Response response = new EVPrimeClient()
                .login(signUpRequest);

        ErrorResponse loginErrorResponse = response.body().as(ErrorResponse.class);

        assertEquals(422,response.getStatusCode());
        assertEquals("Invalid credentials.",loginErrorResponse.getMessage());
        assertEquals("Invalid email or password entered.",loginErrorResponse.getErrors().getCredentials());
    }
}
