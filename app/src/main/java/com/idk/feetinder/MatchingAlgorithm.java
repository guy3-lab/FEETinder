import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

class User {
    private String name;
    private int toesPreference;
    private String coffeePreference;
    private String footSizePreference;
    private String humanRightsPreference;
    private String favoriteToe;
    private int compatibilityScore;
   
    //idk if you need this cuz id from other codes takes this place??
    //but this is just user instantiation
    public User(String name, int toesPreference, String coffeePreference, String footSizePreference, String humanRightsPreference, String favoriteToe) {
        this.name = name;
        this.toesPreference = toesPreference;
        this.coffeePreference = coffeePreference;
        this.footSizePreference = footSizePreference;
        this.humanRightsPreference = humanRightsPreference;
        this.favoriteToe = favoriteToe;
        this.compatibilityScore = 0;
    }
   
    // need all of these
    public void incrementCompatibilityScore(){
        compatibilityScore++;
    }
    public int getCompatibilityScore(){
        return compatibilityScore;
    }
    public int getToesPreference(){
        return toesPreference;
    }
    public String getCoffeePreference(){
        return coffeePreference;
    }
    public String getFootSizePreference(){
        return footSizePreference;
    }
    public String getHumanRightsPreference(){
        return humanRightsPreference;
    }
    public String getFavoriteToe(){
        return favoriteToe;
    }
}

public class UserCompRank{ // class prob diff depending on what other code files say
    private static List<User> userList;
    private static User mainUser;

    //might need this to add user in initializeUsers or just use built in
    //public void addUser(User user) userList.add(user);
    

// TEST (comment out if needed)
    public static void main(String[] args) {
        //initializeUsers();
        calculateCompatibilityScores();
        rankUsers();
        //maybe not print?? because its just like an update in the app (idk)
        //printValidity();
    }
    private static void initializeUsers() {
        //these are just possible user which will obviosuly be removed once i understand how the id stuff in other files works
        mainUser = new User("Main User", 3, "Black", "10 inches", "Right of work", "Long toe");
        userList = new ArrayList<>();
        userList.add(new User("User 1", 2, "Tea", "4 feet", "Freedom of expression", "Big toe"));
	    userList.add(new User("User 2", 5, "Cappuccino", "13 inches", "Freedom of expression", "Long toe"));
	    userList.add(new User("User 3", 1, "Latte", "26 yards", "Right to privacy", "Big toe"));
	    userList.add(new User("User 4", 4, "Tea", "6 inches", "Right to education", "Pinky toe"));
    }
// END TEST



//MAIN FUNCTION
    private static void calculateCompatibilityScores() {
        for (User user : userList) {
            // q1:How many toes do you prefer?
            if (user.getToesPreference() == mainUser.getToesPreference()) user.incrementCompatibilityScore();
           
            // q2:How do you like your coffee?
            if (mainUser.getCoffeePreference().equals("Black") && user.getCoffeePreference().equals("Tea") ||
                    mainUser.getCoffeePreference().equals("Black") && user.getCoffeePreference().equals("Black") ||
                    mainUser.getCoffeePreference().equals("Tea") && user.getCoffeePreference().equals("Tea") ||
                    mainUser.getCoffeePreference().equals("Tea") && user.getCoffeePreference().equals("Black") ||
                    mainUser.getCoffeePreference().equals("Latte") &&
                            (user.getCoffeePreference().equals("Latte") || user.getCoffeePreference().equals("Cappuccino") || user.getCoffeePreference().equals("Mocha")) ||
                    mainUser.getCoffeePreference().equals("Cappuccino") &&
                            (user.getCoffeePreference().equals("Cappuccino") || user.getCoffeePreference().equals("Latte") || user.getCoffeePreference().equals("Mocha")) ||
                    mainUser.getCoffeePreference().equals("Mocha") &&
                            (user.getCoffeePreference().equals("Mocha") || user.getCoffeePreference().equals("Latte") || user.getCoffeePreference().equals("Cappuccino"))) {
                user.incrementCompatibilityScore();
            }
           
            // q3:Ideal foot size?
            int mainFootSize = convertFootSize(mainUser.getFootSizePreference());
            int userFootSize = convertFootSize(user.getFootSizePreference());
            int footSizeDifference = Math.abs(mainFootSize - userFootSize);
            if (footSizeDifference == getMinimumFootSizeDifference()) {
                user.incrementCompatibilityScore();
            }
           
            // q4:What are the most important universal human rights?
            if (mainUser.getHumanRightsPreference().equals("Right to work") && user.getHumanRightsPreference().equals("Right to education") ||
                    mainUser.getHumanRightsPreference().equals("Right to work") && user.getHumanRightsPreference().equals("Right to work") ||
                    mainUser.getHumanRightsPreference().equals("Right to education") && user.getHumanRightsPreference().equals("Right to education") ||
                    mainUser.getHumanRightsPreference().equals("Right to education") && user.getHumanRightsPreference().equals("Right to work") ||
                    mainUser.getHumanRightsPreference().equals("Right to life and liberty") &&
                            (user.getHumanRightsPreference().equals("Right to life and liberty") || user.getHumanRightsPreference().equals("Right to privacy") || user.getHumanRightsPreference().equals("Freedom of expression")) ||
                    mainUser.getHumanRightsPreference().equals("Right to privacy") &&
                            (user.getHumanRightsPreference().equals("Right to privacy") || user.getHumanRightsPreference().equals("Right to liberty") || user.getHumanRightsPreference().equals("Freedom of expression")) ||
                    mainUser.getHumanRightsPreference().equals("Freedom of expression") &&
                            (user.getHumanRightsPreference().equals("Freedom of expression") || user.getHumanRightsPreference().equals("Right to privacy") || user.getHumanRightsPreference().equals("Right to life and liberty"))) {
                user.incrementCompatibilityScore();
            }

            // q5:Favorite toe?
            int mainFavoriteToe = convertToeNumber(mainUser.getFavoriteToe());
            int userFavoriteToe = convertToeNumber(user.getFavoriteToe());
            int toeDifference = Math.abs(mainFavoriteToe - userFavoriteToe);
            if (toeDifference == getMinimumToeDifference()) user.incrementCompatibilityScore();
        }
    }
    //simple converting for strings for easier comparison in foot size function
    private static int convertFootSize(String footSize) {
        Map<String, Integer> footSizeMap = new HashMap<>();
        footSizeMap.put("6 inches", 6);
        footSizeMap.put("10 inches", 10);
        footSizeMap.put("13 inches", 13);
        footSizeMap.put("4 feet", 48);
        footSizeMap.put("26 yards", 936);
        return footSizeMap.get(footSize);
    }
    
