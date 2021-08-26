package turitorial.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json .*;
import turitorial.dataloader.HttpRequest;
import turitorial.history.History;
import turitorial.history.HistoryRepository;
import turitorial.searchKeyHis.SearchKeyHis;

class His {
    public String username;
    public String instanceName;
    public His(){}
    public His(String username, String instanceName) {
        this.username = username;
        this.instanceName = instanceName;
    }
};
class SearchKey {
    public String username;
    public String keyword;
    public String subject;
    public SearchKey(){}
    public SearchKey(String username, String keyword, String subject) {
        this.keyword = keyword;
        this.username = username;
        this.subject = subject;
    }
}

class InstanceInfo{
    public String instanceName;
    public String course;
    public String username;
    public InstanceInfo(){}
    public InstanceInfo(String instanceName, String course, String username) {
        this.course = course;
        this.instanceName = instanceName;
        this.username = username;
    }
}

class userHistory {
    public String username;
    public Integer number;
    public userHistory(String username, Integer number) {
        this.number = number;
        this.username = username;
    }
}

class userSearchKey {
    public Integer number;
    public String username;
    public userSearchKey(Integer number, String username) {
        this.number = number;
        this.username = username;
    }
}

class LinkInstance {
    public String context;
    public String course;
    public LinkInstance(String context, String course) {
        this.context = context;
        this.course = course;
    }
}
class Question {
    public String course;
    public String inputQuestion;
    public Question(String course, String inputQuestion) {
        this.course = course;
        this.inputQuestion = inputQuestion;
    }
}

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    HistoryRepository historyRepository;


    @PostMapping("/users/register")
    public String registerUser(@Valid @RequestBody User newUser) {
        JSONObject jsonObject = new JSONObject();
        List<User> users = userRepository.findAll();
        System.out.println("New user: " + newUser.toString());
        for (User user : users) {
            System.out.println("Registered user: " + newUser.toString());
            if (user.getUsername().equals(newUser)) {
                System.out.println("User Already exists!");
                jsonObject.put("code", "400");
                jsonObject.put("content", "user already exists");
                return jsonObject.toString();
            }
        }
        userRepository.save(newUser);
        jsonObject.put("code", "200");
        jsonObject.put("content", "registered successfully");
        return jsonObject.toString();
    }
    @PostMapping("/users/login")
    public String loginUser(@Valid @RequestBody User user) {
        JSONObject jsonObject = new JSONObject();
        List<User> users = userRepository.findAll();
        for (User other : users) {
            if (other.getUsername().equals(user.getUsername())) {
                other.setLoggedIn(true);
                userRepository.save(other);
                jsonObject.put("code", "200");
                jsonObject.put("content", "login successfully");
                return jsonObject.toString();
            }
        }
        jsonObject.put("code","400");
        jsonObject.put("content", "login failed");
        return jsonObject.toString();
    }
    @PostMapping("/users/logout")
    public String logUserOut(@Valid @RequestBody User user) {
        JSONObject jsonObject = new JSONObject();
        List<User> users = userRepository.findAll();
        for (User other : users) {
            if (other.equals(user)) {
                other.setLoggedIn(false);
                userRepository.save(other);
                jsonObject.put("code", "200");
                jsonObject.put("content", "logout successfully");
                return jsonObject.toString();
            }
        }
        jsonObject.put("code","400");
        jsonObject.put("content", "did not login");
        return  jsonObject.toString();
    }
    @PostMapping("/search/history")
    public String getHistories(@Valid @RequestBody userHistory userHistory) {
        List<User> users = userRepository.findAll();
        String username = userHistory.username;
        Integer number = userHistory.number;
        Integer tot_num = 0;

//        List<String> ret = new ArrayList<String>();
        System.out.println(number);
        JSONArray retArray = new JSONArray();
        for(User other: users) {
            if(other.getUsername().equals(username)) {
                System.out.println("found");
                List<History> histories = other.getHistories();
                for(History history:histories) {
                    tot_num ++;
                    if(tot_num == number) break;
                    JSONObject temp = new JSONObject();
                    temp.put("history", history.getInstanceName());
                    temp.put("time", history.getTime());
                    retArray.put(temp);
                }
                return retArray.toString();
            }
        }
        return retArray.toString();
    }

    @PostMapping("/search/searchkey")
    public List<String> getSearchkeyHis(@Valid @RequestBody userSearchKey usersearchKey) {
        List<User> users = userRepository.findAll();
        String username = usersearchKey.username;
        Integer number = usersearchKey.number;
        Integer tot_num = 0;
        System.out.println(number);
        List<String> ret = new ArrayList<String>();
        for(User other: users) {
            if(other.getUsername().equals(username)) {
                System.out.println("found");
                List<SearchKeyHis> searchKeyHis = other.getSearchKeyHistories();
                for(SearchKeyHis his:searchKeyHis) {
                    tot_num ++;
                    if(tot_num == number) break;
                    ret.add(his.getSearchKey());
                }
                return ret;
            }
        }
        return ret;

    }



    @PostMapping("/users/addhistory")
    public String addHistory(@Valid @RequestBody His his) {
        String username = his.username, instanceName = his.instanceName;
        List<User> users = userRepository.findAll();
        for(User temp_user: users) {
            if(username.equals(temp_user.getUsername())) {
                Date date = new Date();
                History history = new History(instanceName, date.toString(), temp_user);
                temp_user.histories.add(history);
                User temp = userRepository.save(temp_user);
                System.out.println(temp.histories);
                return "Success";
            }
        }
        return "failure";
    }

    public void addSearchKey(String searchKey, User user) {
        SearchKeyHis s = new SearchKeyHis(searchKey, user);
        System.out.println(user.getId());
        user.searchKeyHistories.add(s);
        userRepository.save(user);
    }

    @PostMapping("/users/search")
    public String searchInstance(@Valid @RequestBody SearchKey searchKey) {
        String username = searchKey.username, keyword = searchKey.keyword, subject = searchKey.subject;
        List<User> users = userRepository.findAll();
        for(User user: users) {
            if(user.getUsername().equals(username)) {
                addSearchKey(keyword, user);
            }
        }
        String id = apiLogin();
        System.out.println(id);
        String string = HttpRequest.sendGet("http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList",
                "course=" + subject + "&searchKey=" + keyword + "&id=" + id);
        JSONObject json = new JSONObject(string);
        JSONArray data = json.getJSONArray("data");
        JSONArray retArray = new JSONArray();
        for(int i = 0; i < data.length(); i++) {
            JSONObject obj = data.getJSONObject(i);
            String label = obj.getString("label");
            String category = obj.getString("category");
            JSONObject temp = new JSONObject();
            temp.put("label", label);
            temp.put("category", category);
            retArray.put(temp);
        }
        return retArray.toString();
    }

    @PostMapping("/search/info")
    public String getInstance(@Valid @RequestBody InstanceInfo instanceInfo ) {
        String course = instanceInfo.course, instanceName = new String(instanceInfo.instanceName.getBytes(StandardCharsets.UTF_8)), username = instanceInfo.username;
        String id = apiLogin();
        String string = HttpRequest.sendGet("http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName",
                "course=" + course + "&name=" + instanceName + "&id=" + id);
        His his = new His(username, instanceName);
        addHistory(his);
        return string;
    }

    @DeleteMapping("/users/all")
    public String deleteUsers() {
        JSONObject jsonObject = new JSONObject();
        userRepository.deleteAll();
        jsonObject.put("code","200");
        return jsonObject.toString();
    }

    public String apiLogin() {
        String string = HttpRequest.sendPost("http://open.edukg.cn/opedukg/api/typeAuth/user/login",
                "password=thueda2019&phone=18201616030");
        JSONObject jsonObject = new JSONObject(string);
        System.out.println(jsonObject.getString("id"));
        return jsonObject.getString("id");
    }

    @PostMapping("/users/linkedInstances")
    public String getLinkedInstances(@Valid @RequestBody LinkInstance linkInstance) {
        String context = linkInstance.context, course = linkInstance.course;
        context = context.replaceAll(" ", "_");
        String id = apiLogin();
        String string = HttpRequest.sendPost("http://open.edukg.cn/opedukg/api/typeOpen/open/linkInstance",
                "context=" + context + "&course=" + course + "&id=" + id);
        System.out.println(string);
        JSONObject json = new JSONObject(string);
        JSONObject data = json.getJSONObject("data");
        JSONArray array = data.getJSONArray("results");
        JSONArray retArray = new JSONArray();
        for(int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String entity_type = obj.getString("entity_type");
            Integer start_index = obj.getInt("start_index");
            Integer end_index = obj.getInt("end_index");
            String entity = obj.getString("entity");
            JSONObject temp = new JSONObject();
            temp.put("entity_type", entity_type);
            temp.put("entity", entity);
            temp.put("end_index", end_index);
            temp.put("start_index", start_index);
            retArray.put(temp);
        }
        return retArray.toString();
    }
    @PostMapping("/users/Question")
    public String ansQuestion(@Valid @RequestBody Question question) {
        String inputQuestion = question.inputQuestion, course = question.course;
        String id = apiLogin();
        String string = HttpRequest.sendPost("http://open.edukg.cn/opedukg/api/typeOpen/open/inputQuestion",
                "course=" + course + "&inputQuestion=" + inputQuestion + "&id=" + id);
        JSONObject json = new JSONObject(string);
        JSONArray data = json.getJSONArray("data");
        JSONArray retArray = new JSONArray();
        for(int i = 0; i < data.length(); i++) {
            JSONObject obj = data.getJSONObject(i);
            String all = obj.getString("all");
            Integer score = obj.getInt("score");
            String subject = obj.getString("subject");
            String value = obj.getString("value");
//            String message = obj.has("message") ? obj.getString("message") : null; // 没找到的时候，显示此问题没找到答案。
            JSONObject temp = new JSONObject();
            temp.put("all", all);
            temp.put("score", score);
            temp.put("subject", subject);
            temp.put("value", value);
//            temp.put("message", message);
            retArray.put(temp);
        }
        return retArray.toString();
    }

}