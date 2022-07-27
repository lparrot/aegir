package fr.lauparr.aegir.features;

import fr.lauparr.aegir.entities.*;
import fr.lauparr.aegir.entities.repositories.ProfileRepository;
import fr.lauparr.aegir.entities.repositories.ProjectRepository;
import fr.lauparr.aegir.entities.repositories.UserRepository;
import fr.lauparr.aegir.enums.EnumProjectItemType;
import fr.lauparr.aegir.features.security.AuthSrv;
import fr.lauparr.aegir.features.security.ParamsSecurityCreateAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;

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
  @Autowired
  private ProjectRepository projectRepository;

  @Transactional
  public void initialize() {
    if (userRepository.count() <= 0) {

      Profile defaultProfile = Profile.builder().label("Invited").role(Role.USER).defaultProfile(true).build();
      Profile adminProfile = Profile.builder().label("Administrator").role(Role.ADMIN).role(Role.USER).defaultProfile(false).build();

      profileRepository.saveAll(Arrays.asList(defaultProfile, adminProfile));

      // Create users
      User root = authSrv.createAccount(ParamsSecurityCreateAccount.builder()
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

      Project project = Project.builder().name("Soleil").build().addProjectItem(workspaceTaches).addProjectItem(workspaceBugs);

      workspaceTaches.addChild(folderVabf).addChild(viewDev);

      folderVabf.addChild(viewModuleSolde).addChild(viewModuleDossierFormation);

      root.addProject(project);

      root = userRepository.save(root);
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
