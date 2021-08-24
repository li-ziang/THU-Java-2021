package turitorial.searchKeyHis;


import turitorial.history.History;
import turitorial.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name="searchKeyHistories")
public class SearchKeyHis {
    private @Id @GeneratedValue long id;
    private @NotBlank String searchKey;
    @ManyToOne(fetch = FetchType.LAZY)
    User user;
    public SearchKeyHis() {}
    public SearchKeyHis(String searchKey, User user) {
        this.searchKey = searchKey;
        this.user = user;
    }

    public String getSearchKey() {
        return this.searchKey;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof SearchKeyHis)) return false;
        SearchKeyHis searchKeyHis = (SearchKeyHis) o;
        return ((this.searchKey.equals(searchKeyHis.searchKey)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchKey);
    }
}
