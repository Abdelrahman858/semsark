package project.semsark.model.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    private String name;
    private String seller;
    private String buyer;
    private String email;
    private String phone;
    private String orderType;        // ايجار -بيع
    private String urlPhoto;
}
