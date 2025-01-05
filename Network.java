/** Represents a social network. The network has users, who follow other uesrs.
 *  Each user is an instance of the User class. */
public class Network {

    // Fields
    private User[] users;  // the users in this network (an array of User objects)
    private int userCount; // actual number of users in this network

    /** Creates a network with a given maximum number of users. */
    public Network(int maxUserCount) {
        this.users = new User[maxUserCount];
        this.userCount = 0;
    }

    /** Creates a network  with some users. The only purpose of this constructor is 
     *  to allow testing the toString and getUser methods, before implementing other methods. */
    public Network(int maxUserCount, boolean gettingStarted) {
        this(maxUserCount);
        users[0] = new User("Foo");
        users[1] = new User("Bar");
        users[2] = new User("Baz");
        userCount = 3;
    }

    public int getUserCount() {
        return this.userCount;
    }
    /** Finds in this network, and returns, the user that has the given name.
     *  If there is no such user, returns null.
     *  Notice that the method receives a String, and returns a User object. */
    public User getUser(String name) {
        if (name == null) {
            return null;
        }

        String lowerCaseName = name.toLowerCase();
        for (int i = 0; i < userCount; i++) {
            if (users[i].getName().toLowerCase().equals(lowerCaseName)) {
                return users[i];
            }
        }
        return null;
    }

    /** Adds a new user with the given name to this network.
    *  If ths network is full, does nothing and returns false;
    *  If the given name is already a user in this network, does nothing and returns false;
    *  Otherwise, creates a new user with the given name, adds the user to this network, and returns true. */
    public boolean addUser(String name) {
        if (name == null || userCount >= users.length || getUser(name) != null) {
            return false;
        } else {
            User newUser = new User(name);
            users[userCount] = newUser;
            userCount ++;
            return true;
        }
    }

    /** Makes the user with name1 follow the user with name2. If successful, returns true.
     *  If any of the two names is not a user in this network,
     *  or if the "follows" addition failed for some reason, returns false. */
    public boolean addFollowee(String name1, String name2) {
        User user1 = getUser(name1);
        User user2 = getUser(name2);

        if (user1 == null || user2 == null || name1.toLowerCase().equals(name2.toLowerCase())) {
            return false;
        }
        
        return user1.addFollowee(name2);
    }
    
    /** For the user with the given name, recommends another user to follow. The recommended user is
     *  the user that has the maximal mutual number of followees as the user with the given name. */
    public String recommendWhoToFollow(String name) {
        User givenUser = getUser(name);
        if (givenUser == null) {
            return null;
        }
        String recommended = "";
        int max = 0;
        for (int i = 0; i < userCount; i++) {
            if (!users[i].getName().equals(name)) {
                int mutualCount = givenUser.countMutual(users[i]);
                if (mutualCount > max) {
                    max = mutualCount; 
                    recommended = users[i].getName();
                }
            }
        }
        return recommended;
    }

    /** Computes and returns the name of the most popular user in this network: 
     *  The user who appears the most in the follow lists of all the users. */
    public String mostPopularUser() {
        int [] counter = new int [userCount];
        for (int i = 0; i < userCount; i ++) {
            String name = users[i].getName();
            counter[i] = followeeCount(name);
        }

        int max = 0;
        String mostPopular = null;
        for (int i = 0; i < counter.length; i++) {
            if (counter[i] > max) {
                max = counter[i];
                mostPopular = users[i].getName();
            }
        }
        return mostPopular;
    }

    /** Returns the number of times that the given name appears in the follows lists of all
     *  the users in this network. Note: A name can appear 0 or 1 times in each list. */
    private int followeeCount(String name) {
        int counter = 0;
        for (int i = 0; i < userCount; i++) {
            User user = users[i];
            String[] follows = user.getfFollows();
            if (follows != null) {
                for (int j = 0; j < follows.length; j++ ) {
                    if (follows[j] != null && follows[j].equals(name)) {
                        counter ++;
                        break;
                    }
                }
            }
        }
        return counter;
    }

    // Returns a textual description of all the users in this network, and who they follow.
    public String toString() {
        String output = "Network:";

        if (userCount == 0) {
            return output; 
        }
        output += "\n";

        for (int i = 0; i < userCount; i++) {
            User user = users[i];
            String name = user.getName();
            String[] follow = user.getfFollows();
            output += name + " ->";
           
            if (follow == null || follow.length == 0 || follow[0] == null) {
            } else {
                output += " ";
                for (int j = 0; j < follow.length; j++) {
                    if (follow[j] != null) {
                        output += follow[j] + " ";
                        /* 
                        if (j < follow.length - 1) {
                            output += " "; 
                        }   
                         */               
                    }
                }
            }
            if (i < userCount - 1) {
                output += "\n"; 
            }                  
        }
        return output;
    }
}
