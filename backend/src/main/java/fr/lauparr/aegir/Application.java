package fr.lauparr.aegir;

import fr.lauparr.aegir.features.shared.services.DbInitializerSrv;
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
public class Application implements CommandLineRunner {

  private static ConfigurableApplicationContext context;

  @Autowired
  private DbInitializerSrv dbInitializerSrv;

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
  }
}
