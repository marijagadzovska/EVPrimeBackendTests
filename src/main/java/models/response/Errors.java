package models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Errors {
    String email;
    String password;
    String credentials;
    String title;
    String image;
    String date;
    String description;
}
