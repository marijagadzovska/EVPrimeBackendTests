package objectbuilder;

import models.request.PostUpdateEventRequest;

public class PostEventObjectBuilder {
    public static PostUpdateEventRequest createBodyForPostEvent(){
        return PostUpdateEventRequest.builder()
                .title("default title")
                .image("http image")
                .date("07.11.2023")
                .description("default description")
                .location("default location")
                .build();
    }
}
