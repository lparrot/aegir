package fr.lauparr.aegir.projections;

import org.springframework.beans.factory.annotation.Value;

public interface UserDataInfo_Simple {

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
