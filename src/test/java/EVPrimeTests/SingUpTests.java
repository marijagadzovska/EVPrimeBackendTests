package EVPrimeTests;


import client.EVPrimeClient;
import data.SignUpLoginDataFactory;
import io.restassured.response.Response;
import models.request.SignUpLoginRequest;
import models.response.ErrorResponse;
import models.response.SignUpResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static objectbuilder.SignUpObjectBuilder.createBodyForSignUp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SingUpTests {

    @Test
    public void successfulSignUpTest(){
        SignUpLoginRequest signUpRequest = new SignUpLoginDataFactory(createBodyForSignUp())
                .setEmail(RandomStringUtils.randomAlphabetic(10) + "@mail.com")
                .setPassword(RandomStringUtils.randomAlphabetic(10))
                .createRequest();

        Response response = new EVPrimeClient().signUp(signUpRequest);

        SignUpResponse signUpResponse = response.body().as(SignUpResponse.class);

        assertEquals(201,response.statusCode());
        assertEquals("User created.",signUpResponse.getMessage());
        assertNotNull(signUpResponse.getUser().getId());
        assertEquals(signUpRequest.getEmail(),signUpResponse.getUser().getEmail());
        assertNotNull(signUpResponse.getToken());

    }

    @Test
    public void unsuccessfulSignUpEmailAlreadyExists(){
        SignUpLoginRequest signUpRequest = new SignUpLoginDataFactory(createBodyForSignUp())
                .setEmail(RandomStringUtils.randomAlphabetic(10) + "@mail.com")
                .setPassword(RandomStringUtils.randomAlphabetic(10))
                .createRequest();

        new EVPrimeClient()
                .signUp(signUpRequest);

        Response secondResponse = new EVPrimeClient()
                .signUp(signUpRequest);

        ErrorResponse signUpErrorResponse = secondResponse.body().as(ErrorResponse.class);

        assertEquals(422,secondResponse.getStatusCode());
        assertEquals("User signup failed due to validation errors.",signUpErrorResponse.getMessage());
        assertEquals("Email exists already.",signUpErrorResponse.getErrors().getEmail());
    }

    @Test
    public void unsuccessfulSignUpWrongEmailFormat(){
        SignUpLoginRequest signUpRequest = new SignUpLoginDataFactory(createBodyForSignUp())
                .setEmail(RandomStringUtils.randomAlphabetic(10) + "mail.com")
                .setPassword(RandomStringUtils.randomAlphabetic(10))
                .createRequest();

        Response response = new EVPrimeClient()
                .signUp(signUpRequest);

        ErrorResponse signUpErrorResponse = response.body().as(ErrorResponse.class);

        assertEquals(422, response.getStatusCode());
        assertEquals("User signup failed due to validation errors.",signUpErrorResponse.getMessage());
        assertEquals("Invalid email.",signUpErrorResponse.getErrors().getEmail());
    }

    @Test
    public void unsuccessfulSignUpWrongPassword(){
        SignUpLoginRequest signUpRequest = new SignUpLoginDataFactory(createBodyForSignUp())
                .setEmail(RandomStringUtils.randomAlphabetic(10) + "@mail.com")
                .setPassword(RandomStringUtils.randomAlphabetic(5))
                .createRequest();

        Response response = new EVPrimeClient()
                .signUp(signUpRequest);

        ErrorResponse signUpErrorResponse = response.body().as(ErrorResponse.class);

        assertEquals(422, response.getStatusCode());
        assertEquals("User signup failed due to validation errors.",signUpErrorResponse.getMessage());
        assertEquals("Invalid password. Must be at least 6 characters long.",signUpErrorResponse.getErrors().getPassword());
    }
}






