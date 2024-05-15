package main.java.com.example.myloginpage;
public class Course extends StudyMaterial {
    private String offeredBy;
    private int noOfLanguagesAvailable;
    private String languages;
    private long noOfPeopleEnrolled;
    public Course(String title, String subject, String SM_TYPE, double price, double rating, int noOfRatings,
                  String offeredBy, int noOfLanguagesAvailable, String languages, long noOfPeopleEnrolled, String url) {
        super(title, subject, "Courses", price, rating, noOfRatings, url); // Call parent class constructor
        this.offeredBy = offeredBy;
        this.noOfLanguagesAvailable = noOfLanguagesAvailable;
        this.languages = languages;
        this.noOfPeopleEnrolled = noOfPeopleEnrolled;
    }
    public String getOfferedBy() {
        return offeredBy;
    }
    public int getNoOfLanguagesAvailable() {
        return noOfLanguagesAvailable;
    }
    public String getLanguages() {
        return languages;
    }
    public long getNoOfPeopleEnrolled() {
        return noOfPeopleEnrolled;
    }
}
