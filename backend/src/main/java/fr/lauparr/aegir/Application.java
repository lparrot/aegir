package fr.lauparr.aegir;

import de.vandermeer.asciitable.AsciiTable;
import fr.lauparr.aegir.entities.User;
import fr.lauparr.aegir.features.shared.DbInitializerSrv;
import fr.lauparr.aegir.features.shared.db_request.DBRequestSrv;
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

import javax.persistence.TupleElement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    AsciiTable table = new AsciiTable();
    List<String> table_headers = new ArrayList<>();
    List<Object[]> table_values = new ArrayList<>();
    dbRequestSrv.tuple(User.class)
      .select("username", "userData.postalCode")
      .where(query ->
        query
          .where("username", "<>", "root")
          .where("userData.postalCode", "like", "2%")
      )
      .orWhere("userData.postalCode", "like", "4%")
      .orderBy("username", "asc")
      .list()
      .forEach(
        tuple -> {
          if (table_headers.isEmpty()) {
            table_headers.addAll(tuple.getElements().stream().map(TupleElement::getAlias).collect(Collectors.toList()));
          }

          Object[] data = new Object[table_headers.size()];

          IntStream.range(0, tuple.getElements().size()).forEach(i -> data[i] = tuple.get(i));
          table_values.add(data);
        });

    table.setPadding(2);
    table.addRule();
    table.addRow(table_headers);
    table.addRule();
    table_values.forEach(data -> {
      table.addRow(data);
      table.addRule();
    });

    System.out.println(table.render());
  }
}
