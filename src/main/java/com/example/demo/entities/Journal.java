package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "journal")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String content;
    private String status;
    private Boolean favorite;
    private String imageName;


    @Lob
    @Column(name = "imagedata",length = 1000)
    private byte[] imageData;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @JsonIgnore
    private AppUser appUser;



    @Override
    public String toString() {
        return "Journal [id=" + id + ", title=" + title + "]";
    }

}
