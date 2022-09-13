package fr.lauparr.aegir.projections;

import org.springframework.beans.factory.annotation.Value;

public interface UserInfo_Simple {
  Long getId();

  String getUsername();

  @Value("#{target.userData?.getFullname()}")
  String getFullname();
}
