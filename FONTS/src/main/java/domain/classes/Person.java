package domain.classes;

/**
 * Class Person
 * Represents a person identified by a unique identifier (UUID), with a username and password.
 */
public class Person {

    /**
     * Unique identifier for the person
     */
    private int UUID; 

    /**
     * Username of the person
     */
    private String user; 

    /**
     * Password of the person
     */
    private String password; 

    /**
     * Constructor of the class Person
     * @param usr The username wanted to identify the user
     * @param pass The password wanted to identify the user
     */
    public Person (String usr, String pass) {
        this.user = usr;
        this.password = pass;
        this.UUID = generateID(usr);
    }

    /**
     * Generates an unique ID for each user based on its username
     * @param usr The username of the user
     * @return An integer representing the unique ID of the user
     */
    private int generateID(String usr){
        return (usr.hashCode() - Long.valueOf(System.currentTimeMillis()).hashCode());
    }

    //  ## GETTERS AND SETTERS ##

    /**
     * Gets the username of the actual person
     * @return String username of the actual person
     */
    public String getUsername(){
        return user;
    }

    /**
     * Gets the password of the actual person
     * @return String password of the actual person
     */
    public String getPassword(){
        return password;
    }

    /**
     * Gets the UUID of the actual person
     * @return int UUID of the actual person
     */
    public int getUUID(){
        return UUID;
    }

    /**
     * Changes the username of the actual person
     * @param usr The new username to be set
     */
    public void changeUsername(String usr){
        user = usr;
    }

    /**
     * Changes the password of the actual person
     * @param pass The new password to be set
     */
    public void changePassword(String pass){
        password = pass;
    }

}
