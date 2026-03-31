package com.distributedstorage.backend.model;
import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
private List<FileMetadata> files;

    public User() {}

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public List<FileMetadata> getFiles() { return files; }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<FileMetadata> getFiles() {
        return files;
    }
    public void setPassword(String password) {
    this.password = password;
}
}