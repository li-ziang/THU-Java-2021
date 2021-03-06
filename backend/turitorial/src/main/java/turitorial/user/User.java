package turitorial.user;

import turitorial.collection.Collection;
import turitorial.history.History;
import turitorial.searchKeyHis.SearchKeyHis;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    private @Id @GeneratedValue long id;
    private @NotBlank String username;
    public @NotBlank String password;
    private @NotBlank boolean loggedIn;
    @OneToMany(cascade = CascadeType.ALL) List<History> histories;
    @OneToMany(cascade = CascadeType.ALL) List<SearchKeyHis> searchKeyHistories;
    @OneToMany(cascade = CascadeType.ALL) List<Collection> collections;
    public User() {
    }
    public User(@NotBlank String username,
                @NotBlank String password) {
        this.username = username;
        this.password = password;
        this.loggedIn = false;
    }
    public List<Collection> getCollections() { return collections;}

    public List<History> getHistories() {
        return histories;
    }
    public List<SearchKeyHis> getSearchKeyHistories() {return  searchKeyHistories;}

    public long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isLoggedIn() {
        return loggedIn;
    }
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, username, password,
                loggedIn);
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", loggedIn=" + loggedIn +
                '}';
    }
}