package project.semsark.model.requestBody;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContactReq {
    private String firstName;
    private String secondName;
    private String subject;
    private String email;
    private String message;
}
