package fr.lauparr.aegir.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Function;

@Getter
@Setter
public class GroupByMapper<T, U> {
  private T master;
  private List<U> details;

  public GroupByMapper(T master, List<U> details) {
    this.master = master;
    this.details = details;
  }

  public <V, W> GroupByMapper<V, W> convert(Function<T, V> functionConverMaster, Function<List<U>, List<W>> functionConverDetails) {
    V masterConverted = functionConverMaster.apply(this.master);
    List<W> detailsConverted = functionConverDetails.apply(this.details);
    return new GroupByMapper<>(masterConverted, detailsConverted);
  }
}
