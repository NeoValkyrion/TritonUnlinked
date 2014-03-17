package triton.unlinked;

/**
 * Created by jeremykao on 3/12/14.
 */

public class CourseObject {

    private String subject;
    private String title;

    public CourseObject(String courseSubject, String courseTitle){
        this.subject = courseSubject;
        this.title = courseTitle;
    }
    public String getSubject(){
        return this.subject;
    }
    public void setSubject(String courseSubject){
        this.subject = courseSubject;
    }
    public String getTitle(){
        return this.title;
    }
    public void setTitle(String courseSubject){
        this.title = courseSubject;
    }
}
