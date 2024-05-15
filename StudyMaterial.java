package main.java.com.example.myloginpage;
public class StudyMaterial {
    private String title;
    private String subject;
    private final String SM_TYPE;
    private double price;
    private double rating;
    private int noOfRatings;
    private String url;
    public StudyMaterial(String title, String subject, String SM_TYPE, double price, double rating, int noOfRatings, String url) {
        this.title = title;
        this.subject = subject;
        this.SM_TYPE = SM_TYPE;
        this.price = price;
        this.rating = rating;
        this.noOfRatings = noOfRatings;
        this.url = url;
    }
    public String getTitle() {
        return title;
    }
    public String getSubject() {
        return subject;
    }
    public String getSM_TYPE() {
        return SM_TYPE;
    }
    public double getPrice() {
        return price;
    }
    public double getRating() {
        return rating;
    }
    public int getNoOfRatings() {
        return noOfRatings;
    }
    public String getUrl() {
        return url;
    }
}