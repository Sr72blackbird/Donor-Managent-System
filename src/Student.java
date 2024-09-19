public class Student {
    private int id;       // Auto-incremented ID
    private String admno;    // Admission number
    private String name;  // Student name
    private String course;// Student's course

    public Student(int id, String admno, String name, String course) {
        this.id = id;
        this.admno = admno;
        this.name = name;
        this.course = course;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAdmno() { return admno; }
    public void setAdmno(String admno) { this.admno = admno; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

// Representing the students in a combo box.
    @Override
    public String toString() {
        return name + " (" + admno + ")";
    }
}
