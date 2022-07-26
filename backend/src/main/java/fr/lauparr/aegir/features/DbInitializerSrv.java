package fr.lauparr.aegir.features;

import fr.lauparr.aegir.entities.Profile;
import fr.lauparr.aegir.entities.Role;
import fr.lauparr.aegir.entities.User;
import fr.lauparr.aegir.entities.repositories.ProfileRepository;
import fr.lauparr.aegir.entities.repositories.UserRepository;
import fr.lauparr.aegir.features.security.AuthSrv;
import fr.lauparr.aegir.features.security.ParamsSecurityCreateAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DbInitializerSrv {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private AuthSrv authSrv;
  @Autowired
  private ProfileRepository profileRepository;

  @Transactional
  public void initialize() {
    if (userRepository.count() <= 0) {

      Profile defaultProfile = Profile.builder().label("Invited").role(Role.USER).defaultProfile(true).build();

      profileRepository.save(defaultProfile);

      // Create users
      User root = authSrv.createAccount(ParamsSecurityCreateAccount.builder()
        .username("root")
        .password("123")
        .build());

      userRepository.save(root);
    }
  }

}
