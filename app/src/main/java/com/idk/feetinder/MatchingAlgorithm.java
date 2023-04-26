/* Krish, commented out because doesn't compile yet and haven't had time to fix issues
package com.idk.feetinder;

import android.os.Build;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

public class MatchingAlgorithm {

    public static void main(String[] args) throws InterruptedException {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://your-firebase-url.firebaseio.com/")
                .setServiceAccount("path/to/serviceAccount.json")
                .build();
        FirebaseApp.initializeApp(options);
        User user1 = collectUserData();
        User user2 = collectUserData();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.child(user1.getId()).setValueAsync(user1);
        usersRef.child(user2.getId()).setValueAsync(user2);
        int k = 2;
        List<User> users = getUsersFromFirebase();
        List<User> neighbors1 = KNearestNeighbor.findKNearestNeighbors(user1, users, k);
        List<User> neighbors2 = KNearestNeighbor.findKNearestNeighbors(user2, users, k);
        Set<User> commonNeighbors = new HashSet<>(neighbors1);
        commonNeighbors.retainAll(neighbors2);
        System.out.println("User 1: " + user1);
        System.out.println("User 2: " + user2);
        System.out.println("Common neighbors: " + commonNeighbors);
    }

    public static User collectUserData() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your gender (male/female/other): ");
        String gender = scanner.nextLine();
        System.out.print("Enter your age: ");
        int age = scanner.nextInt(); scanner.nextLine();
        System.out.print("Enter your preferences (comma-separated): ");
        String preferencesString = scanner.nextLine();
        Set<String> preferences = new HashSet<>(Arrays.asList(preferencesString.split(","))); System.out.println();
        return new User(name, preferences, gender, age);
    }

    public static List<User> getUsersFromFirebase() throws InterruptedException {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        List<User> users = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener(){
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    User user = childSnapshot.getValue(User.class);
                    users.add(user);
                }
                latch.countDown();
            }
            public void onCancelled(DatabaseError databaseError){
                System.out.println("Error reading from Firebase: " + databaseError.getMessage());
                latch.countDown();
            }
        });
        latch.await();
        return users;
    }
}

//user class - just represents user
class User {
    private String id;
    private String name;
    private Set<String> preferences;
    private String gender;
    private int age;

    public User(){}
    public User(String name, Set<String> preferences, String gender, int age) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.preferences = preferences;
        this.gender = gender;
        this.age = age;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public Set<String> getPreferences(){
        return preferences;
    }

    public String getGender(){
        return gender;
    }

    public int getAge(){
        return age;
    }

    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", preferences=" + preferences +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                '}';
    }

}


    public static List<User> findKNearestNeighbors(User user, List<User> users, int k){
        Map<User, Double> similarityScores = new HashMap<>();
        for (User otherUser : users){
            if (!user.equals(otherUser)){
                double score = calculateSimilarityScore(user, otherUser);
                similarityScores.put(otherUser, score);
            }
        }

        //sort users by similarity scores
        List<User> sortedUsers = new ArrayList<>(users);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sortedUsers.sort((u1, u2) -> Double.compare(similarityScores.get(u2), similarityScores.get(u1)));
        }
        return sortedUsers.subList(0, Math.min(k, sortedUsers.size()));
    }


    private static double calculateSimilarityScore(User user1, User user2) {
        Set<String> intersection = new HashSet<>(user1.getPreferences());
        intersection.retainAll(user2.getPreferences());
        Set<String> union = new HashSet<>(user1.getPreferences());
        union.addAll(user2.getPreferences());
        double jIndex = (double) intersection.size() / union.size();
        double ageDiff = Math.abs(user1.getAge() - user2.getAge());
        double genderDiff = user1.getGender().equals(user2.getGender()) ? 0.0 : 1.0;
        double eDist = Math.sqrt(ageDiff * ageDiff + genderDiff * genderDiff);
        //finally return similarity score (took too long)
        return jIndex * (1.0 / (1.0 + eDist));
    }

}
*/