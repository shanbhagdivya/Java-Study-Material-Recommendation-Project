package main.java.com.example.myloginpage;
public class EBook extends StudyMaterial {
    private String author;
    private String imageUrl;
    public EBook(String imageUrl, String title, String subject, String SM_TYPE, double price, double rating, int noOfRatings,
                 String url, String author) {
        super(title, subject, SM_TYPE, price, rating, noOfRatings, url); // parent class constructor
        this.imageUrl = imageUrl;
        this.author = author;
    }
    public String getAuthor() {
        return author;
    }
    public String getImageUrl() {
        return imageUrl;
    }
}
