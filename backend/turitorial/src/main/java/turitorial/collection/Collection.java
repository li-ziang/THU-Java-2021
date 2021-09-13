package turitorial.collection;

import turitorial.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "collections")
public class Collection {
    private @Id @GeneratedValue long id;
    private @NotBlank String instanceName;
    private @NotBlank String course;
    @ManyToOne(fetch = FetchType.EAGER)
    User user;
    public Collection(){}
    public Collection(String instanceName, String course, User user) {
        this.instanceName = instanceName;
        this.user = user;
        this.course = course;
    }
    public String getInstanceName() {
        return this.instanceName;
    }
    public Long getId() {
        return this.id;
    }
    public String getCourse() {
        return this.course;
    }
}
