package rw.ac.rca.spring_boot_template.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import rw.ac.rca.spring_boot_template.enumerations.EGender;
import rw.ac.rca.spring_boot_template.enumerations.EStatus;
import rw.ac.rca.spring_boot_template.utils.ExpirationTokenUtils;

import java.util.Date;
import java.util.Set;
import java.util.UUID;


@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"}), @UniqueConstraint(columnNames = {"phone_number"})})
@OnDelete(action = OnDeleteAction.CASCADE)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    // primary key
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    // personal details
    private String username;
    private String firstName;
    private String lastName;
    @Transient
    private String fullName;
    private String nationalId;
    private Date dateOfBirth;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column (name = "email")
    private String email;
    @Enumerated(EnumType.STRING)
    private EGender gender;

    // Time Stamp details
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    // authentication details
    @Column(name = "activation_code")
    private String activationCode;
    @JsonIgnore
    @Column(name = "password" , nullable = true)
    private String password;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EStatus status = EStatus.WAIT_EMAIL_VERIFICATION;
    @Column(name = "verified")
    private boolean verified;
    @Column
    private String expirationToken = ExpirationTokenUtils.generateToken();

    // Relationships details


    // Define the many-to-many relationship with roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;


    public User(String username, String phoneNumber, String email, EGender gender, String password, EStatus status, boolean verified , String activationCode) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.password = password;
        this.status = status;
        this.verified = verified;
        this.activationCode =  activationCode;
    }

}
