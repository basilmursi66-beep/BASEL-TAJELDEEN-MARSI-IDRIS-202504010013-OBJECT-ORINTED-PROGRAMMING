import java.util.ArrayList;


public class course {

    private String courseName;
    private String electiveCourse;
    private ArrayList<String> subjects;


    public course(String courseName, String electiveCourse){

        this.courseName = courseName;
        this.electiveCourse = electiveCourse;

        subjects = new ArrayList<>();

        assignSubjects();

    }



    private void assignSubjects(){

    subjects.clear();

    // Standard subjects
    subjects.add("Programming Fundamentals");
    subjects.add("Database Systems");
    subjects.add("Software Engineering");

    // Elective subjects
    if(electiveCourse.equals("Cybersecurity")){
        subjects.add("Network Security");
        subjects.add("Ethical Hacking");
        subjects.add("Cyber Defense");
    }
    else if(electiveCourse.equals("Data Science")){
        subjects.add("Statistics");
        subjects.add("Data Analysis");
        subjects.add("Machine Learning");
    }
    else if(electiveCourse.equals("Artificial Intelligence")){
        subjects.add("AI Fundamentals");
        subjects.add("Machine Learning");
        subjects.add("Deep Learning");
    }
}

public void setElectiveCourse(String electiveCourse){
    this.electiveCourse = electiveCourse;
    assignSubjects();
}



    public String getCourseName(){

        return courseName;

    }



    public String getElectiveCourse(){

        return electiveCourse;

    }



    public ArrayList<String> getSubjects(){

        return subjects;

    }

}