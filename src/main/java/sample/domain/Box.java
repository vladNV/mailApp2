package sample.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "box")
@EqualsAndHashCode(of = "id")
@ToString(of = {"from", "to"})
public class Box {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email_from")
    private String from;

    @Column(name = "email_to")
    private String to;

    private LocalDateTime sentDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mail_id")
    Mail mail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    Client client;

}
