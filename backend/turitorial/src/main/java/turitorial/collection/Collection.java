package turitorial.collection;

import turitorial.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "collections")
public class Collection {
    private @Id @GeneratedValue long id;
    private @NotBlank String instanceName;
    @ManyToOne(fetch = FetchType.LAZY)
    User user;
    public Collection(){}
    public Collection(String instanceName, User user) {
        this.instanceName = instanceName;
        this.user = user;
    }
    public String getInstanceName() {
        return this.instanceName;
    }
}
