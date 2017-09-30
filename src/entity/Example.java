package entity;


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by OUSSAMA EZZIOURI <oussama.ezziouri@gmail.com>
 */
@Entity
@Table(name = "example")
@AttributeOverrides({  
    @AttributeOverride(name="id", column=@Column(name="id"))
    
})
public class Example extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "example_id_seq")
    @SequenceGenerator(name = "example_id_seq", sequenceName = "example_id_seq", allocationSize = 1)
    private Integer id;
    
    @Column(name = "name")
    private String name;

    public Example() {
    }

    public Example(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
    

}
