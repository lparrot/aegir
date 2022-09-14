package fr.lauparr.aegir.features.security;

import fr.lauparr.aegir.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsSrv implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(final String username) {
    return this.userRepository.findFirstByUsername(username).orElseThrow(() -> new AccessDeniedException("Mauvais login ou mot de passe"));
  }

}
