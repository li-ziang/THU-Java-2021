package turitorial.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import org.json .*;
import turitorial.history.History;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json .*;
import turitorial.dataloader.HttpRequest;
import turitorial.history.History;
import turitorial.history.HistoryRepository;
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
            if (user.equals(newUser)) {
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
            if (other.equals(user)) {
                user.setLoggedIn(true);
                userRepository.save(user);
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
                user.setLoggedIn(false);
                userRepository.save(user);
                jsonObject.put("code", "200");
                jsonObject.put("content", "logout successfully");
                return jsonObject.toString();
            }
        }
        jsonObject.put("code","400");
        jsonObject.put("content", "did not login");
        return  jsonObject.toString();
    }
    @PostMapping("/users/histories")
    public List<String> getHistories(@Valid @RequestBody User user) {
        List<User> users = userRepository.findAll();
        for(User other: users) {
            if(other.equals(user)) {
                List<String> ret = new ArrayList<String>();
                List<History> histories = other.getHistories();
                for(History history:histories) {
                    ret.add(history.getInstanceName());
                }
                return ret;
            }
        }
        return null;
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

    @PostMapping("/users/search")
    public String searchInstance(@Valid @RequestBody SearchKey searchKey) {
        String username = searchKey.username, keyword = searchKey.keyword, subject = searchKey.subject;
        List<User> users = userRepository.findAll();
        for(User user: users) {
            if(user.getUsername().equals(username)) {
                // TODO: ADD keyword to user's keyword form;
            }
        }
        String string = HttpRequest.sendGet("http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList",
                "course=" + subject + "&searchKey=" + keyword + "&id=9fd824ec-146b-413c-bc16-56710d654f26");
        return string;
    }

    @PostMapping("/users/instance")
    public String getInstance(@Valid @RequestBody InstanceInfo instanceInfo ) { // course参数可以为空字符串
        String course = instanceInfo.course, instanceName = new String(instanceInfo.instanceName.getBytes(StandardCharsets.UTF_8)), username = instanceInfo.username;
        String string = HttpRequest.sendGet("http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName",
                "course=" + course + "&name=" + instanceName + "&id=9fd824ec-146b-413c-bc16-56710d654f26");
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

}