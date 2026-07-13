package ai.pnu.b03.minifarm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "users", indexes = {
        @Index(name = "uk_users_email", columnList = "email", unique = true)
})
public class User {

    public User() {}

    @Id
    private String id;

    private String username;

    private String password;

    private String email;

    private String role; // ROLE_USER, ROLE_ADMIN

    private String provider;

    private String providerId;

    @CreationTimestamp
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID().toString();
    }
}