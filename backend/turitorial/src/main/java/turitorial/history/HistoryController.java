package turitorial.history;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import turitorial.user.User;
import turitorial.user.UserController;
import turitorial.user.UserRepository;

import java.util.List;

@RestController
public class HistoryController {
    @Autowired
    HistoryRepository historyRepository;
//    @Autowired
//    UserRepository userRepository;

    @DeleteMapping("/histories/all")
    public String deleteHistories() {
        JSONObject jsonObject = new JSONObject();
        historyRepository.deleteAll();
        jsonObject.put("code","200");
        return jsonObject.toString();
    }
}
