class Student{
    String Name;
    int Age;
    double Gpa;


    Student(String StudentName, int StudentAge, double StudentGpa){
    Name = StudentName;
    Age = StudentAge;
    Gpa = StudentGpa;
}

public void displayInfo(){
    System.out.println("Name: " + Name);
    System.out.println("Age: " + Age);
    System.out.println("Gpa: " + Gpa);
}
public void study(){
    System.out.println(Name + " is studying");
}
public void takeExam(){
System.out.println(Name + " is taking an exam");
}
}   