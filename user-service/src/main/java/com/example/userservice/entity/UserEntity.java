package com.example.userservice.entity;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length= 50, unique = true)
    private String email;
    @Column(nullable = false, length= 50)
    private String name;
    @Column(nullable = true, unique = true)
    private String userId;
    @Column(nullable = true, unique = true)
    private String encryptedPwd;

    @Column(nullable = false)
    private String pwd;

//    @Column(nullable = false, updatable = false, insertable = false)
//    @ColumnDefault(value = "CURRENT_TIMESTAMP")
//    private Date createdAt;
//
//    @Column(nullable = false, updatable = true, insertable = false)
//    @ColumnDefault(value = "CURRENT_TIMESTAMP")
//    private Date modifiedAt;
}