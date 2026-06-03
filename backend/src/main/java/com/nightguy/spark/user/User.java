package com.nightguy.spark.user;

import com.nightguy.spark.comment.Comment;
import com.nightguy.spark.post.Post;
import com.nightguy.spark.reaction.Reaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(unique = true, length = 16)
  private String username;

  @NotNull private String password;

  @Column(columnDefinition = "TEXT")
  private String profilePictureLink;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
  private Set<Post> posts;

  @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
  private Set<Comment> comments;

  @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
  private Set<Reaction> reactions;

  public User(Role role, String password, String username) {
    this.role = role;
    this.password = password;
    this.username = username;
  }

  @Override
  @NonNull
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  @NonNull
  public String getUsername() {
    return username;
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", username='" + username + '\'' + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
