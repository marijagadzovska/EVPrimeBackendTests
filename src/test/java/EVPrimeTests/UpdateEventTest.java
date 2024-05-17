package EVPrimeTests;

import client.EVPrimeClient;
import data.PostEventDataFactory;
import data.SignUpLoginDataFactory;
import database.DbClient;
import io.restassured.response.Response;
import models.request.PostUpdateEventRequest;
import models.request.SignUpLoginRequest;
import models.response.ErrorResponse;
import models.response.LoginResponse;
import models.response.PostUpdateDeleteEventResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static objectbuilder.PostEventObjectBuilder.createBodyForPostEvent;
import static objectbuilder.SignUpObjectBuilder.createBodyForSignUp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UpdateEventTest {
    DbClient dbClient = new DbClient();
    private static String id;
    private SignUpLoginRequest signUpRequest;
    private LoginResponse loginResponseBody;
    private PostUpdateEventRequest requestBody;
    private PostUpdateDeleteEventResponse postResponse;
    @Before
    public void setUp() {
        signUpRequest = new SignUpLoginDataFactory(createBodyForSignUp())
                .setEmail(RandomStringUtils.randomAlphanumeric(10) + "@mail.com")
                .setPassword(RandomStringUtils.randomAlphanumeric(10))
                .createRequest();

        new EVPrimeClient()
                .signUp(signUpRequest);

        Response loginResponse = new EVPrimeClient()
                .login(signUpRequest);

        loginResponseBody = loginResponse.body().as(LoginResponse.class);

        requestBody = new PostEventDataFactory(createBodyForPostEvent())
                .setTitle("Dallas Mavericks - LA Clippers")
                .setImage("https://s.hdnux.com/photos/01/37/20/53/24972014/3/960x0.webp")
                .setDate("05-03-2024")
                .setLocation("American Airlines Center, Dallas, Texas, USA")
                .setDescription("Expect high-energy play and intense competition in this exciting basketball showdown.")
                .createRequest();

        Response response = new EVPrimeClient()
                .postEvent(requestBody, loginResponseBody.getToken());

        postResponse = response.body().as(PostUpdateDeleteEventResponse.class);
        id = postResponse.getMessage().substring(39);
    }

    @Test
    public void updateEventTest() throws SQLException {
        requestBody.setDate("2024-04-09");

        Response updateResponse = new EVPrimeClient()
                .updateEvent(requestBody, loginResponseBody.getToken(), postResponse.getMessage().substring(39));

        PostUpdateDeleteEventResponse updateResponseBody = updateResponse.body().as(PostUpdateDeleteEventResponse.class);

        assertEquals(201, updateResponse.statusCode());
        assertTrue(updateResponseBody.getMessage().contains("Successfully updated the event with id: "));
        assertEquals(requestBody.getDate(), dbClient.getEventFromDB(postResponse.getMessage().substring(39)).getDate());
    }

    @Test
    public void unsuccessfulUpdateEventInvalidTitleTest(){
        requestBody.setTitle("");

        Response updateResponse = new EVPrimeClient()
                .updateEvent(requestBody, loginResponseBody.getToken(), postResponse.getMessage());

        ErrorResponse updateErrorResponse = updateResponse.body().as(ErrorResponse.class);

        assertEquals(422, updateResponse.statusCode());
        assertEquals("Updating the event failed due to validation errors.", updateErrorResponse.getMessage());
        assertEquals("Invalid title.", updateErrorResponse.getErrors().getTitle());
    }

    @Test
    public void unsuccessfulUpdateEventEmptyImageTest(){
        requestBody.setImage("");

        Response updateResponse = new EVPrimeClient()
                .updateEvent(requestBody, loginResponseBody.getToken(), postResponse.getMessage());

        ErrorResponse updateErrorResponse = updateResponse.body().as(ErrorResponse.class);

        assertEquals(422, updateResponse.statusCode());
        assertEquals("Updating the event failed due to validation errors.", updateErrorResponse.getMessage());
        assertEquals("Invalid image.", updateErrorResponse.getErrors().getImage());
    }

    @Test
    public void unsuccessfulUpdateEventInvalidImageTest(){
        requestBody.setImage("image");

        Response updateResponse = new EVPrimeClient()
                .updateEvent(requestBody, loginResponseBody.getToken(), postResponse.getMessage());

        ErrorResponse updateErrorResponse = updateResponse.body().as(ErrorResponse.class);

        assertEquals(422, updateResponse.statusCode());
        assertEquals("Updating the event failed due to validation errors.", updateErrorResponse.getMessage());
        assertEquals("Invalid image.", updateErrorResponse.getErrors().getImage());
    }

    @Test
    public void unsuccessfulUpdateEventEmptyDateTest(){
        requestBody.setDate("");

        Response updateResponse = new EVPrimeClient()
                .updateEvent(requestBody, loginResponseBody.getToken(), postResponse.getMessage());

        ErrorResponse updateErrorResponse = updateResponse.body().as(ErrorResponse.class);

        assertEquals(422, updateResponse.statusCode());
        assertEquals("Updating the event failed due to validation errors.", updateErrorResponse.getMessage());
        assertEquals("Invalid date.", updateErrorResponse.getErrors().getDate());
    }

   @Test
   public void unsuccessfulUpdateEventEmptyLocationTest(){
       requestBody.setLocation("");

       Response updateResponse = new EVPrimeClient()
               .updateEvent(requestBody, loginResponseBody.getToken(), postResponse.getMessage());

       ErrorResponse updateErrorResponse = updateResponse.body().as(ErrorResponse.class);

       assertEquals(422, updateResponse.statusCode());
       assertEquals("Updating the event failed due to validation errors.", updateErrorResponse.getMessage());
       assertEquals("Invalid location.", updateErrorResponse.getErrors().getDescription());
   }

    @Test
    public void unsuccessfulUpdateEventEmptyDescriptionTest(){
        requestBody.setDescription("");

        Response updateResponse = new EVPrimeClient()
                .updateEvent(requestBody, loginResponseBody.getToken(), postResponse.getMessage());

        ErrorResponse updateErrorResponse = updateResponse.body().as(ErrorResponse.class);

        assertEquals(422, updateResponse.statusCode());
        assertEquals("Updating the event failed due to validation errors.", updateErrorResponse.getMessage());
        assertEquals("Invalid description.", updateErrorResponse.getErrors().getDescription());
    }

    @Test
    public void unsuccessfulUpdateNoAuthorizationTest(){
        Response updateResponse = new EVPrimeClient()
                .updateEvent(requestBody, postResponse.getMessage());

        ErrorResponse updateErrorResponse = updateResponse.body().as(ErrorResponse.class);

        assertEquals(401, updateResponse.statusCode());
        assertEquals("Not authenticated.", updateErrorResponse.getMessage());
    }
}
