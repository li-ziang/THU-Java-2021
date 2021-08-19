package turitorial.history;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HistoryController {
    @Autowired
    HistoryRepository historyRepository;
    @DeleteMapping("/histories/all")
    public String deleteHistories() {
        JSONObject jsonObject = new JSONObject();
        historyRepository.deleteAll();
        jsonObject.put("code","200");
        return jsonObject.toString();
    }
}
