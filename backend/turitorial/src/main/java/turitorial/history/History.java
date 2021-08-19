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
    private @Id @GeneratedValue long id;
    @ManyToOne(fetch = FetchType.LAZY) User user;
    History() {}
    History(String instanceName, String time) {
        this.time = time;
        this.instanceName = instanceName;
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
    @Override
    public String toString() {
        return "";
    }

}
