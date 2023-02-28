package Spring_REST_API.controller;

import Spring_REST_API.Firebase;
import Spring_REST_API.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class UserController {

    @Autowired
    private Firebase firebase;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @GetMapping("/users")
    public List<User> getUsers(@RequestParam(required = false) String course) throws ExecutionException, InterruptedException {
        if(course == null) {
            return firebase.getAll();
        }
        else{
            return firebase.getByCourse(course);
        }
    }

    @RequestMapping(value = "/users/{email}", method = RequestMethod.GET)
    public User getUserByEmail(@PathVariable String email) throws ExecutionException, InterruptedException {
         return firebase.getByEmail(email);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public User saveUser(@RequestBody User user) throws ExecutionException, InterruptedException {
        return firebase.createUser(user);
    }

    @RequestMapping(value = "/users/{email}", method = RequestMethod.PUT)
    public User addCourseToUser(@PathVariable String email, @RequestBody String course) throws ExecutionException, InterruptedException {
        StringBuilder string = new StringBuilder(course);
        string.deleteCharAt(0);
        string.deleteCharAt(string.length()-1);
        return firebase.addCourseToExistingUser(email, string.toString());
    }

    @RequestMapping(value = "/users/{email}", method = RequestMethod.DELETE)
    public User removeCourseFromUser(@PathVariable String email, @RequestBody String course) throws ExecutionException, InterruptedException {
        StringBuilder string = new StringBuilder(course);
        string.deleteCharAt(0);
        string.deleteCharAt(string.length()-1);
        return firebase.deleteCourseOfUser(email, string.toString());
    }
}
