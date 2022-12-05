package project.semsark.model.request_body;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Email;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdate {

    private String username;
    private String password;
    private Long profileId;
    private String phone;
    @Email
    private String oldEmail;
    private String newEmail;
    private String img;
}
