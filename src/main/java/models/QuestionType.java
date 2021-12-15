package models;

public enum QuestionType {
    OPEN_QUESTION("Open Question"),
    CLOSED_QUESTION("Option Question");

    private String name;

    QuestionType(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
