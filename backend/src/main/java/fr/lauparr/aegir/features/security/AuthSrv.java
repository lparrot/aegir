package fr.lauparr.aegir.features.security;

import fr.lauparr.aegir.entities.Profile;
import fr.lauparr.aegir.entities.User;
import fr.lauparr.aegir.entities.UserData;
import fr.lauparr.aegir.entities.repositories.ProfileRepository;
import fr.lauparr.aegir.entities.repositories.UserDataRepository;
import fr.lauparr.aegir.entities.repositories.UserRepository;
import fr.lauparr.aegir.exceptions.MessageException;
import fr.lauparr.aegir.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class AuthSrv {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserDataRepository userDataRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private TokenSrv tokenSrv;
  @Autowired
  private ProfileRepository profileRepository;

  public JwtTokenDto login(final String username, final String password) {
    final User user = this.userRepository.findFirstByUsername(username).orElse(null);

    // Si aucun utilisateur n'a été trouvé à partir du login
    if (user == null) {
      throw MessageException.builder().message(MessageUtils.getMessage("error.auth.bad_credentials")).build();
    }

    // Si le mot de passe entré ne correspond pas à celui en base de données
    if (!this.passwordEncoder.matches(password, user.getPassword())) {
      throw MessageException.builder().message(MessageUtils.getMessage("error.auth.bad_credentials")).build();
    }

    final String token = this.tokenSrv.createToken(user);

    return JwtTokenDto.builder()
      .token(token)
      .claims(this.tokenSrv.getClaims(token))
      .build();
  }

  public User getUserData(final String name) {
    return this.userRepository.findFirstByUsername(name).orElseThrow(EntityNotFoundException::new);
  }

  public User createAccount(UserData userData, final ParamsSecurityCreateAccount params) {
    final Optional<User> user = this.userRepository.findFirstByUsername(params.getUsername());

    if (user.isPresent()) {
      throw MessageException.builder().message(MessageUtils.getMessage("error.auth.same_email")).build();
    }

    final Profile defaultProfile = this.profileRepository.findDefaultProfile();

    final User userToCreate = User.builder()
      .username(params.getUsername())
      .profile(defaultProfile)
      .build();

    userToCreate.setPassword(this.passwordEncoder.encode(params.getPassword()));

    userToCreate.setUserData(userData);

    this.userRepository.save(userToCreate);

    return userToCreate;
  }
}
