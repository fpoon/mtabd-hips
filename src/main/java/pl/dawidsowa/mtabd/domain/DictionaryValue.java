package pl.dawidsowa.mtabd.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DictionaryValue.
 */
@Entity
@Table(name = "dictionary_value")
public class DictionaryValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "long_name")
    private String longName;

    @OneToOne
    @JoinColumn(unique = true)
    private Translation translation;

    @OneToOne
    @JoinColumn(unique = true)
    private Dictionary dictionary;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public DictionaryValue name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongName() {
        return longName;
    }

    public DictionaryValue longName(String longName) {
        this.longName = longName;
        return this;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public Translation getTranslation() {
        return translation;
    }

    public DictionaryValue translation(Translation translation) {
        this.translation = translation;
        return this;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public DictionaryValue dictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
        return this;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DictionaryValue dictionaryValue = (DictionaryValue) o;
        if (dictionaryValue.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dictionaryValue.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DictionaryValue{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", longName='" + getLongName() + "'" +
            "}";
    }
}
