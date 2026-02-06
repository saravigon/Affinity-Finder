package domain.classes;
import java.util.List;
import java.util.ArrayList;
 
/**
 * Class Profile
 * Represents a profile that extends a person, adding description, created forms, and answered forms.
 */
public class Profile extends Person{

     /**
      * Description of the profile
      */
     private String description;

     /**
      * List of forms created by the profile
      */
     private List<Integer> createdForms;

     /**
      * List of forms answered by the profile
      */
     private ArrayList<Integer> answeredForms;

     /**
      * The constructor of the class Profile
      * @param usr The username to identify and relate the user
      * @param pass The password to identify and relate the user as well
      */
     public Profile (String usr, String pass) {
          super(usr, pass);
          description="No description yet";
          this.createdForms = new ArrayList<>();
          this.answeredForms = new ArrayList<>();
     }

     /**
      * Also a constructor of Profile
      * @param p Person to relate the profile with the user
      */
     public Profile (Person p) {
          super(p.getUsername(), p.getPassword());
          description="No description yet";
          this.createdForms = new ArrayList<>();
          this.answeredForms = new ArrayList<>();
     }

     /**
      * Gets the description of the actual profile
      * @return String descriptionof the actual profile
      */
     public String getDescription(){
          return description;
     }

     /**
      * Gets all forms created by profile
      * @return List of forms created by the actual profile
      */
     public List<Integer> getcreatedForms(){
          return createdForms;
     }

     /**
      * Gets all forms answered by profile
      * @return List of forms answered by the actual profile
      */
     public ArrayList<Integer> getAnsweredForms(){
          return answeredForms;
     }

     /**
      * Change the current description to the given one
      * @param desc The new description 
      */
     public void setDescription(String desc){
          description=desc;
     }

     /**
      * Adds a form to the current forms created by user
      * @param UFID The form id of the form to be added
      */
     public void addCreatedForm(Integer UFID){
          createdForms.add(UFID);
     }

     /**
      * Adds a form to the current forms answered by user
      * @param UFID The form to be added
      */
     public void addAnsweredForm(Integer UFID){
          answeredForms.add(UFID);
     }

     /**
      * Delete a form creted by user
      * @param f The form to be deleted
      */
     public void deleteForm(Integer f){
          createdForms.remove(f);
     }

     /**
      * Delete a form answered by user
      * @param f The form to be deleted
      */
     public void deleteAnsweredForm(Integer f){
          answeredForms.remove(f);
     }

}
