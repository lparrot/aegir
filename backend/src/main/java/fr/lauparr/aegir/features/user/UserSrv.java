package fr.lauparr.aegir.features.user;

import fr.lauparr.aegir.entities.User;
import fr.lauparr.aegir.exceptions.MessageException;
import fr.lauparr.aegir.repositories.UserRepository;
import fr.lauparr.aegir.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserSrv {

  @Autowired
  private UserRepository userRepository;

  public User getUserById(Long userId) {
    return userRepository.findById(userId).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.user")));
  }

  public Page<User> getUsers(Pageable pagination) {
    return userRepository.findAll(pagination);
  }

  public void updateUser(Long userId, ParamsUserEdit params) {
    User user = userRepository.findById(userId).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.user")));

    user.setUsername(params.getUsername());
    user.getUserData()
      .setEmail(params.getEmail())
      .setLastname(params.getLastname())
      .setFirstname(params.getFirstname())
      .setAddress(params.getAddress())
      .setPostalCode(params.getPostalCode())
      .setCity(params.getCity())
      .setAbout(params.getAbout());

    userRepository.save(user);
  }
}
