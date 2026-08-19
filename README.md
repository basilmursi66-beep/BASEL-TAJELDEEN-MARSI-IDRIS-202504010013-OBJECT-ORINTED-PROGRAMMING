# BASEL-TAJELDEEN-MARSI-IDRIS-202504010013-OBJECT-ORINTED-PROGRAMMING

This is the Java Object-Oriented Programming Tutorials.This is a Beginning Java Object Oriented Programming Tutorials.

Object-Oriented Programming (OOP) programming tutorials and exercises compiled by me as part of a coursework.

This repository has some Java exercises on basic programming topics like classes, objects, constructors, methods, inheritance and GUI development.

---

## 📚 Contents

The repository comprises of individual folders, where each folder contains a tutorial for a specific Java programming concept.

| Tutorial       | Main Content                                            |
| -------------- | ------------------------------------------------------- |
| `tutorial_1`   | Classes, objects, attributes, constructors, and methods |
| `tutorial_2`   | Java class and object exercises                         |
| `tutorial_3-4` | Inheritance and relationships between classes           |
| `tutorial_5`   | Object-oriented programming practice and documentation  |
| `tutorial_6`   | Inheritance using employee and lecturer classes         |
| `tutorial_7`   | Abstract classes / inheritance using appliances         |
| 'tutorial_8-9' | Practical tutorial work included in the repository      |
| `tutolrial_10` | Java GUI quiz application                               |
---

## 🗂️ Project Structure

```text

│
├── tutorial_1/
│   ├── Student.java
│   └── main.java
│
├── tutorial_2/
│   ├── Student.java
│   └── main.java
│
├── tutorial_3/
│   ├── Person.java
│   ├── Student.java
│   ├── Lecturer.java
│   └── Main.java
│
├── tutorial_5/
│   ├── Student.java
│   ├── main.java
│   └── docementation.txt
│
├── tutorial_6/
│   ├── employee.java
│   ├── lecturer.java
│   └── main.java
│
├── tutorial_7/
│   ├── Applience.java
│   ├── Microwave.java
│   ├── WashingMachine.java
│   └── Main.java
|
|── tutorial_8-9/
|   |── Main.java
|   |── Save.java
|   |── task.txt
│
├── tutolrial_10/
│   ├── Questions.java
│   └── QuizBattleGUI.java
│
├── .gitignore
├── LICENSE
└── README.md
```

> Note: The spelling of the folder tutolrial_10 is on purpose, as it is in the original repository.

---

## 💻 Technologies Used

* **Java**
* Object-Oriented Programming (OOP)
* Java Classes & Objects
* Constructors
* Methods
* Inheritance
* Polymorphism
* GUI Programming
* Git & GitHub

---

## 🧠 Concepts Practiced

### 1. Classes and Objects

The first tutorials give an introduction to the basic organization of Java classes.

For instance, `tutorial_1` has a class named `Student` which has attributes like:

* Name
* Age
* GPA

It also contains the means of presenting a student's information and executing activities like learning and taking a test.

```java
Student s1 = new Student("basil", 20, 3.8);

s1.displayInfo();
s1.study();
s1.takeExsam();
```

---

### 2. Constructors

Constructors are used to create an object.

Example:

```java
Student(String studentName, int studentAge, double studentGpa) {
    name = studentName;
    age = studentAge;
    gpa = studentGpa;
}
```

---

### 3. Methods

The projects illustrate how methods can be used to specify behaviours for objects.

Examples include:

```java
displayInfo()
study()
takeExsam()
```

---

### 4. Inheritance

Later tutorials will present relationships between classes.

For instance, in tutorial_3 is available:

* `Person`
* `Student`
* `Lecturer`
* `Main`

This will give some experience in object oriented design and the creation of related classes.

---

### 5. Employee and Lecturer Classes

The `tutorial_6` example builds upon the concept of object-oriented relationships in the following ways:

```text
employee
   │
   └── lecturer
```

The tutorial contains separate classes for employees and lecturers together with a main program for testing them.

---

### 6. Abstract Classes and Appliances

`tutorial_7` requires the use of appliance related classes:

* `Applience`
* `Microwave`
* `WashingMachine`
* `Main`

This exercise is an opportunity to practice creating a parent class and specific child classes.

---

### 7. Java GUI Programming

A small quiz-application is provided in the last tutorial of the repository.

The `tutolrial_10` folder includes:

```text
Questions.java
QuizBattleGUI.java
```

The project illustrates the use of java for building graphical user interface for interactive Quiz.

---

## 🚀 How to Run

### Requirements

Install the following:

Be sure to have Java Development Kit (JDK).
A Java IDE (Integrated Development Environment) like IntelliJ IDEA, Eclipse, or Netbeans

The programs are also pre-compilable and executable directly via the command line.

### Clone the Repository

```bash
https://github.com/basilmursi66-beep/BASEL-TAJELDEEN-MARSI-IDRIS-202504010013-OBJECT-ORINTED-PROGRAMMING
```

Change the working directory to the project directory:

```bash
BASEL-TAJELDEEN-MARSI-IDRIS-202504010013-OBJECT-ORINTED-PROGRAMMING 
```

### Compile a Tutorial

For example:

```bash
cd tutorial_1
javac Student.java main.java
```

Afterwards, execute the main class:

```bash
java main
```

The syntax of the command might differ, depending on the class name and the Java environment in use.

---

## 📖 Learning Objectives

The aim of this repository is to learn and showcase basic concepts of java programming and object-oriented programming.

These exercises were completed with the following people:

* Creating Java classes
Creating and manipulating objects.
* Defining attributes
* Creating constructors
* Creating methods
Passing or passing along values to objects.
* Using inheritance
Create parent/child classes
* Applying OOP principles
Writing a basic GUI application with Java.Writing a very simple GUI app with java.
Create a structure for Java projects using Git and GitHub

---

## 👨‍💻 Author



Here are some of the coursework and practical exercises I have created while studying Java Object Oriented Programming.

---

## 📄 License

MIT License

Copyright (c) 2026 basilmursi66-beep

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

---

## 🔗 Repository

The full source code can be found on GitHub: https://github.com/basilmursi66-beep/BASEL-TAJELDEEN-MARSI-IDRIS-202504010013-OBJECT-ORINTED-PROGRAMMING
