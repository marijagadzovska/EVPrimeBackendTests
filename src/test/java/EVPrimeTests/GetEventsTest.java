package EVPrimeTests;

import client.EVPrimeClient;
import data.PostEventDataFactory;
import data.SignUpLoginDataFactory;
import io.restassured.response.Response;
import models.request.PostUpdateEventRequest;
import models.request.SignUpLoginRequest;
import models.response.GetEventsResponse;
import models.response.LoginResponse;
import models.response.PostUpdateDeleteEventResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static objectbuilder.PostEventObjectBuilder.createBodyForPostEvent;
import static objectbuilder.SignUpObjectBuilder.createBodyForSignUp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GetEventsTest {

    @Test
    public void gelAllEventsTest(){
        Response response = new EVPrimeClient()
                .getAllEvents();

        GetEventsResponse responseBody = response.body().as(GetEventsResponse.class);

        assertEquals(200,response.getStatusCode());
        assertFalse(responseBody.getEvents().isEmpty());
    }

    @Test
    public void getEventByIdTest(){
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

        System.out.println(postResponse.getMessage());

        String id = postResponse.getMessage().substring(39);

        Response getEventResponse = new EVPrimeClient()
                .getEventById(id);

        GetEventsResponse responseBody = getEventResponse.body().as(GetEventsResponse.class);

        assertEquals(200,getEventResponse.getStatusCode());
        assertEquals(requestBody.getTitle(), responseBody.getEvents().get(0).getTitle());
        assertEquals(requestBody.getDate(), responseBody.getEvents().get(0).getDate());
        assertEquals(requestBody.getImage(), responseBody.getEvents().get(0).getImage());
        assertEquals(requestBody.getDescription(), responseBody.getEvents().get(0).getDescription());
        assertEquals(requestBody.getLocation(), responseBody.getEvents().get(0).getLocation());
    }

}



