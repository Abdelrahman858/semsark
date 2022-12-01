package project.semsark.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private long id;
    private String name;
    private List<RoleDTO> roles;
}
