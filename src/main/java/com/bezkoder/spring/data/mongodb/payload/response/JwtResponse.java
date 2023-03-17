package com.bezkoder.spring.data.mongodb.payload.response;

import com.bezkoder.spring.data.mongodb.model.Role;
import java.util.Set;

public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;

  private Set<Role> roles;

  public JwtResponse(String accessToken, Long id, Set<Role> roles) {
    this.token = accessToken;
    this.id = id;
    this.roles = roles;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

}
