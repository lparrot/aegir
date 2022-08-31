package fr.lauparr.aegir.features.security;

import org.springframework.beans.factory.annotation.Value;

public interface UserInfo_Security {
  Long getId();

  String getUsername();

  UserDataInfo getUserData();

  interface UserDataInfo {

    String getEmail();

    String getFirstname();

    String getLastname();

    String getAddress();

    String getCity();

    String getPostalCode();

    String getAbout();

    @Value("#{target.getFullname()}")
    String getFullname();

  }
}
