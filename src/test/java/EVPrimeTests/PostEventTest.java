package EVPrimeTests;

import client.EVPrimeClient;
import data.PostEventDataFactory;
import data.SignUpLoginDataFactory;
import database.DbClient;
import io.restassured.response.Response;
import models.request.PostUpdateEventRequest;
import models.request.SignUpLoginRequest;
import models.response.LoginResponse;
import models.response.PostUpdateDeleteEventResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.sql.SQLException;

import static objectbuilder.PostEventObjectBuilder.createBodyForPostEvent;
import static objectbuilder.SignUpObjectBuilder.createBodyForSignUp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PostEventTest {
    DbClient dbClient = new DbClient();

    @Test
    public void successfulPostEventTest() throws SQLException {
        SignUpLoginRequest signUpRequest = new SignUpLoginDataFactory(createBodyForSignUp())
                .setEmail(RandomStringUtils.randomAlphabetic(10) + "@mail.com")
                .setPassword(RandomStringUtils.randomAlphabetic(10))
                .createRequest();

        new EVPrimeClient()
                .signUp(signUpRequest);

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
                .postEvent(requestBody, loginResponseBody.getToken());

        PostUpdateDeleteEventResponse postResponse = response.body().as(PostUpdateDeleteEventResponse.class);

        String id = postResponse.getMessage().substring(39);

        PostUpdateEventRequest eventFromDB = dbClient.getEventFromDB(id);

        assertEquals(201, response.statusCode());
        assertTrue(postResponse.getMessage().contains("Successfully created an event with id: "));
        assertEquals(requestBody.getTitle(), dbClient.getEventFromDB(postResponse.getMessage().substring(39)).getTitle());
        assertEquals(requestBody.getImage(), dbClient.getEventFromDB(postResponse.getMessage().substring(39)).getImage());
        assertEquals(requestBody.getDate(), dbClient.getEventFromDB(postResponse.getMessage().substring(39)).getDate());
        assertEquals(requestBody.getLocation(), dbClient.getEventFromDB(postResponse.getMessage().substring(39)).getLocation());
        assertEquals(requestBody.getDescription(), dbClient.getEventFromDB(postResponse.getMessage().substring(39)).getDescription());
    }
}



