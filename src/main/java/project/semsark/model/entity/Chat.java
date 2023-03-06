package project.semsark.model.entity;

import lombok.*;
import javax.persistence.*;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    private String senderEmail;
    private String receiverEmail;
    private String message;
    private Date date;
    private boolean status;
}
