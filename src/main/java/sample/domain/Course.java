package sample.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String classNumber;
    private Long during;
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    Client client;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    Set<Organization> organizations = new HashSet<>();

    @Override
    public String toString() {
        return  name + " : " + classNumber;
    }
}
