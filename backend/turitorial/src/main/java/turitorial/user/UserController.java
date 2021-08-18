package turitorial.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import turitorial.Status;

import javax.validation.Valid;
import java.util.List;
import org.json .*;
import turitorial.dataloader.HttpRequest;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;
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
    @DeleteMapping("/users/all")
    public String deleteUsers() {
        JSONObject jsonObject = new JSONObject();
        userRepository.deleteAll();
        jsonObject.put("code","200");
        return jsonObject.toString();
    }
}