package class1.dwit.com.assignmentreminder.domain;

/**
 * Created by User on 4/5/2017.
 */

public class Assignment {

    String deadline;
    String assignment_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String URL;
    String batch;
    int id;
    String name;

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getDeadline() {

        return deadline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getAssignment_name() {
        return assignment_name;
    }

    public void setAssignment_name(String assignment_name) {
        this.assignment_name = assignment_name;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }


}
