package entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by OUSSAMA EZZIOURI <oussama.ezziouri@gmail.com>
 */
@Entity
@Table(name = "base_entity_permission", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name")})
@AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "id"))
    ,
    @AttributeOverride(name = "create_uid", column = @Column(name = "create_uid"))
    ,
    @AttributeOverride(name = "update_uid", column = @Column(name = "update_uid"))
    ,
    @AttributeOverride(name = "create_time", column = @Column(name = "create_time"))
    ,
    @AttributeOverride(name = "update_time", column = @Column(name = "update_time"))
})
public class BaseEntityPermission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entity_permission_id_seq")
    @SequenceGenerator(name = "entity_permission_id_seq", sequenceName = "entity_permission_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "entity")
    private String entity;

    @Column(name = "c")
    private short c;

    @Column(name = "r")
    private short r;

    @Column(name = "u")
    private short u;

    @Column(name = "d")
    private short d;

    @Column(name = "priority")
    private int priority;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "entityPermissions")
    private Set<BaseGroup> groups = new HashSet<BaseGroup>(0);

    public BaseEntityPermission() {
    }

    public BaseEntityPermission(String className, short _create_, short _read_, short _update_, short _delete_) {
        this.entity = className;
        this.c = _create_;
        this.r = _read_;
        this.u = _update_;
        this.d = _delete_;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public int canCreate() {
        return c;
    }

    public void setCreate(short _create_) {
        this.c = _create_;
    }

    public short canRead() {
        return r;
    }

    public void setRead(short _read_) {
        this.r = _read_;
    }

    public short canUpdate() {
        return u;
    }

    public void setUpdate(short _update_) {
        this.u = _update_;
    }

    public short canDelete() {
        return d;
    }

    public void setDelete(short _delete_) {
        this.d = _delete_;
    }

    public Set<BaseGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<BaseGroup> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return super.toString() + "BaseEntityPermission{id= " + id + ",name=" + name + ", entity=" + entity + ", c=" + c + ", r=" + r + ", u=" + u + ", d=" + d + ", priority=" + priority + ", groups=" + groups + '}';
    }

}
