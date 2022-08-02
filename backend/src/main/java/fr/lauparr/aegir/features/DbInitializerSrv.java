package fr.lauparr.aegir.features;

import com.github.javafaker.Faker;
import fr.lauparr.aegir.entities.*;
import fr.lauparr.aegir.entities.repositories.ProfileRepository;
import fr.lauparr.aegir.entities.repositories.UserDataRepository;
import fr.lauparr.aegir.entities.repositories.UserRepository;
import fr.lauparr.aegir.enums.EnumProjectItemType;
import fr.lauparr.aegir.features.security.AuthSrv;
import fr.lauparr.aegir.features.security.ParamsSecurityCreateAccount;
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

  @Transactional
  public void initialize() {
    if (userRepository.count() <= 0) {

      Profile defaultProfile = Profile.builder().label("Invited").role(Role.USER).defaultProfile(true).build();
      Profile adminProfile = Profile.builder().label("Administrator").role(Role.ADMIN).role(Role.USER).defaultProfile(false).build();

      profileRepository.saveAll(Arrays.asList(defaultProfile, adminProfile));

      UserData userData = UserData.builder()
        .email("laurent.parrot78@gmail.com")
        .lastname("Parrot")
        .firstname("Laurent")
        .address(faker.address().streetAddress())
        .city(faker.address().city())
        .postalCode(faker.address().zipCode())
        .about("Administrateur du système d'informations AEGIR")
        .build();

      // Create users
      User root = authSrv.createAccount(userData, ParamsSecurityCreateAccount.builder()
        .username("root")
        .password("123")
        .build());

      root.setProfile(adminProfile);

      authenticate(root);

      final ProjectItem workspaceTaches = ProjectItem.builder().type(EnumProjectItemType.WORKSPACE).name("Taches").build();
      final ProjectItem folderVabf = ProjectItem.builder().type(EnumProjectItemType.FOLDER).name("VABF").build();
      final ProjectItem viewModuleSolde = ProjectItem.builder().type(EnumProjectItemType.VIEW).name("Module solde").build();
      final ProjectItem viewModuleDossierFormation = ProjectItem.builder().type(EnumProjectItemType.VIEW).name("Module dossier formation").build();
      final ProjectItem viewDev = ProjectItem.builder().type(EnumProjectItemType.VIEW).name("DEV").build();
      final ProjectItem workspaceBugs = ProjectItem.builder().type(EnumProjectItemType.WORKSPACE).name("Bugs").build();

      Project projectSoleil = Project.builder().name("Soleil").build().addProjectItem(workspaceTaches).addProjectItem(workspaceBugs);
      workspaceTaches.addChild(folderVabf).addChild(viewDev);
      folderVabf.addChild(viewModuleSolde).addChild(viewModuleDossierFormation);

      root.addProject(projectSoleil);

      ProjectItem workspaceProd = ProjectItem.builder().type(EnumProjectItemType.WORKSPACE).name("PROD").build();
      ProjectItem workspaceDev = ProjectItem.builder().type(EnumProjectItemType.WORKSPACE).name("DEV").build();
      ProjectItem viewProdBugs = ProjectItem.builder().type(EnumProjectItemType.VIEW).name("Bugs").build();
      ProjectItem viewDevBugs = ProjectItem.builder().type(EnumProjectItemType.VIEW).name("Bugs").build();
      ProjectItem viewDevEvol = ProjectItem.builder().type(EnumProjectItemType.VIEW).name("Evolutions").build();

      Project projectCcs = Project.builder().name("CCS").build().addProjectItem(workspaceProd).addProjectItem(workspaceDev);
      workspaceProd.addChild(viewProdBugs);
      workspaceDev.addChild(viewDevBugs).addChild(viewDevEvol);

      root.addProject(projectCcs);

      userDataRepository.save(userData);

      UserData randomUserData = null;
      User randomUser = null;

      for (int i = 0; i < 20; i++) {
        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();
        String username = StringUtils.stripAccents(firstname.toLowerCase() + "." + lastname.toLowerCase());

        randomUserData = UserData.builder()
          .email(faker.internet().emailAddress(username))
          .lastname(lastname)
          .firstname(firstname)
          .address(faker.address().streetAddress())
          .city(faker.address().city())
          .postalCode(faker.address().zipCode())
          .about(faker.superhero().descriptor())
          .build();

        // Create users
        randomUser = authSrv.createAccount(randomUserData, ParamsSecurityCreateAccount.builder()
          .username(username)
          .password("123")
          .build());

        randomUser.setProfile(adminProfile);

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
