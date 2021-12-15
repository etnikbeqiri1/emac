package models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "subject")
public class Subject implements Serializable {

    @Id
    @Column(name = "subject_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "subject_name")
    private String name;

    @OneToMany(mappedBy = "subject", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<User> userList = new ArrayList<User>();

    @Override
    public String toString(){
        return name;
    }
}
