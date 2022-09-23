package fr.lauparr.aegir;

import fr.lauparr.aegir.features.shared.services.DbInitializerSrv;
import fr.lauparr.aegir.features.shared.services.db_request.DBRequestSrv;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class Application implements CommandLineRunner {

  private static ConfigurableApplicationContext context;

  @Autowired
  private DbInitializerSrv dbInitializerSrv;
  @Autowired
  private DBRequestSrv dbRequestSrv;

  public static void main(String[] args) {
    context = new SpringApplicationBuilder(Application.class)
      .bannerMode(Banner.Mode.OFF)
      .build(args)
      .run();
  }

  public static void restart() {
    if (context != null) {
      log.info("Server restarting ...");

      ApplicationArguments args = context.getBean(ApplicationArguments.class);
      Thread thread = new Thread(() -> {
        context.close();
        context = SpringApplication.run(Application.class, args.getSourceArgs());
      });

      thread.setDaemon(false);
      thread.start();
    } else {
      log.error("Application context cannot be recovered.");
    }
  }

  @Override
  public void run(String... args) {
    this.dbInitializerSrv.initialize();
//    dbRequestSrv.tuple(User.class)
//      .select("username", "userData.address")
//      .where("username", "like", "julie%")
//      .where("userData.postalCode", "=", "68085")
//      .list()
//      .forEach(
//        tuple -> System.out.println(tuple.get("userData.address"))
//      );
  }
}
