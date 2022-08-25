package fr.lauparr.aegir.features.user;

import fr.lauparr.aegir.entities.User;
import fr.lauparr.aegir.entities.repositories.UserRepository;
import fr.lauparr.aegir.exceptions.MessageException;
import fr.lauparr.aegir.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSrv {

  @Autowired
  private UserRepository userRepository;

  public User getUserById(Long userId) {
    return userRepository.findById(userId).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.user")));
  }

  public List<User> getUsers() {
    return userRepository.findAll();
  }
}
