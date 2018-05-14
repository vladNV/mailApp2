package sample.session;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sample.Main;
import sample.domain.Client;
import sample.repository.ClientRepository;

@Component(value = "client")
@Getter
@Setter()
@ToString(of = "username")
@EqualsAndHashCode(of = "id")
public class ClientDetails {
    @Autowired private ClientRepository clientRepository;

    private Long id;
    private String username;
    private String email;
    private String name;

    public void extractClient(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.email = client.getEmail();
        this.username = client.getLogin();
    }

    public Client buildClient() {
        ClientDetails currentUser = (ClientDetails) Main.scenes.get("client");
        return clientRepository.findById(currentUser.getId()).get();
    }
}
