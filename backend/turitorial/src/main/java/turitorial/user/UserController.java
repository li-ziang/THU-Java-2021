package turitorial.user;

import com.alibaba.fastjson.JSON;
import io.swagger.v3.core.util.Json;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
//import fastjson;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import org.json .*;
import turitorial.collection.Collection;
import turitorial.collection.CollectionRepository;
import turitorial.dataloader.HttpRequest;
import turitorial.history.History;
import turitorial.history.HistoryRepository;
import turitorial.searchKeyHis.SearchKeyHis;

class His {
    public String username;
    public String instanceName;
    public String course;
    public His(){}
    public His(String username, String instanceName, String course) {
        this.username = username;
        this.instanceName = instanceName;
        this.course = course;
    }
};
class SearchKey {
    public String username;
    public String keyword;
    public String course;
    public SearchKey(){}
    public SearchKey(String username, String keyword, String course) {
        this.keyword = keyword;
        this.username = username;
        this.course = course;
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

class  AddCollection{
    public String username;
    public String instanceName;
    public String course;
    public AddCollection(String username, String course, String instanceName) {
        this.instanceName = instanceName;
        this.course = course;
        this.username = username;
    }
}

class GetExercise {
    public String instanceName;
    public String username;
    public GetExercise(String instanceName, String username) {
        this.instanceName = instanceName;
        this.username = username;
    }
}

class NewPasswd{
    public String username;
    public String oldPassword;
    public String newPassword;
    public NewPasswd(String username, String oldPassword, String newPassword) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}

@RestController
public class UserController {
    String apiId = "964628b3-12ff-4ccb-a0f7-a30ea2653a30";
    @Autowired
    UserRepository userRepository;
    @Autowired
    HistoryRepository historyRepository;
    @Autowired
    CollectionRepository collectionRepository;

    @PostMapping("/users/register")
    public String registerUser(@Valid @RequestBody User newUser) { // 注册
        JSONObject jsonObject = new JSONObject();
        List<User> users = userRepository.findAll();
        System.out.println("New user: " + newUser.toString());
        for (User user : users) {
            System.out.println("Registered user: " + newUser.toString());
            if (user.getUsername().equals(newUser.getUsername())) {
                System.out.println("User Already exists!");
                jsonObject.put("code", "400");
                jsonObject.put("content", "user already exists");
                return jsonObject.toString();
            }
        }
        if(newUser.getUsername() == "") {
            return "Username can not be empty";
        }
        userRepository.save(newUser);
        jsonObject.put("code", "200");
        jsonObject.put("content", "registered successfully");
        return jsonObject.toString();
    }
    @PostMapping("/users/login")
    public String loginUser(@Valid @RequestBody User user) { // 登   录
        JSONObject jsonObject = new JSONObject();
        List<User> users = userRepository.findAll();
        for (User other : users) {
            if (other.getUsername().equals(user.getUsername()) && other.getPassword().equals(user.getPassword())) {
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
    public String logUserOut(@Valid @RequestBody User user) { // 登出
        JSONObject jsonObject = new JSONObject();
        List<User> users = userRepository.findAll();
        for (User other : users) {
            if (other.getUsername().equals(user.getUsername())) {
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

    @PostMapping("/users/changePassword")
    public String changePassword(@Valid @RequestBody NewPasswd newPasswd) {
        String username = newPasswd.username, oldPassword = newPasswd.oldPassword, newPassword = newPasswd.newPassword;
        List<User> users = userRepository.findAll();
        for(User temp_user: users) {
            if(temp_user.getUsername().equals(username)) {
                if(temp_user.getPassword().equals(oldPassword) && temp_user.isLoggedIn()) {
                    temp_user.password = newPassword;
                    userRepository.save(temp_user);
                    return "Success";
                }
            }
        }
        return "failure";
    }


    @PostMapping("/search/history")
    public String getHistories(@Valid @RequestBody userHistory userHistory) { // 获取某一个用户的历史记录
        List<User> users = userRepository.findAll();
        String username = userHistory.username;
        Integer number = userHistory.number;
        Integer tot_num = 0;

        System.out.println(number);
        JSONArray retArray = new JSONArray();
        for(User other: users) {
            if(other.getUsername().equals(username)) {
                System.out.println("found");
                List<History> histories = other.getHistories();
                for(int i = histories.size() - 1; i >= 0; i--) {
                    History history = histories.get(i);
                    if(history.getInstanceName().equals("")) {
//                        other.histories.remove(i);
                        continue;
                    }
                    tot_num ++;
                    JSONObject temp = new JSONObject();
                    temp.put("history", history.getInstanceName());
                    temp.put("time", history.getTime());
                    temp.put("course", history.getCourse());
                    retArray.put(temp);
                    if(tot_num == number) return retArray.toString();
                }
                return retArray.toString();
            }
        }
        return retArray.toString();
    }

    @PostMapping("/search/searchkey")
    public List<String> getSearchkeyHis(@Valid @RequestBody userSearchKey usersearchKey) { // 获取某一用户的搜索关键词记录
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
                for(int i = searchKeyHis.size() - 1; i >= 0; i-- ) {
                    SearchKeyHis his = searchKeyHis.get(i);
                    tot_num ++;
                    ret.add(his.getSearchKey());
                    if(tot_num >= number) {
                        System.out.println(ret.size());
                        System.out.println("number1111");
                        return ret;
                    }
                }
                System.out.println("number 22222");
                System.out.println(ret.size());
                return ret;
            }
        }
        System.out.println("number3333");
        System.out.println(ret.size());
        return ret;

    }



    @PostMapping("/users/addhistory")
    public String addHistory(@Valid @RequestBody His his) { // 为用户添加历史记录
        String username = his.username, instanceName = his.instanceName, course = his.course;
        List<User> users = userRepository.findAll();
        for(User temp_user: users) {
            if(username.equals(temp_user.getUsername()) && temp_user.isLoggedIn()) {
                Date date = new Date();
                History history = new History(instanceName, date.toString(), temp_user, course);
                temp_user.histories.add(history);
                User temp = userRepository.save(temp_user);
                System.out.println("add history");
                return "Success";
            }
        }
        return "No such user";
    }

    public void addSearchKey(String searchKey, User usersadas) {
        String username = usersadas.getUsername();
        if(searchKey.equals("")) return;
        System.out.println(usersadas.getId());
        List<User> users = userRepository.findAll();
        for(User temp_user: users) {
            if(username.equals(temp_user.getUsername())) {
                for(int i = temp_user.searchKeyHistories.size() - 1; i >= 0; i--) {
                    if(temp_user.searchKeyHistories.get(i).getSearchKey().equals(searchKey) || temp_user.searchKeyHistories.get(i).getSearchKey().equals("")) {
                        temp_user.searchKeyHistories.remove(i);
                    }
                }
                SearchKeyHis s = new SearchKeyHis(searchKey, temp_user);
                temp_user.searchKeyHistories.add(s);
                userRepository.save(temp_user);
//                System.out.println(temp_user.searchKeyHistories);
            }
        }

//        for(int i = 0; i < user.searchKeyHistories.size(); i++) {
//            SearchKeyHis sh = user.searchKeyHistories.get(i);
//            if(sh.getSearchKey().equals(searchKey)) {
//                System.out.println("remove");
//                user.searchKeyHistories.remove(i);
//            }
//        }
//        System.out.println(s.getSearchKey());
//        user.searchKeyHistories.add(s);

    }

    public Boolean judge(String message) {
        JSONObject json = new JSONObject(message);
        System.out.println(message);
        if(!json.has("data")) return  false;
        if(json.has("status") && json.getInt("status") == 500) {
            return false;
        }
        if(!json.has("code")) return false;
        if(json.has("data") && (json.get("data") instanceof String) && (json.getString("data").equals("") || json.getString("data").equals("null"))) {
            System.out.println(" data testing");
            return false;
        }
        if(json.has("code") && !json.getString("code").equals("0")) {
            System.out.println(json.getString("code"));
            System.out.println("code testing");
            return false;
        }
        return true;
    }

    @PostMapping("/users/search")
    public String searchInstance(@Valid @RequestBody SearchKey searchKey) { // 实体搜索接口
        String username = searchKey.username, keyword = searchKey.keyword, subject = searchKey.course;
        List<User> users = userRepository.findAll();
        for(User user: users) {
            if(user.getUsername().equals(username) && user.isLoggedIn()) {
                addSearchKey(keyword, user);
            }
        }
        String id = apiLogin();
        System.out.println(id);
        String string = null;
        try{
            string = HttpRequest.sendGet("http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList",
                    "course=" + subject + "&searchKey=" + URLEncoder.encode(keyword, "utf-8") + "&id=" + id);
        }
        catch (UnsupportedEncodingException e) {
            System.out.println(e);
        }
        System.out.println(string);
        if(!judge(string)) {
            return "failure";
        }

        JSONObject json = new JSONObject(string);
        if(!json.has("data")) {
            JSONArray ret = new JSONArray();
            return ret.toString();
        }
        JSONArray data = json.getJSONArray("data");
        JSONArray retArray = new JSONArray();
        HashMap<String, String> map = new HashMap<>();
        for(int i = 0; i < data.length(); i++) {
            JSONObject obj = data.getJSONObject(i);
            String label = obj.getString("label");
            String category = obj.getString("category");
            map.put(label, category);
            JSONObject temp = new JSONObject();
            temp.put("label", label);
            temp.put("category", category);
            retArray.put(temp);
        }


        return retArray.toString();
    }

    @PostMapping("/search/info")
    public String getInstance(@Valid @RequestBody InstanceInfo instanceInfo ) { // 获取实体详情
        String course = instanceInfo.course, instanceName = new String(instanceInfo.instanceName.getBytes(StandardCharsets.UTF_8)), username = instanceInfo.username;
        String id = apiLogin();
        String string = null;
        try {
            string = HttpRequest.sendGet("http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName",
                    "course=" + course + "&name=" + URLEncoder.encode(instanceName, "utf-8") + "&id=" + id);
        }
        catch(UnsupportedEncodingException e) {
            System.out.println(e);
        }
        System.out.println("search info");
        System.out.println(string);
        if(!judge(string)) {
            System.out.println("exam failed");
            return "failure";
        }
        System.out.println("search/info");

        System.out.println(string);


        JSONObject json = new JSONObject(string);
        if(!json.has("data")) {
            JSONArray ret = new JSONArray();
            return ret.toString();
        }

        JSONObject data = json.getJSONObject("data");
        if(data.has("uri") && !data.getString("uri").equals("")) {
            His his = new His(username, instanceName, course);
            addHistory(his);
        }
        JSONObject ret = new JSONObject(); // 最终的返回值，包含NamedIndividual， property和content
        ret.put("NamedIndividual", false); // 初始化为false
        JSONArray property = new JSONArray();
        JSONArray obj_content = new JSONArray(); // 所以的object实体
        JSONArray sub_content = new JSONArray(); // 所有的subject实体
        if(data.has("property")) {
            JSONArray temp_property = data.getJSONArray("property");
            for(int i = 0; i < temp_property.length(); i++) {
                JSONObject tpo = temp_property.getJSONObject(i);
                String object = tpo.getString("object");
                String entity_name = null;
                Boolean isEntity = false;
                if(object.contains("NamedIndividual")) {
                    ret.put("NamedIndividual", true); // 命名实体标识，记录后直接continue
                    continue;
                }
                if(object.contains("http://kb.cs.tsinghua.edu.cn")) {
                    continue; // 标识出处，不显示在前端
                }
                if(object.contains("http://edukg.org")) { // 为实体uri，需要转换成中文
                    String instanceFromUri = getInstanceFromUri(object, course);
                    JSONObject temp_obj = new JSONObject(instanceFromUri);
                    JSONObject temp_data;
                    if(temp_obj.has("data")) {
                        temp_data = temp_obj.getJSONObject("data");
                        if(temp_data.has("entity_name")) {
                            entity_name = temp_data.getString("entity_name");
                        }
                    }
                    else {
                        entity_name = "";
                    }
                    isEntity = true;
                }
                else {
                    entity_name = object; // 实体名为中文
                }
                String predicateLabel = tpo.getString("predicateLabel");
                JSONObject ret_obj = new JSONObject();
                ret_obj.put("predicateLabel", predicateLabel);
                ret_obj.put("label", entity_name);
                ret_obj.put("isEntity", isEntity);
                property.put(ret_obj);
            }
        }

        if(data.has("content")) {
            JSONArray temp_content = data.getJSONArray("content");
            for(int i = 0; i < temp_content.length(); i++) {
                JSONObject tpo = temp_content.getJSONObject(i);
                if(tpo.has("object_label")) {
                    String object_label = tpo.getString("object_label");
                    String predicate_label = tpo.getString("predicate_label");
                    JSONObject temp_obj = new JSONObject();
                    temp_obj.put("object_label", object_label);
                    temp_obj.put("predicate_label", predicate_label);
                    obj_content.put(temp_obj);
                }
                if(tpo.has("subject_label")) {
                    String subject_label = tpo.getString("subject_label");
                    String predicate_label = tpo.getString("predicate_label");
                    JSONObject temp_obj = new JSONObject();
                    temp_obj.put("predicate_label", predicate_label);
                    temp_obj.put("subject_label", subject_label);
                    sub_content.put(temp_obj);
                }
            }
        }
        ret.put("property",property );
        ret.put("obj_content", obj_content);
        ret.put("sub_content", sub_content);
        ret.put("isCollected", false);
        List<User> allUsers = userRepository.findAll();
        for(User temp_user: allUsers) {
            if(temp_user.getUsername().equals(username) && temp_user.isLoggedIn()) {
                List<Collection> collections = temp_user.collections;
                for(Collection temp_collection: collections) {
                    if(temp_collection.getInstanceName().equals(instanceName)) {
                        ret.put("isCollected", true);
                        break;
                    }
                }
                break;
            }
         }

        return ret.toString();
    }

    public String getInstanceFromUri(String uri, String course) {
        String id = apiLogin();
        String string = HttpRequest.sendPost("http://open.edukg.cn/opedukg/api/typeOpen/open/getKnowledgeCard",
                "course=" + course + "&uri=" + uri + "&id=" + id);

        System.out.println(string);
        return string;
    }

    @DeleteMapping("/users/all")
    public String deleteUsers() {
        JSONObject jsonObject = new JSONObject();
        userRepository.deleteAll();
        jsonObject.put("code","200");
        return jsonObject.toString();
    }

    public String apiLogin() { // api登录
        String string = HttpRequest.sendPost("http://open.edukg.cn/opedukg/api/typeAuth/user/login",
                "password=thueda2019&phone=18201616030");
        JSONObject jsonObject = new JSONObject(string);
//        System.out.println(jsonObject.getString("id"));
        if(!jsonObject.has("id")) {
            return apiId;
        }
        this.apiId = jsonObject.getString("id");
        return jsonObject.getString("id");
    }

    @PostMapping("/users/linkedInstances")
    public String getLinkedInstances(@Valid @RequestBody LinkInstance linkInstance) { // 实体链接接口
        String context = linkInstance.context, course = linkInstance.course;
        context = context.replaceAll(" ", "_");
        String id = apiLogin();
        String string = null;
        try {
            string = HttpRequest.sendPost("http://open.edukg.cn/opedukg/api/typeOpen/open/linkInstance",
                    "context=" + URLEncoder.encode(context, "utf-8") + "&course=" + URLEncoder.encode(course, "utf-8") + "&id=" + id);
        }
        catch (UnsupportedEncodingException e) {
            System.out.println(e);
        }
        if(!judge(string)) {
            return "failure";
        }

        System.out.println(string);
        JSONObject json = new JSONObject(string);
        if(!json.has("data")) {
            JSONArray temp = new JSONArray();
            return temp.toString();
        }
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
    public String ansQuestion(@Valid @RequestBody Question question) { // 实体问答接口
        String inputQuestion = question.inputQuestion, course = question.course;
        String id = apiId;
        String string = null;
        try {
            string = HttpRequest.sendPost("http://open.edukg.cn/opedukg/api/typeOpen/open/inputQuestion",
                    "course=" + course + "&inputQuestion=" + URLEncoder.encode(inputQuestion, "utf-8") + "&id=" + id);
        }
        catch (UnsupportedEncodingException e) {
            System.out.println(e);
        }
        if(!judge(string)) {
            return "failure";
        }

        System.out.println(string);
        JSONObject json = new JSONObject(string);
        if(json.has("code") && json.getString("code") != "0") {
            JSONArray ret = new JSONArray();

        }
        if(!json.has("data") || !json.has("code")) {
            JSONArray ret = new JSONArray();
            return ret.toString();
        }
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
//        String final_ret = retArray.get(0).("subject") + ", " + retArray.get(0).toString("value");
        JSONObject final_ret = retArray.getJSONObject(0);
        if(final_ret.getString("value").equals("")) {
            return "";
        }
        return final_ret.getString("subject") + ", " + final_ret.getString("value");
//        return retArray.toString();
    }

    @PostMapping("/users/addCollection")
    public String addCollection(@Valid @RequestBody AddCollection addCollection) {
        String username = addCollection.username, instanceName = addCollection.instanceName, course = addCollection.course;
        if(username == null || instanceName == null || course == null) {
            return "failure";
        }
        System.out.println("in addCollection");
        List<User> users = userRepository.findAll();
        for(User temp_user: users) {
            if(temp_user.getUsername().equals(username) && temp_user.isLoggedIn()) {
                Collection collection = new Collection(instanceName, course, temp_user);
//                for(int i = temp_user.collections.size() - 1; i >= 0; i--) {
//                    String cou = temp_user.collections.get(i).getCourse();
//                    if(cou == null) {
//                        temp_user.collections.remove(i);
//                    }
//                }
//                userRepository.save(temp_user);
                System.out.println(temp_user.collections);
                for(Collection temp_collection: temp_user.collections) {
                    if(temp_collection.getInstanceName().equals(instanceName)) {
                        return "Already in collection";
                    }
                }
                System.out.println("adding collection");
                temp_user.collections.add(collection);
                userRepository.save(temp_user);
                return "Success";
            }
        }
        return "No Such User";
    }

    @PostMapping("/users/deleteCollection")
    public String deleteCollection(@Valid @RequestBody AddCollection deleteCollection) {
        String username = deleteCollection.username, instanceName = deleteCollection.instanceName, course = deleteCollection.course;
        List<User> users = userRepository.findAll();
        System.out.println("in deleteCollection");
        for(User temp_user: users) {
            if(temp_user.getUsername().equals(username) && temp_user.isLoggedIn()) {
                System.out.println("Get users");
                for(int i = 0; i < temp_user.collections.size(); i++) {
                    if(temp_user.collections.get(i).getInstanceName().equals(instanceName) &&
                            temp_user.collections.get(i).getCourse().equals(course)) {
                        System.out.println(temp_user.collections.size());
                        temp_user.collections.remove(i);
                        userRepository.save(temp_user);
                        System.out.println(temp_user.collections.size());
                        System.out.println("deleting collection");
                        return "Success";
                    }
                }
            }
        }
        return "No such User";
    }
    @PostMapping("/users/getCollection")
    public String getCollections(@Valid @RequestBody String collection) {
        JSONObject json = new JSONObject(collection);
        String username = json.getString("username");
        List<User> users = userRepository.findAll();
        JSONArray retArray = new JSONArray();
        for(User temp_user: users) {
            if(temp_user.getUsername().equals(username)) {
                List<Collection> collections = temp_user.getCollections();
//                retObj.put("collections", collections);
                for(int i = collections.size() - 1; i >=0; i--) {
                    Collection temp = collections.get(i);
                    JSONObject obj = new JSONObject();
                    obj.put("course", temp.getCourse());
                    obj.put("instanceName", temp.getInstanceName());
                    retArray.put(obj);
                }
                return retArray.toString();
            }
        }
        return retArray.toString();
    }

    List<String> parseQuesion(String qBody) {
        List<String> ret = new ArrayList<>();
        for(int i = qBody.length() - 1; i >= 0; i--) {
            char selection = qBody.charAt(i);
            char point = ' ';
            if(i != qBody.length() - 1) {
                point = qBody.charAt(i + 1);
            }
            if((selection == 'D' || selection == 'C' || selection == 'B' || selection == 'A')) {
                String ans = qBody.substring(i);
                ret.add(ans);
                qBody = qBody.substring(0, i);
//                System.out.println(qBody);
                if(selection == 'A') {
                    ret.add(qBody);
                    break;
                }
            }
        }
        List<String> reverse = new ArrayList<>();
        for(int i = ret.size() - 1; i >= 0; i--) {
            reverse.add(ret.get(i));
        }
        return reverse;

    }


    @PostMapping("/users/exercise")
    public String getRelatedExercise(@Valid @RequestBody String instanceName) {
//        String instanceName = instanceName;
        String id = apiLogin();
        String string = null;
        try {
            string = HttpRequest.sendGet("http://open.edukg.cn/opedukg/api/typeOpen/open/questionListByUriName",
                    "uriName=" + URLEncoder.encode(instanceName, "utf-8") + "&id=" + id);
        }
        catch (UnsupportedEncodingException e) {
            System.out.println(e);
        }
        if(!judge(string)) {
            System.out.println("failed");
            return "failure";
        }
        JSONObject json = new JSONObject(string);
        JSONArray data = json.getJSONArray("data");
        JSONArray retArray = new JSONArray();
        for(int i = 0; i < data.length(); i++) {
            JSONObject question = data.getJSONObject(i);
            String qAnswer = question.getString("qAnswer");
            Long qId = question.getLong("id");
            String qBody = question.getString("qBody");
            List<String> quesionWithAns = parseQuesion(qBody);
            JSONObject obj = new JSONObject();
            obj.put("qId", qId);
            obj.put("questionWithAns", quesionWithAns);
            obj.put("qAnswer", qAnswer);
            retArray.put(obj);
        }
        return retArray.toString();
    }

    @PostMapping("/users/exam")
    public String giveExam(@Valid @RequestBody String examine) {
        JSONObject json = new JSONObject(examine);
        List<String> arr = JSON.parseArray(json.getJSONArray("knowledge").toString(), String.class);
        JSONArray retArray = new JSONArray();
        for(int i = 0; i < arr.size(); i++) {
            String know = arr.get(i);
            String relatedExercise = getRelatedExercise(know);
            if(relatedExercise.equals("failure")) {
                System.out.println("keyword failed");
                continue;
            }
            JSONArray temp_arr = new JSONArray(relatedExercise);
            for(int j = 0; j < temp_arr.length(); j++) {
                JSONObject new_obj = temp_arr.getJSONObject(j);
                boolean flag = false;
                for(int k = 0; k < retArray.length(); k++) {
                    JSONObject tmp_json = retArray.getJSONObject(i);
                    if(tmp_json.getLong("qId") == new_obj.getLong("qId")) {
                        flag = true;
                        break;
                    }
                }
                if(flag) {
                    continue;
                }
                retArray.put(new_obj);
            }
        }
        List<Integer> randList = getRandomTen(retArray.length());
        System.out.println(randList);
        JSONArray ret = new JSONArray();
        for(int i = 0; i < randList.size(); i++) {
            ret.put(retArray.get(randList.get(i)));
        }
        System.out.println("___________________________");
        System.out.println(ret.toString());
        return ret.toString();
    }
    List<Integer> getRandomTen(int len) {
        List<Integer> retList = new ArrayList<>();
        for(int i = 0; i < len; i++) {
            retList.add(i);
        }
        int tot = 100;
        Random rand = new Random();
        System.out.println("-----------------length----------------");
        System.out.println(len);
        if(len == 0) {
            return retList;
        }
        while (tot > 0) {
            tot--;
            int x = rand.nextInt(len), y = rand.nextInt(len);
            int originX = retList.get(x);
            retList.set(x, retList.get(y));
            retList.set(y, originX);
        }
        return  retList.subList(0, 10);
    }

    public List<String> getTopTenHistories(List<History> histories) {
        HashMap<String, Integer> map = new HashMap<>();
        for(History temp_his: histories) {
            String instanceName = temp_his.getInstanceName();
            if(map.containsKey(instanceName)) {
                Integer time = map.get(instanceName);
                map.put(instanceName, time + 1);
            }
            else {
                map.put(instanceName, 1);
            }
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            //降序排序
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                //return o1.getValue().compareTo(o2.getValue());
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        int tot_num = 0;
        List<String> ret = new ArrayList<>();
        for (Map.Entry<String, Integer> mapping : list) {
//            System.out.println(mapping.getKey() + ":" + mapping.getValue());
            ret.add(mapping.getKey());
            tot_num++;
            if(tot_num == 1) return ret;
        }
        return ret;
    }

    @PostMapping("/users/recommend")
    public String recommendExercise(@Valid @RequestBody String input) {
        JSONObject json = new JSONObject(input);
        String username = json.getString("username");
        List<User> allUser = userRepository.findAll();
        for(User temp_user: allUser) {
            if(temp_user.getUsername().equals(username) && temp_user.isLoggedIn()) {
                List<History> histories = temp_user.getHistories();
                List<String> arr = getTopTenHistories(histories);
                if(arr.size() == 0) {
                    arr.add("李白");
                }
                System.out.println(arr);
                System.out.println("--------------------------------------------------");
                JSONArray retArray = new JSONArray();
                for(int i = 0; i < arr.size(); i++) {
                    String know = arr.get(i);
                    String relatedExercise = getRelatedExercise(know);
                    if(relatedExercise.equals("failure")) {
                        continue;
                    }
                    JSONArray temp_arr = new JSONArray(relatedExercise);
                    System.out.println(i);
                    for(int j = 0; j < temp_arr.length(); j++) {
                        JSONObject new_obj = temp_arr.getJSONObject(j);
                        boolean flag = false;
                        for(int k = 0; k < retArray.length(); k++) {
                            JSONObject tmp_json = retArray.getJSONObject(i);
                            if(tmp_json.getLong("qId") == new_obj.getLong("qId")) {
                                flag = true;
                                break;
                            }
                        }
                        if(flag) {
                            continue;
                        }
                        retArray.put(new_obj);
                    }
                }
                List<Integer> randList = getRandomTen(retArray.length());
//                System.out.println(randList);
                JSONArray ret = new JSONArray();
                for(int i = 0; i < randList.size(); i++) {
                    ret.put(retArray.get(randList.get(i)));
                }
                return ret.toString();
            }
        }
        return "No such user";
    }





}