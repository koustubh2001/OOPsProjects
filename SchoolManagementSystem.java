import java.util.*;

class Person {
    protected String name;
    protected int age;
    protected String id;

    public Person(String name, int age, String id) {
        this.name = name;
        this.age = age;
        this.id = id;
    }

    public String getName() { return name; }
    public String getId() { return id; }
}

class Student extends Person {
    private Map<String, Integer> grades; // Subject -> Grade
    private String assignedClass;
    private int attendancePercentage;

    public Student(String name, int age, String id, String assignedClass) {
        super(name, age, id);
        this.assignedClass = assignedClass;
        this.grades = new HashMap<>();
        this.attendancePercentage = 100; // Default full attendance
    }

    public void addGrade(String subject, int grade) {
        grades.put(subject, grade);
    }

    public void setAttendance(int percentage) {
        this.attendancePercentage = percentage;
    }

    public boolean isEligibleForExam() {
        return attendancePercentage >= 75;
    }

    public void printReport() {
        System.out.println("Report for: " + name);
        System.out.println("Class: " + assignedClass);
        System.out.println("Attendance: " + attendancePercentage + "%");
        System.out.println("Grades:");
        for (Map.Entry<String, Integer> entry : grades.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("Exam Eligibility: " + (isEligibleForExam() ? "Eligible" : "Not Eligible"));
    }
}

class Teacher extends Person {
    private List<String> subjects;
    private List<String> assignedClasses;

    public Teacher(String name, int age, String id) {
        super(name, age, id);
        this.subjects = new ArrayList<>();
        this.assignedClasses = new ArrayList<>();
    }

    public void addSubject(String subject) {
        subjects.add(subject);
    }

    public boolean assignClass(String className) {
        if (assignedClasses.size() < 3) {
            assignedClasses.add(className);
            return true;
        }
        return false; // Max 3 classes per teacher
    }

    public List<String> getAssignedClasses() {
        return assignedClasses;
    }
}

class School {
    private Map<String, Student> students;
    private Map<String, Teacher> teachers;
    private Map<String, List<Student>> classes;

    public School() {
        students = new HashMap<>();
        teachers = new HashMap<>();
        classes = new HashMap<>();
    }

    public void addStudent(String name, int age, String id, String className) {
        if (!classes.containsKey(className)) {
            classes.put(className, new ArrayList<>());
        }
        if (classes.get(className).size() < 40) {
            Student student = new Student(name, age, id, className);
            students.put(id, student);
            classes.get(className).add(student);
        } else {
            System.out.println("Class " + className + " is full!");
        }
    }

    public void addTeacher(String name, int age, String id) {
        Teacher teacher = new Teacher(name, age, id);
        teachers.put(id, teacher);
    }

    public void assignTeacherToClass(String teacherId, String className) {
        Teacher teacher = teachers.get(teacherId);
        if (teacher != null) {
            if (teacher.assignClass(className)) {
                System.out.println("Teacher " + teacher.getName() + " assigned to class " + className);
            } else {
                System.out.println("Teacher " + teacher.getName() + " cannot handle more than 3 classes.");
            }
        }
    }

    public void assignGrade(String studentId, String subject, int grade) {
        Student student = students.get(studentId);
        if (student != null) {
            student.addGrade(subject, grade);
        }
    }

    public void setAttendance(String studentId, int percentage) {
        Student student = students.get(studentId);
        if (student != null) {
            student.setAttendance(percentage);
        }
    }

    public void generateReport(String studentId) {
        Student student = students.get(studentId);
        if (student != null) {
            student.printReport();
        } else {
            System.out.println("Student not found!");
        }
    }
}

public class SchoolManagementSystem {
    public static void main(String[] args) {
        School school = new School();

        // Adding students
        school.addStudent(" koustubh", 17, "S101", "Class A");
        school.addStudent("Pritam", 17, "S102", "Class A");

        // Adding teachers
        school.addTeacher("Mr.Dhanraj", 29, "T201");
        school.addTeacher("Ms.Shivaraj", 26, "T202");

        // Assign teachers to classes
        school.assignTeacherToClass("T201", "Class A");
        school.assignTeacherToClass("T202", "Class B");

        // Assign grades
        school.assignGrade("S101", "Math", 90);
        school.assignGrade("S101", "Science", 85);
        school.assignGrade("S102", "Math", 80);

        // Set attendance
        school.setAttendance("S101", 80);
        school.setAttendance("S102", 70);

        // Generate student reports
        school.generateReport("S101");
        school.generateReport("S102");
    }
}