    //same as above but for which toe user likes
    private static int convertToeNumber(String toe) {
        Map<String, Integer> toeNumberMap = new HashMap<>();
        toeNumberMap.put("Big toe", 5);
        toeNumberMap.put("Long toe", 4);
        toeNumberMap.put("Middle toe", 3);
        toeNumberMap.put("Ring toe", 2);
        toeNumberMap.put("Pinky toe", 1);
        return toeNumberMap.get(toe);
    }
    
    //get diff to comapre to other user diff
    private static int getMinimumFootSizeDifference() {
        int minDifference = Integer.MAX_VALUE;
        for (User user : userList) {
            int mainFootSize = convertFootSize(mainUser.getFootSizePreference());
            int userFootSize = convertFootSize(user.getFootSizePreference());
            int footSizeDifference = Math.abs(mainFootSize - userFootSize);
            if (footSizeDifference < minDifference) {
                minDifference = footSizeDifference;
            }
        }
        return minDifference;
    }
    
    //same as above but toe diffs
    private static int getMinimumToeDifference() {
        int minDifference = Integer.MAX_VALUE;
        for (User user : userList) {
            int mainFavoriteToe = convertToeNumber(mainUser.getFavoriteToe());
            int userFavoriteToe = convertToeNumber(user.getFavoriteToe());
            int toeDifference = Math.abs(mainFavoriteToe - userFavoriteToe);
            if (toeDifference < minDifference) {
                minDifference = toeDifference;
            }
        }
        return minDifference;
    }
//END OF MAIN FUNCTION
   
   
    //sorts all users so a final update
    public static void rankUsers(){ userList.sort(Comparator.comparingInt(User::getCompatibilityScore).reversed());}


    //TEST (comment out if needed)
    /*public static void printValidity() {
        // System.out.println("Compatibility Ranking:");
        // for (int i = 0; i < userList.size(); i++) {
        //     User user = userList.get(i);
        //     System.out.println((i + 1) + ". " + user);
        // }
    }*/
}
