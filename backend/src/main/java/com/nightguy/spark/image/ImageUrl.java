package com.nightguy.spark.image;

import com.nightguy.spark.post.Post;
import com.nightguy.spark.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "image_urls")
@Getter
@Setter
public class ImageUrl {

  @PrePersist
  public void addTimestamp() {
    creationTimestamp = Instant.now();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull private Instant creationTimestamp;

  @NotNull @Column(unique = true) private UUID publicId;

  @Column(columnDefinition = "TEXT", unique = true)
  private String imageLink;

  @ManyToOne
  @JoinColumn(
          name = "owner_id",
          nullable = false,
          foreignKey = @ForeignKey(name = "image_urls_user_id_fkey"))
  private User owner;

  @OneToOne
  @JoinColumn(
      name = "post_id",
      unique = true,
      foreignKey = @ForeignKey(name = "image_urls_post_id_fkey"))
  private Post post;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    ImageUrl imageUrl = (ImageUrl) o;
    return Objects.equals(id, imageUrl.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
