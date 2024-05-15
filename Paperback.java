package main.java.com.example.myloginpage;
public class Paperback extends EBook {
    private String publisher;
    public Paperback(String imageUrl, String title, String subject, String SM_TYPE, double price,
                     double rating, int noOfRatings, String url, String author, String publisher) {
        super(imageUrl, title, subject, SM_TYPE, price, rating, noOfRatings, url, author);
        this.publisher = publisher;
    }
    public String getPublisher() {
        return publisher;
    }
}
