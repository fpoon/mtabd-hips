package pl.dawidsowa.mtabd.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the DictionaryValue entity.
 */
public class DictionaryValueDTO implements Serializable {

    private Long id;

    private String name;

    private String longName;

    private Long translationId;

    private Long dictionaryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public Long getTranslationId() {
        return translationId;
    }

    public void setTranslationId(Long translationId) {
        this.translationId = translationId;
    }

    public Long getDictionaryId() {
        return dictionaryId;
    }

    public void setDictionaryId(Long dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DictionaryValueDTO dictionaryValueDTO = (DictionaryValueDTO) o;
        if(dictionaryValueDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dictionaryValueDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DictionaryValueDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", longName='" + getLongName() + "'" +
            "}";
    }
}
