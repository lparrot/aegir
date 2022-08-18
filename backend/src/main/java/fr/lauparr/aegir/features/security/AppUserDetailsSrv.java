package fr.lauparr.aegir.features.security;

import fr.lauparr.aegir.entities.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsSrv implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  @Cacheable(value = "userTokenCache", key = "#username")
  public UserDetails loadUserByUsername(final String username) {
    return this.userRepository.findFirstByUsername(username).orElseThrow(() -> new AccessDeniedException("Mauvais login ou mot de passe"));
  }

}
