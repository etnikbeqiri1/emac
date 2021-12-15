package models;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "user_full_name")
    private String fullName;

    @Column(name = "user_role")
    private Role role;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="subject_id",nullable = true)
    private Subject subject;

    @OneToMany(mappedBy = "professor", orphanRemoval = true, cascade =  CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Group> groupList = new ArrayList<Group>();

    @OneToMany(mappedBy = "professor", orphanRemoval = true, cascade =  CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Exam> examList = new ArrayList<Exam>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<AuditItem> auditItemList = new ArrayList<AuditItem>();

    @Override
    public String toString(){
        return String.format("#%d @%s %s %s %s", id, username, fullName, role, subject);
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof User)
            return ((User) obj).getId() == this.getId();
        return false;
    }
}
