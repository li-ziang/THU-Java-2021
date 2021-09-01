package turitorial.history;

import turitorial.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "histories")
public class History {
    private @NotBlank String instanceName;
    private @NotBlank String time;
    private @NotBlank String course;
    private @Id @GeneratedValue long id;
    @ManyToOne(fetch = FetchType.LAZY) User user;
    public History() {}
    public History(String instanceName, String time, User user, String course) {
        this.time = time;
        this.instanceName = instanceName;
        this.user = user;
        this.course = course;
    }
    public String getCourse() {
        return course;
    }
    public String getInstanceName() {
        return instanceName;
    }
    public String getTime() {
        return time;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof History)) return false;
        History history = (History) o;
        return ((this.instanceName.equals(history.instanceName)) && (this.instanceName.equals(history.instanceName)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceName, time);
    }

}
