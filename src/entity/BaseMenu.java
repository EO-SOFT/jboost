/*

 * BaseMenu.java
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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author OUSSAMA EZZIOURI <oussama.ezziouri@gmail.com>
 */
@Entity
@Table(name = "base_menu", uniqueConstraints = {
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
public class BaseMenu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_id_seq")
    @SequenceGenerator(name = "menu_id_seq", sequenceName = "menu_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "menu")
    private String menu;

    @ManyToOne(optional = true, cascade = CascadeType.REFRESH)
    private BaseMenu parent;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "menuPermissions")
    private Set<BaseGroup> groups = new HashSet<BaseGroup>(0);

    public BaseMenu() {
    }

    public BaseMenu(String name, String menu) {
        this.name = name;
        this.menu = menu;
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

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public Set<BaseGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<BaseGroup> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "BaseMenu{id=" + id + ",name=" + name + ", menu=" + menu + ", parent=" + parent + ", groups=" + groups + '}';
    }

}
