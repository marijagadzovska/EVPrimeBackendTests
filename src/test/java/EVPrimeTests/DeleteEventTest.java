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
import static org.junit.Assert.assertFalse;

public class DeleteEventTest {

    DbClient dbClient = new DbClient();
    private SignUpLoginRequest signUpRequest;
    private LoginResponse loginResponseBody;
    private PostUpdateEventRequest requestBody;
    String id;

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
                .setDate("05-03-2026")
                .setLocation("American Airlines Center, Dallas, Texas, USA")
                .setDescription("Expect high-energy play and intense competition in this exciting basketball showdown.")
                .createRequest();

        Response response = new EVPrimeClient()
                .postEvent(requestBody, loginResponseBody.getToken());

        PostUpdateDeleteEventResponse postResponse = response.body().as(PostUpdateDeleteEventResponse.class);

        id = postResponse.getMessage().substring(39);
    }

    @Test
    public void deleteEventTest() throws SQLException {
        Response responseDelete = new EVPrimeClient().deleteEvent(loginResponseBody.getToken(), id);

        PostUpdateDeleteEventResponse deleteResponseBody = responseDelete.body().as(PostUpdateDeleteEventResponse.class);

        assertEquals(200, responseDelete.statusCode());
        assertEquals("Successfully deleted the event with id: " + id, deleteResponseBody.getMessage());
        assertFalse(dbClient.isEventDeletedFromDb(id));
    }

    @Test
    public void unsuccessfulDeleteAnEvent(){
        Response responseDelete = new EVPrimeClient().deleteEvent(id);

        ErrorResponse deleteResponseBody = responseDelete.body().as(ErrorResponse.class);

        assertEquals(401, responseDelete.statusCode());
        assertEquals("Not authenticated.",deleteResponseBody.getMessage());
    }
}




