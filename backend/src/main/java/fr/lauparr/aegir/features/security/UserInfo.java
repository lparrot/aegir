package fr.lauparr.aegir.features.security;

public interface UserInfo {
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

  }
}
