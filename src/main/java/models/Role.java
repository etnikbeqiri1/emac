package models;


import lombok.Getter;

import java.util.Arrays;
import java.util.List;
@Getter
public enum Role {
    ADMIN(Arrays.asList(Module.ADMIN_DASHBOARD,Module.SUBJECT,Module.USER)),
    USER(Arrays.asList(Module.USER_DASHBOARD,Module.GROUP,Module.STUDENTS,Module.EXAMS, Module.QUESTION));

    private List<Module> modules;

    Role(List<Module> modules){
        this.modules = modules;
    }

}
