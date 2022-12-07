package project.semsark.model.entity;

import lombok.*;
import project.semsark.model.enums.TypesOfBuilding;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long userId;
    @OneToMany(cascade = CascadeType.ALL)
    List<Photos> photosList = new ArrayList<>();
    private String signalPower;
    private String title;
    private String category;
    private String address;
    private String  des;
    private String apartmentDetails;
    private String  types;

    private Double price;

    private Double lang;
    private Double lat;
    private long area;
    private long numOfRoom;
    private long numOfBathroom;
    private long numOfHalls;
    private long level;
    private boolean finished;
    private boolean single;

}
