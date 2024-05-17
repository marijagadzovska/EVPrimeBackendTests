package EVPrimeTests;

import client.EVPrimeClient;
import data.PostEventDataFactory;
import data.SignUpLoginDataFactory;
import io.restassured.response.Response;
import models.request.PostUpdateEventRequest;
import models.request.SignUpLoginRequest;
import models.response.ErrorResponse;
import models.response.LoginResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static objectbuilder.PostEventObjectBuilder.createBodyForPostEvent;
import static objectbuilder.SignUpObjectBuilder.createBodyForSignUp;
import static org.junit.Assert.assertEquals;

public class UnsuccessfulPostEventsTests {

    SignUpLoginRequest signUpRequest;

    @Before
    public void setUp() {
        signUpRequest = new SignUpLoginDataFactory(createBodyForSignUp())
                .setEmail(RandomStringUtils.randomAlphabetic(10) + "@mail.com")
                .setPassword(RandomStringUtils.randomAlphabetic(10))
                .createRequest();

        new EVPrimeClient()
                .signUp(signUpRequest);

    }

    @Test
    public void unsuccessfulPostEventEmptyTitle() {
        Response loginResponse = new EVPrimeClient()
                .login(signUpRequest);

        LoginResponse loginResponseBody = loginResponse.body().as(LoginResponse.class);

        PostUpdateEventRequest requestBody = new PostEventDataFactory(createBodyForPostEvent())
                .setTitle("")
                .setImage("https://s.hdnux.com/photos/01/37/20/53/24972014/3/960x0.webp")
                .setDate("05-03-2024")
                .setLocation("American Airlines Center, Dallas, Texas, USA")
                .setDescription("Expect high-energy play and intense competition in this exciting basketball showdown.")
                .createRequest();

        Response response = new EVPrimeClient()
                .postEvent(requestBody, loginResponseBody.getToken());

        ErrorResponse loginErrorResponse = response.body().as(ErrorResponse.class);

        assertEquals(422, response.statusCode());
        assertEquals("Adding the event failed due to validation errors.", loginErrorResponse.getMessage());
        assertEquals("Invalid title.", loginErrorResponse.getErrors().getTitle());
    }

    @Test
    public void unsuccessfulPostEventEmptyImage() {
        Response loginResponse = new EVPrimeClient()
                .login(signUpRequest);

        LoginResponse loginResponseBody = loginResponse.body().as(LoginResponse.class);

        PostUpdateEventRequest requestBody = new PostEventDataFactory(createBodyForPostEvent())
                .setTitle("Dallas Mavericks - LA Clippers")
                .setImage("")
                .setDate("05-03-2024")
                .setLocation("American Airlines Center, Dallas, Texas, USA")
                .setDescription("Expect high-energy play and intense competition in this exciting basketball showdown.")
                .createRequest();

        Response response = new EVPrimeClient()
                .postEvent(requestBody, loginResponseBody.getToken());

        ErrorResponse loginErrorResponse = response.body().as(ErrorResponse.class);

        assertEquals(422, response.statusCode());
        assertEquals("Adding the event failed due to validation errors.", loginErrorResponse.getMessage());
        assertEquals("Invalid image.", loginErrorResponse.getErrors().getImage());
    }

    @Test
    public void unsuccessfulPostInvalidImage() {
        Response loginResponse = new EVPrimeClient()
                .login(signUpRequest);

        LoginResponse loginResponseBody = loginResponse.body().as(LoginResponse.class);

        PostUpdateEventRequest requestBody = new PostEventDataFactory(createBodyForPostEvent())
                .setTitle("Dallas Mavericks - LA Clippers")
                .setImage("image")
                .setDate("05-03-2024")
                .setLocation("American Airlines Center, Dallas, Texas, USA")
                .setDescription("Expect high-energy play and intense competition in this exciting basketball showdown.")
                .createRequest();

        Response response = new EVPrimeClient()
                .postEvent(requestBody, loginResponseBody.getToken());

        ErrorResponse loginErrorResponse = response.body().as(ErrorResponse.class);

        assertEquals(422, response.statusCode());
        assertEquals("Adding the event failed due to validation errors.", loginErrorResponse.getMessage());
        assertEquals("Invalid image.", loginErrorResponse.getErrors().getImage());
    }

