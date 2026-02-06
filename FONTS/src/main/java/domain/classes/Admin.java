package domain.classes;

/**
 * Class representing the Admin user in the system
 */
public class Admin extends Person{
    
    /**
     * Constructor of the class Admin
     * @param usr The username given to identify admin
     * @param pass The password given to identify admin
     */
    public Admin(String usr, String pass){
        super(usr, pass);
    }

    /**
     * Constructor of the class Admin from a Person
     * @param p The Person to be converted to Admin
     */
    public Admin (Person p) {
          super(p.getUsername(), p.getPassword());
     }
}
