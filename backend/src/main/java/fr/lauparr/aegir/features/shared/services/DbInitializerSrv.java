package fr.lauparr.aegir.features.shared.services;

import com.github.javafaker.Faker;
import fr.lauparr.aegir.entities.*;
import fr.lauparr.aegir.entities.repositories.ProfileRepository;
import fr.lauparr.aegir.entities.repositories.UserDataRepository;
import fr.lauparr.aegir.entities.repositories.UserRepository;
import fr.lauparr.aegir.enums.EnumProjectItemType;
import fr.lauparr.aegir.features.security.AuthSrv;
import fr.lauparr.aegir.features.security.ParamsSecurityCreateAccount;
import fr.lauparr.aegir.features.websockets.WebsocketSrv;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;

@Service
public class DbInitializerSrv {

  @Autowired
  private Faker faker;
  @Autowired
  private AuthSrv authSrv;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserDataRepository userDataRepository;
  @Autowired
  private ProfileRepository profileRepository;
  @Autowired
  private WebsocketSrv websocketSrv;

  @Transactional
  public void initialize() {
    if (userRepository.count() <= 0) {

      Profile defaultProfile = new Profile().setLabel("Invited").setDefaultProfile(true).addRole(Role.USER);
      Profile adminProfile = new Profile().setLabel("Administrator").setDefaultProfile(false).addRole(Role.ADMIN).addRole(Role.USER);

      profileRepository.saveAll(Arrays.asList(defaultProfile, adminProfile));

      UserData userData = new UserData()
        .setEmail("laurent.parrot78@gmail.com")
        .setLastname("Parrot")
        .setFirstname("Laurent")
        .setAddress(faker.address().streetAddress())
        .setCity(faker.address().city())
        .setPostalCode(faker.address().zipCode())
        .setAbout("Administrateur du système d'informations AEGIR");

      // Create users
      User root = authSrv.createAccount(userData, new ParamsSecurityCreateAccount()
          .setUsername("root")
          .setPassword("123"))
        .setProfile(adminProfile);

      authenticate(root);

      TaskStatus statutsSoleilTachesTodo = new TaskStatus().setName("To Do");
      TaskStatus statutsSoleilTachesInProgress = new TaskStatus().setName("In Progress");

      TaskStatus statutsSoleilBugsTodo = new TaskStatus().setName("To Do");
      TaskStatus statutsSoleilBugsInProgress = new TaskStatus().setName("In Progress");
      TaskStatus statutsSoleilBugsToTest = new TaskStatus().setName("To Test");

      TaskStatus statutsCcsProdTodo = new TaskStatus().setName("To Do");
      TaskStatus statutsCcsProdInProgress = new TaskStatus().setName("In Progress");

      TaskStatus statutsCcsDevTodo = new TaskStatus().setName("To Do");
      TaskStatus statutsCcsDevInProgress = new TaskStatus().setName("In Progress");
      TaskStatus statutsCcsDevToTest = new TaskStatus().setName("To Test");

      root
        .addProject(new Project().setName("Soleil")
          .addProjectItem(new ProjectItem().setType(EnumProjectItemType.WORKSPACE).setName("Taches")
            .addStatus(statutsSoleilTachesTodo)
            .addStatus(statutsSoleilTachesInProgress)
            .addChild(new ProjectItem().setType(EnumProjectItemType.FOLDER).setName("VABF")
              .addChild(new ProjectItem().setType(EnumProjectItemType.VIEW).setName("Module solde"))
              .addChild(new ProjectItem().setType(EnumProjectItemType.VIEW).setName("Module dossier formation")))
            .addChild(new ProjectItem().setType(EnumProjectItemType.VIEW).setName("DEV")))

          .addProjectItem(new ProjectItem().setType(EnumProjectItemType.WORKSPACE).setName("Bugs")
            .addStatus(statutsSoleilBugsTodo)
            .addStatus(statutsSoleilBugsInProgress)
            .addStatus(statutsSoleilBugsToTest)
          ));

      root
        .addProject(new Project().setName("CCS")
          .addProjectItem(new ProjectItem().setType(EnumProjectItemType.WORKSPACE).setName("PROD")
            .addStatus(statutsCcsProdTodo)
            .addStatus(statutsCcsProdInProgress)
            .addChild(new ProjectItem().setType(EnumProjectItemType.VIEW).setName("Bugs")
              .addTask(new Task().setName("Corriger le problème de mot de passe lors de la connexion").setStatus(statutsCcsProdInProgress).addComment(new TaskComment().setContent("Correction en cours ...")))
              .addTask(new Task().setName("Modifier le libellé du bouton de création d'un profil").setStatus(statutsCcsProdTodo).addComment(new TaskComment().setContent("Modification mineure, n'est pas prioritaire")))))

          .addProjectItem(new ProjectItem().setType(EnumProjectItemType.WORKSPACE).setName("DEV")
            .addStatus(statutsCcsDevTodo)
            .addStatus(statutsCcsDevInProgress)
            .addStatus(statutsCcsDevToTest)
            .addChild(new ProjectItem().setType(EnumProjectItemType.VIEW).setName("Bugs")
              .addTask(new Task().setName("Corriger le commentaire sur la méthode getUserByTaskId()").setStatus(statutsCcsDevInProgress)))
            .addChild(new ProjectItem().setType(EnumProjectItemType.VIEW).setName("Evolutions")
              .addTask(new Task().setName("Création de l'écran d'administration des profils").setStatus(statutsCcsDevTodo))
              .addTask(new Task().setName("Création de l'écran de tableau de bord").setStatus(statutsCcsDevTodo)
                .addComment(new TaskComment().setContent("Attention, doit être réalisé par une équipe encadrée par un lead dev"))
              ))));

      userDataRepository.save(userData);

      UserData randomUserData;
      User randomUser;

      for (int i = 0; i < 20; i++) {
        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();
        String username = StringUtils.stripAccents(firstname.toLowerCase() + "." + lastname.toLowerCase());

        randomUserData = new UserData()
          .setEmail(faker.internet().emailAddress(username))
          .setLastname(lastname)
          .setFirstname(firstname)
          .setAddress(faker.address().streetAddress())
          .setCity(faker.address().city())
          .setPostalCode(faker.address().zipCode())
          .setAbout(faker.superhero().descriptor());

        // Create users
        randomUser = authSrv.createAccount(randomUserData, new ParamsSecurityCreateAccount()
            .setUsername(username)
            .setPassword("123"))
          .setProfile(adminProfile);

        userRepository.save(randomUser);
      }
    }
  }

  public void authenticate(final User user) {
    SecurityContextHolder.getContext().setAuthentication(new Authentication() {
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
      }

      @Override
      public Object getCredentials() {
        return user.getPassword();
      }

      @Override
      public Object getDetails() {
        return user;
      }

      @Override
      public Object getPrincipal() {
        return user;
      }

      @Override
      public boolean isAuthenticated() {
        return true;
      }

      @Override
      public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {

      }

      @Override
      public String getName() {
        return user.getUsername();
      }
    });
  }
}
