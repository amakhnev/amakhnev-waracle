package com.waracle.cakemgr.entity;

import com.google.gson.annotations.SerializedName;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Cakes")
public class Cake implements Serializable {

    @Serial
    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "TITLE", unique = true, nullable = false, length = 100)
    @NotNull(message = "Title must not be null")
    @Size(min = 1, max = 100, message = "Title must between {min} and {max} characters")
    private String title;

    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    @SerializedName("desc")
    @NotNull(message = "Description must not be null")
    @Size(min = 1, max = 100, message = "Description must between {min} and {max} characters")
    private String description;

    @Column(name = "IMAGE", nullable = false, length = 300)
    @NotNull(message = "Image URL must not be null")
    @Size(min = 1, max = 300, message = "Image URL must between {min} and {max} characters")
    private String image;

    public Cake() {
    }
    public Cake(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cake cake = (Cake) o;
        return Objects.equals(title, cake.title) &&
                Objects.equals(description, cake.description) &&
                Objects.equals(image, cake.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, image);
    }
}