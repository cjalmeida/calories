package tt.calories.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import tt.calories.security.PasswordHasher;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Represents an application user
 *
 * @author Cloves Almeida
 * @version 1.0.0
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Length(min=3)
    private String fullName;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    private Boolean active = true;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles = new LinkedList<>();

    private Integer caloriesPerDay;

    @NotNull
    @Length(min=3)
    private String userTimezone = "UTC";


    /**
     * Random 10-char string to use as salt.
     */
    @Column(length = 10)
    private String salt = UUID.randomUUID().toString().substring(0, 10);


    public User() {

    }

    /**
     * Convenience constructor.
     */
    public User(String fullName, String email, String plainPassword) {
        this.fullName = fullName;
        this.email = email;
        this.setPlainPassword(plainPassword);
    }

    @Transient
    @JsonIgnore
    public List<String> getRolesString() {
        return this.roles.stream().map(Role::getName).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Meal)) return false;
        User x = (User) o;
        return new EqualsBuilder()
            .append(x.email, x.email)
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(email)
            .toHashCode();
    }

    /**
     * Use this method to update the password. It's hashed before persistence.
     */
    @Transient
    @JsonProperty
    public void setPlainPassword(String plainPassword) {
        this.password = PasswordHasher.hash(plainPassword, salt);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public void setCaloriesPerDay(Integer caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }

    public String getUserTimezone() {
        return userTimezone;
    }

    public void setUserTimezone(String userTimezone) {
        this.userTimezone = userTimezone;
    }

    @JsonIgnore
    public String getSalt() {
        return salt;
    }

    @JsonIgnore
    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
