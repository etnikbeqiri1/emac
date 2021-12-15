package models;

public enum Module {

    LOGIN("Login" , "/views/login_screen.fxml"),
    MAIN_SCREEN("EMAC" , "/views/main_screen.fxml"),
    ADMIN_DASHBOARD("Admin Dashboard" , "/views/admin_dashboard.fxml"),
    SUBJECT("Subject" , "/views/subject_screen.fxml"),
    ADD_SUBJECT("Add Subject" , "/views/add_subject_screen.fxml"),
    EDIT_SUBJECT("Edit Subject","/views/edit_subject_screen.fxml"),
    USER("Users","/views/users_screen.fxml"),
    ADD_USER("Add User","/views/add_user_screen.fxml"),
    EDIT_USER("Edit User","/views/edit_user_screen.fxml"),
    USER_DASHBOARD("User Dashboard", "/views/user_dashboard.fxml"),
    GROUP("Groups", "/views/group_screen.fxml"),
    ADD_GROUP("Add Group", "/views/add_group_screen.fxml"),
    ADD_STUDENT("Add Student", "/views/add_student_screen.fxml"),
    STUDENTS("Students", "/views/students_screen.fxml"),
    EDIT_GROUP("Edit Group", "/views/edit_group_screen.fxml"),
    EDIT_STUDENT("Edit Student", "/views/edit_student_screen.fxml"),
    EXAMS("Exams", "/views/exams_screen.fxml"),
    ADD_EXAM("Add Exam", "/views/add_exam_screen.fxml"),
    EDIT_EXAM("Edit Exam", "/views/edit_exam_screen.fxml"),
    QUESTION("Questions" , "/views/question_screen.fxml"),
    ADD_OPEN_QUESTION("Add Open Question", "/views/add_open_question_screen.fxml"),
    ADD_OPTION_QUESTION("Add Option Question", "/views/add_option_question_screen.fxml"),
    GENERATE_EXAMS("Generate Exams", "/views/generate_exam_screen.fxml"),
    EDIT_OPEN_QUESTION("Edit Open Question", "/views/edit_open_question_screen.fxml"),
    EDIT_OPTION_QUESTION("Edit Option Question", "/views/edit_option_question_screen.fxml");

    private String title;
    private String resource;

    Module(String title,String resource) {
        this.title = title;
        this.resource = resource;
    }

    public String getTitle() {
        return title;
    }

    public String getResource() {
        return resource;
    }
}
