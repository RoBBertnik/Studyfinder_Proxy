package Spring_REST_API;

import Spring_REST_API.User.User;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class Firebase {
    private static FirebaseDatabase defaultDatabase;
    private static FirebaseApp defaultApp;
    private static Firestore db;


    @PostConstruct
    public void initialize(){
        try{
            FileInputStream serviceAccount = new FileInputStream("serviceaccount.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://studyfinder-b616c-default-rtdb.europe-west1.firebasedatabase.app")
                    .build();

            defaultApp = FirebaseApp.initializeApp(options);

            defaultDatabase = FirebaseDatabase.getInstance(defaultApp);
            db = FirestoreClient.getFirestore(defaultApp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User createUser(User user) throws ExecutionException, InterruptedException {

        if(user == null) {
            throw new NullPointerException("User can't be null!");
        }

        newUserEntryRealTime(user);
        return newEntryCloud(user);
    }

    private String newUserEntryRealTime(User user){
        String email = user.getEmail();
        user.setEmail(null);

        email = email.replace('.', ',');

        DatabaseReference ref = defaultDatabase.getReference("StudyFinder");
        DatabaseReference usersRef = ref.child("users");

        Map<String, Object> users = new HashMap<>();
        users.put(email, user);
        usersRef.updateChildrenAsync(users);
        email = email.replace(',', '.');
        user.setEmail(email);
        return user.getEmail();
    }

    private User newEntryCloud(User user) throws ExecutionException, InterruptedException {
        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("firstName", user.getFirstName());
        data.put("lastName", user.getLastName());
        data.put("courses", user.getCourses());
        ApiFuture<WriteResult> addedDocRef = db.collection("users").document(user.getEmail()).set(data);
        return user;
    }

    public List<User> getAll() throws ExecutionException, InterruptedException {
        List<User> userList = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = db.collection("users").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for(QueryDocumentSnapshot doc : documents) {
            userList.add(convertQueryToUser(doc));
        }
        return userList;
    }

    private User convertQueryToUser(QueryDocumentSnapshot snapshot){
        User user = snapshot.toObject(User.class);
        user.setEmail(snapshot.getId());
        return user;
    }


    public User getByEmail(String email) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("users").document(email);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if(document.exists()) {
            return convertDocumentToUser(document);
        }
        else{
            throw new NullPointerException("No such document!");
        }
    }

    private User convertDocumentToUser(DocumentSnapshot snapshot){
        User user = snapshot.toObject(User.class);
        user.setEmail(snapshot.getId());
        return user;
    }

    public User addCourseToExistingUser(String email, String course) throws ExecutionException, InterruptedException {
        User user = getByEmail(email);
        if(checkForDuplicateCourses(user, course)){
            return user;
        }
        user.addCourse(course);
        newUserEntryRealTime(user);
        return newEntryCloud(user);
    }

    private Boolean checkForDuplicateCourses(User user, String course){
        for(int i = 0; i < user.getCourses().size(); i++){
            if(user.getCourses().get(i).equals(course)){
                return true;
            }
        }
        return false;
    }

    public List<User> getByCourse(String course) throws ExecutionException, InterruptedException {
        List<User> userList = getAll();
        List<User> returnList = new ArrayList<User>();

        for(int i = 0; i < userList.size(); i++){
            User user = userList.get(i);
            if(checkForDuplicateCourses(user, course)){
                returnList.add(user);
            }
        }
        return returnList;
    }

    public User deleteCourseOfUser(String email, String course) throws ExecutionException, InterruptedException {
        User user = getByEmail(email);
        for(int i = 0; i< user.getCourses().size(); i++){
            if(user.getCourses().get(i).equals(course)){
                user.removeCourse(course);
                return createUser(user);
            }
        }
        throw new IllegalArgumentException("Kurs existiert nicht!");
    }
}
