/*

 * SysView.java
 * 
 * Copyright (c) 2017 OUSSAMA EZZIOURI <oussama.ezziouri@gmail.com>. 
 * 
 * This file is part of Expression program is undefined on line 7, column 40 in file:///E:/Programs/NetBeans%207.4/workspace/JITPOS_Master/JitPOS/nbproject/licenseheader.txt..
 * 
 * Expression program is undefined on line 9, column 19 in file:///E:/Programs/NetBeans%207.4/workspace/JITPOS_Master/JitPOS/nbproject/licenseheader.txt. is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Expression program is undefined on line 14, column 19 in file:///E:/Programs/NetBeans%207.4/workspace/JITPOS_Master/JitPOS/nbproject/licenseheader.txt. is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Expression program is undefined on line 20, column 30 in file:///E:/Programs/NetBeans%207.4/workspace/JITPOS_Master/JitPOS/nbproject/licenseheader.txt..  If not, see <http ://www.gnu.org/licenses/>.
 */
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
 *
 * @author OUSSAMA EZZIOURI <oussama.ezziouri@gmail.com>
 */
@Entity
@Table(name = "sys_view", uniqueConstraints = {
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
public class SysView extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "view_id_seq")
    @SequenceGenerator(name = "view_id_seq", sequenceName = "view_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "fxml_path") // eg : /ui/main/Main.fxml
    private String fxml_path;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "viewPermissions")
    private Set<SysGroup> groups = new HashSet<SysGroup>(0);

    public SysView() {
    }

    public SysView(String name, String view) {
        this.name = name;
        this.fxml_path = view;
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

    public String getFxml_path() {
        return fxml_path;
    }

    public void setFxml_path(String fxml_path) {
        this.fxml_path = fxml_path;
    }

    public Set<SysGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<SysGroup> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return super.toString() + "SysView{id=" + id + " name=" + name + ", view=" + fxml_path + ", groups=" + groups + '}';
    }

}
