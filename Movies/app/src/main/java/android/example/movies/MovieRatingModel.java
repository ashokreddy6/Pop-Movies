package android.example.movies;

/**
 * Created by Ashok on 12/27/2015.
 */
public class MovieRatingModel {
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String author;
    private String content;
    private String url;
}
