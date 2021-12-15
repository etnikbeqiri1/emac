package models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "student_group")
public class Group {
    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "group_name")
    private String name;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "group_professor",nullable = false)
    private User professor;

    @OneToMany(mappedBy = "group", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Student> studentList = new ArrayList<Student>();

    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Group)
            return ((Group) obj).getId() == this.getId();
        return false;

    }
}
