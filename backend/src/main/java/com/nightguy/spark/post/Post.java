package com.nightguy.spark.post;

import com.nightguy.spark.comment.Comment;
import com.nightguy.spark.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "posts")
@Getter
@Setter
@SQLDelete(sql = "UPDATE posts SET deletion_timestamp = NOW() WHERE id = ?")
@SQLRestriction("deletion_timestamp IS NULL")
public class Post {
  @PrePersist
  public void addTimestamp() {
    creationTimestamp = Instant.now();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull private Instant creationTimestamp;
  private Instant deletionTimestamp;
  @NotBlank private String textContent;
  @NotNull private Integer likeCount = 0;

  @Column(columnDefinition = "TEXT")
  private String imageLink;

  @ManyToOne
  @JoinColumn(
      name = "user_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "posts_user_id_fkey"))
  private User author;

  @OneToMany(mappedBy = "post")
  private Set<Comment> comments;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Post post = (Post) o;
    return Objects.equals(id, post.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
