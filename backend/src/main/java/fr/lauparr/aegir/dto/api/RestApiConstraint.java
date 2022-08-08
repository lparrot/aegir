package fr.lauparr.aegir.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.io.Serializable;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestApiConstraint implements Serializable {

  /**
   * Le champ (l'attribut) où se trouve l'erreur de validation
   */
  private String field;

  /**
   * Le message associé à l'erreur de validation
   */
  private String message;

  /**
   * La valeur actuelle de l'objet où il y a une erreur de validation
   */
  private transient Object value;

  /**
   * La classe de l'objet où il y a une erreur de validation
   */
  private Class clazz;

  /**
   * Constructeur
   */
  public RestApiConstraint(final ConstraintViolation<?> constraint) {
    setField(constraint.getPropertyPath().toString());
    setMessage(constraint.getMessage());
    setClazz(constraint.getRootBeanClass());
    setValue(constraint.getInvalidValue());
  }

  /**
   * Constructeur
   */
  public RestApiConstraint(final ObjectError error) {
    if (error instanceof FieldError) {
      FieldError fieldError = (FieldError) error;
      setField(fieldError.getField());
      setMessage(fieldError.getDefaultMessage());
      setValue(fieldError.getRejectedValue());
    }
  }

}
