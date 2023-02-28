package Spring_REST_API.User;

import java.util.ArrayList;
import java.util.List;

public  class User {

    private String username = null;
    private String firstName = null;
    private String lastName = null;
    private String email = null;

    private List<String> courses = new ArrayList<String>();

    public User(){

    }

    public User(List<String> courses, String email, String firstName, String lastName, String username) {

        if(courses == null || email == null ||  firstName == null || lastName == null || username == null){
            throw new NullPointerException("No Value can be null!");
        }

        this.courses = courses;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String email, String firstName, String lastName, String username) {

        if(email == null ||  firstName == null || lastName == null || username == null){
            throw new NullPointerException("No Value can be null!");
        }

        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }


    public String getUsername(){
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getCourses(){ return courses; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void addCourse(String course) {
        courses.add(course);
    }

    public void removeCourse(String course) {
        courses.remove(course);
    }

    private void setCourses(List<String> courses){
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + firstName + " " + lastName + '\'' +
                ", courses='" + courses + '\'' +
                '}';
    }
}
