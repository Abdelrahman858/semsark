package project.semsark.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Email;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailsDto {

    private String username;
    private Long profileId;
    private String phone;
    @Email
    private String email;
    private boolean social = false;
    private String img;


}