    @Test
    public void unsuccessfulPostEventInvalidDate() {
        Response loginResponse = new EVPrimeClient()
                .login(signUpRequest);

        LoginResponse loginResponseBody = loginResponse.body().as(LoginResponse.class);

        PostUpdateEventRequest requestBody = new PostEventDataFactory(createBodyForPostEvent())
                .setTitle("Dallas Mavericks - LA Clippers")
                .setImage("https://s.hdnux.com/photos/01/37/20/53/24972014/3/960x0.webp")
                .setDate("")
                .setLocation("American Airlines Center, Dallas, Texas, USA")
                .setDescription("Expect high-energy play and intense competition in this exciting basketball showdown.")
                .createRequest();

        Response response = new EVPrimeClient()
                .postEvent(requestBody, loginResponseBody.getToken());

        ErrorResponse loginErrorResponse = response.body().as(ErrorResponse.class);

        assertEquals(422, response.statusCode());
        assertEquals("Adding the event failed due to validation errors.", loginErrorResponse.getMessage());
        assertEquals("Invalid date.", loginErrorResponse.getErrors().getDate());
    }

    @Test
    public void unsuccessfulPostEventInvalidLocation() {
        Response loginResponse = new EVPrimeClient()
                .login(signUpRequest);

        LoginResponse loginResponseBody = loginResponse.body().as(LoginResponse.class);

        PostUpdateEventRequest requestBody = new PostEventDataFactory(createBodyForPostEvent())
                .setTitle("Dallas Mavericks - LA Clippers")
                .setImage("https://s.hdnux.com/photos/01/37/20/53/24972014/3/960x0.webp")
                .setDate("05-03-2024")
                .setLocation("")
                .setDescription("Expect high-energy play and intense competition in this exciting basketball showdown.")
                .createRequest();

        Response response = new EVPrimeClient()
                .postEvent(requestBody, loginResponseBody.getToken());

        ErrorResponse loginErrorResponse = response.body().as(ErrorResponse.class);

        assertEquals(422, response.statusCode());
        assertEquals("Adding the event failed due to validation errors.", loginErrorResponse.getMessage());
        assertEquals("Invalid location.", loginErrorResponse.getErrors().getDescription());
    }

    @Test
    public void unsuccessfulPostInvalidDescription() {
        Response loginResponse = new EVPrimeClient()
                .login(signUpRequest);

        LoginResponse loginResponseBody = loginResponse.body().as(LoginResponse.class);

        PostUpdateEventRequest requestBody = new PostEventDataFactory(createBodyForPostEvent())
                .setTitle("Dallas Mavericks - LA Clippers")
                .setImage("https://s.hdnux.com/photos/01/37/20/53/24972014/3/960x0.webp")
                .setDate("05-03-2024")
                .setLocation("American Airlines Center, Dallas, Texas, USA")
                .setDescription("")
                .createRequest();

        Response response = new EVPrimeClient()
                .postEvent(requestBody, loginResponseBody.getToken());

        ErrorResponse loginErrorResponse = response.body().as(ErrorResponse.class);

        assertEquals(422, response.statusCode());
        assertEquals("Adding the event failed due to validation errors.", loginErrorResponse.getMessage());
        assertEquals("Invalid description.", loginErrorResponse.getErrors().getDescription());
    }

    @Test
    public void unsuccessfulNotAuthenticated() {
        Response loginResponse = new EVPrimeClient()
                .login(signUpRequest);

        LoginResponse loginResponseBody = loginResponse.body().as(LoginResponse.class);

        PostUpdateEventRequest requestBody = new PostEventDataFactory(createBodyForPostEvent())
                .setTitle("Dallas Mavericks - LA Clippers")
                .setImage("https://s.hdnux.com/photos/01/37/20/53/24972014/3/960x0.webp")
                .setDate("05-03-2024")
                .setLocation("American Airlines Center, Dallas, Texas, USA")
                .setDescription("Expect high-energy play and intense competition in this exciting basketball showdown.")
                .createRequest();

        Response response = new EVPrimeClient()
                .postEvent(requestBody);

        ErrorResponse loginErrorResponse = response.body().as(ErrorResponse.class);

        assertEquals(401, response.statusCode());
        assertEquals("Not authenticated.", loginErrorResponse.getMessage());
    }
}