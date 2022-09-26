package fr.lauparr.aegir.interceptors;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.P6Logger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class P6LoggerImpl implements P6Logger {

  @Override
  public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql, String url) {
    if (StringUtils.isNotBlank(sql) && !(sql.contains("select * from") && sql.contains("where 1=0"))) {
      logText(sql);
    }
  }

  @Override
  public void logException(Exception e) {
    logText(e.getMessage());
  }

  @Override
  public void logText(String text) {
    log.debug(text);
  }

  @Override
  public boolean isCategoryEnabled(Category category) {
    return true;
  }
}
