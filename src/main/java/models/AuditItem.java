package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "audits")
public class AuditItem {

    @Id
    @Column(name = "audit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "audit_user_id", nullable = false)
    private User user;

    @Column(name = "audit_action")
    private AuditAction action;

    @Column(name = "audit_entity_name")
    private String entityName;

    @Column(name = "audit_before_content")
    @Lob
    private String beforeContent;

    @Column(name = "audit_after_content")
    @Lob
    private String afterContent;

    @Column(name = "audit_date")
    private LocalDateTime localDateTime;


    @PrePersist
    public void log() {
        localDateTime = LocalDateTime.now();
    }


    public enum AuditAction {
        CREATED {
            @Override
            public String toString() {
                return "created";
            }
        },
        UPDATED {
            @Override
            public String toString() {
                return "updated";
            }
        },
        DELETED {
            @Override
            public String toString() {
                return "deleted";
            }
        };


    }
}
