package fr.lauparr.aegir.features.shared.services;

import com.github.javafaker.Faker;
import fr.lauparr.aegir.entities.*;
import fr.lauparr.aegir.features.security.AuthSrv;
import fr.lauparr.aegir.features.security.ParamsSecurityCreateAccount;
import fr.lauparr.aegir.features.websockets.WebsocketSrv;
import fr.lauparr.aegir.repositories.ProfileRepository;
import fr.lauparr.aegir.repositories.UserDataRepository;
import fr.lauparr.aegir.repositories.UserRepository;
import fr.lauparr.aegir.repositories.WorkspaceRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
  @Autowired
  private WorkspaceRepository workspaceRepository;

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

      TaskStatus statutsSoleilTodo = new TaskStatus().setName("To Do").setColor("green");
      TaskStatus statutsSoleilInProgress = new TaskStatus().setName("In Progress").setColor("blue");
      TaskStatus statutsSoleilToTest = new TaskStatus().setName("To Test").setColor("orange");

      TaskStatus statutsCcsTodo = new TaskStatus().setName("To Do").setColor("green");
      TaskStatus statutsCcsInProgress = new TaskStatus().setName("In Progress").setColor("blue");
      TaskStatus statutsCcsToTest = new TaskStatus().setName("To Test").setColor("orange");

      final Workspace workspaceSoleil = new Workspace().setName("Soleil");
      final Workspace workspaceCcs = new Workspace().setName("CCS");

      workspaceSoleil
        .addFolder(new Folder().setName("VABF").setWorkspace(workspaceSoleil)
          .addBoard(new Board().setName("Module solde"))
          .addBoard(new Board().setName("Module dossier formation"))
        )
        .addFolder(new Folder().setName("DEV").setWorkspace(workspaceSoleil)
          .addBoard(new Board().setName("Bugs")
          )
        )
        .addStatus(statutsSoleilTodo)
        .addStatus(statutsSoleilInProgress)
        .addStatus(statutsSoleilToTest);

      workspaceCcs
        .addFolder(new Folder().setName("PROD").setWorkspace(workspaceCcs)
          .addBoard(new Board().setName("Bugs")
            .addTask(new Task().setName("Corriger le problème de mot de passe lors de la connexion").setStatus(statutsCcsInProgress).addComment(new TaskComment().setContent("Correction en cours ...")))
            .addTask(new Task().setName("Modifier le libellé du bouton de création d'un profil").setStatus(statutsCcsTodo).setAssigned(root).setAssignedAt(LocalDateTime.now()).addComment(new TaskComment().setContent("Modification mineure, n'est pas prioritaire")))
          )
        )
        .addFolder(new Folder().setName("DEV").setWorkspace(workspaceCcs)
          .addBoard(new Board().setName("Bugs")
            .addTask(new Task().setName("Corriger le commentaire sur la méthode getUserByTaskId()").setStatus(statutsCcsInProgress))
          )
          .addBoard(new Board().setName("Evolutions")
            .addTask(new Task().setName("Création de l'écran d'administration des profils").setAssigned(root).setAssignedAt(LocalDateTime.now()).setStatus(statutsCcsTodo))
            .addTask(new Task().setName("Création de l'écran de tableau de bord").setStatus(statutsCcsTodo)
              .addComment(new TaskComment().setContent("Attention, doit être réalisé par une équipe encadrée par un lead dev"))
            )
          )
        )
        .addStatus(statutsCcsTodo)
        .addStatus(statutsCcsInProgress)
        .addStatus(statutsCcsToTest);

      workspaceRepository.saveAll(Arrays.asList(workspaceSoleil, workspaceCcs));

      root.addWorkspace(workspaceSoleil).addWorkspace(workspaceCcs);

      workspaceSoleil.addMember(new Member().setUser(root).setRole(Role.ADMIN));
      workspaceCcs.addMember(new Member().setUser(root).setRole(Role.ADMIN));

      userRepository.save(root);

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

        workspaceSoleil.addMember(new Member().setUser(randomUser).setRole(Role.ADMIN));
        workspaceCcs.addMember(new Member().setUser(randomUser).setRole(Role.ADMIN));

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
