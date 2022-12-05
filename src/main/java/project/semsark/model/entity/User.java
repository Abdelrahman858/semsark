package project.semsark.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_details")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    private String username;
    private String email;
    private String password;
    private String phone = "00000000000";
    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
    private boolean verify = false;
    private boolean suspended = false;
    private String img;

}
