package com.nightguy.spark.comment;

import com.nightguy.spark.post.Post;
import com.nightguy.spark.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "comments")
@Getter
@Setter
@SQLDelete(sql = "UPDATE comments SET deletion_timestamp = NOW() WHERE id = ?")
@SQLRestriction("deletion_timestamp IS NULL")
public class Comment {
  @PrePersist
  public void addTimestamp() {
    creationTimestamp = Instant.now();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull private Instant creationTimestamp;
  private Instant deletionTimestamp;

  @Column(length = 200)
  @NotBlank
  private String textContent;

  @ManyToOne
  @JoinColumn(
      name = "user_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "comments_user_id_fkey"))
  private User author;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "post_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "comments_post_id_fkey"))
  private Post post;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Comment comment = (Comment) o;
    return Objects.equals(id, comment.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
