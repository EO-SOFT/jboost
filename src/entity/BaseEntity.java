package entity;

import db.DBInstance;
import entity.permission.exceptions.EntityPermissionException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Created by OUSSAMA EZZIOURI <oussama.ezziouri@gmail.com>
 */
@MappedSuperclass
@Table(name = "base_entity")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BaseEntity implements Serializable {

    @CreationTimestamp
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    @UpdateTimestamp
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private BaseUser create_user; //User who creates the record

    @ManyToOne(cascade = CascadeType.REFRESH)
    private BaseUser update_user; //User who updates the record

    @Transient
    public final DBInstance HDB = new DBInstance().getInstance();

    public DBInstance getHDB() {
        return HDB;
    }

    
    /**
     * Check if the user has rights to execute the operation on the given entity
     *
     * @param entityName The name of the entity to check
     * @param user User that need to be checked
     * @param operation, 'C' for Create, 'R' for Read, 'U' for Update, 'D' for
     * Delete.
     * @return true if the user has right on this operation for the given entity
     * or false otherwise.
     */
    public boolean checkAccessToEntity(String entityName, BaseUser user, char operation) {
        for (SysEntityPermission en : user.getEntityPermissions()) {
            switch (operation) {
                case 'C':
                    if (en.getEntity().equals(entityName) && en.canCreate() == 1) {
                        return true;
                    }
                    break;
                case 'R':
                    if (en.getEntity().equals(entityName) && en.canRead() == 1) {
                        return true;
                    }
                    break;
                case 'U':
                    if (en.getEntity().equals(entityName) && en.canUpdate() == 1) {
                        return true;
                    }
                    break;
                case 'D':
                    if (en.getEntity().equals(entityName) && en.canDelete() == 1) {
                        return true;
                    }
                    break;
            }
        }
        return false;

    }

    /**
     * Check if the user has rights to access the given menu
     *
     * @param menu The name of the menu
     * @param user The user to check
     * @return true if the user has right to access into the given menu or false
     * otherwise.
     */
    public boolean checkAccessToMenu(String menu, BaseUser user) {
        for (SysMenu en : user.getMenuPermissions()) {
            if (en.getMenu().equals(menu)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the user has rights to access the given view
     *
     * @param view The name of the view
     * @param user The user to check
     * @return true if the user has right to access into the given view or false
     * otherwise.
     */
    public boolean checkAccessToView(String view, BaseUser user) {
        for (SysView en : user.getViewPermissions()) {
            if (en.getFxml_path().equals(view)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Insert the given object in the database
     *
     * @param object Object to be inserted
     * @param u User to execute the create operation
     * @return Id of the new database line, 0 if the creation fails
     * @throws entity.permission.exceptions.EntityPermissionException
     */
    public int create(Object object, BaseUser u) throws EntityPermissionException {
        if (u.checkAccessToEntity(object.getClass().getSimpleName(), u, 'C')) {
            try {
                HDB.getSession().flush();
                HDB.getSession().clear();
                HDB.getSession().beginTransaction();
                int id = (int) HDB.getSession().save(object);
                HDB.getSession().getTransaction().commit();

                String msg = "New "
                        + object.getClass().getCanonicalName()
                        + " created by user " + u.getId()
                        + " / " + u.getFirstName() + " " + u.getLastName() + ".";
                HDB.info(msg);
                return id;
            } catch (HibernateException e) {
                if (HDB.getSession().getTransaction() != null && HDB.getSession().getTransaction().isActive()) {
                    try {
                        // Second try catch as the rollback could fail as well
                        HDB.getSession().getTransaction().rollback();
                    } catch (HibernateException e1) {
                        System.out.println("Error rolling back transaction");
                        System.out.println(e1.getMessage());
                        return 0;
                    }
                    // throw again the first exception

                    throw e;
                }
            }
        } else {
            String msg = "User " + u.getId() + " / " + u.getFirstName() + " " + u.getLastName() + " doesn't have permission to create new " + object.getClass().getCanonicalName() + " objects.";
            EntityPermissionException e = new EntityPermissionException(msg);
            HDB.severe(e.getMessage());
            throw e;
        }
        return 0;
    }

    /**
     * Insert the given entity object in the database with CreationTimestamp and
     * created user
     *
     * @param entity Entity to be inserted
     * @param u User to execute the create operation
     * @return Id of the new database line, 0 if the creation fails
     */
    public int create(BaseEntity entity, BaseUser u) {
        if (u.checkAccessToEntity(entity.getClass().getSimpleName(), u, 'C')) {
            try {
                entity.setCreateTime(new Date());
                entity.setCreate_user(u);
                HDB.getSession().flush();
                HDB.getSession().clear();
                HDB.getSession().beginTransaction();
                int newId = (int) HDB.getSession().save(entity);
                HDB.getSession().getTransaction().commit();

                return newId;
            } catch (HibernateException e) {
                if (HDB.getSession().getTransaction() != null && HDB.getSession().getTransaction().isActive()) {
                    try {
                        // Second try catch as the rollback could fail as well
                        HDB.getSession().getTransaction().rollback();
                    } catch (HibernateException e1) {
                        HDB.severe("Error rolling back transaction");
                        HDB.severe(e1.getMessage());
                        return 0;
                    }
                }
            }
        } else {
            String msg = "User " + u.getId() + " / " + u.getFirstName() + " " + u.getLastName() + " doesn't have permission to create new " + entity.getClass().getCanonicalName() + " objects.";
            EntityPermissionException e = new EntityPermissionException(msg);
            HDB.severe(e.getMessage());
        }
        return 0;
    }

    /**
     * Return all lines that much the given SQL criteria
     *
     * @param object Simple class name to be search on
     * @param u
     * @param whereCriteria Parameters to test on WHERE clause in HQL format
     * (ex: id = 1, name = 'Toto'
     * @param orderBy ex id ASC, name DESC ..
     * @param startRow
     * @param maxRows
     * @return List of found objects or null if no objects are found
     *
     * @throws ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws entity.permission.exceptions.EntityPermissionException
     */
    public List<Object> select(Object object, BaseUser u, String whereCriteria,
            String orderBy, int startRow, int maxRows)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, EntityPermissionException {
        if (u.checkAccessToEntity(object.getClass().getSimpleName(), u, 'R')) {
            try {
                String limit = "";
                HDB.getSession().flush();
                HDB.getSession().clear();
                HDB.getSession().beginTransaction();

                //Intialize Where criteria to 1=1
                if (whereCriteria.equals("") || whereCriteria.isEmpty() || whereCriteria == null) {
                    whereCriteria = " WHERE 1 = 1";
                } else {
                    whereCriteria = " WHERE " + whereCriteria;
                }
                //Intialize limit clause            

                //Intialize order by clause            
                if (orderBy.equals("") || orderBy.isEmpty() || orderBy == null) {
                    orderBy = " ORDER BY id ASC";
                } else {
                    orderBy = " ORDER BY " + orderBy;
                }

                HDB.startSession();
                Query query = HDB.getSession().createQuery("FROM "
                        + object.getClass().getSimpleName()
                        + whereCriteria
                        + orderBy
                        + limit).setFirstResult(startRow);

                if (maxRows > 0) {
                    query.setMaxResults(maxRows);
                }

                HDB.getSession().getTransaction().commit();
                if (query.list().isEmpty()) {
                    return null;
                } else {
                    Class<?> clazz = Class.forName(object.getClass().getCanonicalName());
                    return convert(query.list(), clazz.newInstance());
                }

            } catch (HibernateException e) {
                if (HDB.getSession().getTransaction() != null && HDB.getSession().getTransaction().isActive()) {
                    try {
                        // Second try catch as the rollback could fail as well
                        HDB.getSession().getTransaction().rollback();
                    } catch (HibernateException e1) {
                        System.out.println("Error rolling back transaction on read method !");
                        System.out.println(e1.getMessage());
                    }
                    // throw again the first exception

                    throw e;
                }
            }
        } else {
            String msg = "User " + u.getId() + " / " + u.getFirstName() + " " + u.getLastName() + " doesn't have permission to select records of " + object.getClass().getCanonicalName() + " objects.";
            EntityPermissionException e = new EntityPermissionException(msg);
            HDB.severe(e.getMessage());
            throw e;
        }
        return new ArrayList<Object>();
    }

    /**
     * Update the given object in the database
     *
     * @param object Object to be updated
     * @param u User to execute the update operation
     * @return true if the update succeed, false otherwise
     * @throws entity.permission.exceptions.EntityPermissionException
     */
    public boolean update(Object object, BaseUser u) throws EntityPermissionException {
        if (u.checkAccessToEntity(object.getClass().getSimpleName(), u, 'U')) {
            try {

                HDB.getSession().flush();
                HDB.getSession().clear();
                HDB.getSession().beginTransaction();
                HDB.getSession().update(object);

                HDB.getSession().getTransaction().commit();
                return true;
            } catch (HibernateException e) {
                if (HDB.getSession().getTransaction() != null && HDB.getSession().getTransaction().isActive()) {
                    try {
                        // Second try catch as the rollback could fail as well
                        HDB.getSession().getTransaction().rollback();
                    } catch (HibernateException e1) {
                        System.out.println("Error rolling back transaction");
                        System.out.println(e1.getMessage());
                        return false;
                    }
                    // throw again the first exception

                    throw e;
                }
            }
        } else {
            String msg = "User " + u.getId() + " / " + u.getFirstName() + " " + u.getLastName() + " doesn't have permission to update " + object.getClass().getCanonicalName() + " objects.";
            EntityPermissionException e = new EntityPermissionException(msg);
            HDB.severe(e.getMessage());
            throw e;
        }
        return false;
    }

    /**
     * Update the given entity in the database with UpdateTimestamp and Update
     * user
     *
     * @param entity Entity to be updated
     * @param u User to execute the update operation
     * @return true if the update succeed, false otherwise
     */
    public boolean update(BaseEntity entity, BaseUser u) throws EntityPermissionException {
        if (u.checkAccessToEntity(entity.getClass().getSimpleName(), u, 'U')) {
            try {
                entity.setUpdateTime(new Date());
                entity.setUpdate_user(u);
                HDB.getSession().flush();
                HDB.getSession().clear();
                HDB.getSession().beginTransaction();
                HDB.getSession().update(entity);

                HDB.getSession().getTransaction().commit();
                return true;
            } catch (HibernateException e) {
                if (HDB.getSession().getTransaction() != null && HDB.getSession().getTransaction().isActive()) {
                    try {
                        // Second try catch as the rollback could fail as well
                        HDB.getSession().getTransaction().rollback();
                    } catch (HibernateException e1) {
                        System.out.println("Error rolling back transaction");
                        System.out.println(e1.getMessage());
                        return false;
                    }
                    // throw again the first exception

                    throw e;
                }
            }
        } else {
            String msg = "User " + u.getId() + " / " + u.getFirstName() + " " + u.getLastName() + " doesn't have permission to update " + entity.getClass().getCanonicalName() + " objects.";
            EntityPermissionException e = new EntityPermissionException(msg);
            HDB.severe(e.getMessage());
            throw e;
        }
        return false;
    }

    /**
     * Delete the given object from the database
     *
     * @param object Object to be deleted
     * @param u User to execute the delete operation
     * @return true if the delete succeed, false otherwise
     * @throws hibernate.EntityPermissionException
     */
    public boolean delete(Object object, BaseUser u) throws EntityPermissionException {
        if (u.checkAccessToEntity(object.getClass().getSimpleName(), u, 'D')) {
            try {
                HDB.getSession().flush();
                HDB.getSession().clear();
                HDB.getSession().beginTransaction();
                HDB.getSession().delete(object);
                HDB.getSession().getTransaction().commit();
            } catch (HibernateException e) {
                if (HDB.getSession().getTransaction() != null && HDB.getSession().getTransaction().isActive()) {
                    try {
                        // Second try catch as the rollback could fail as well
                        HDB.getSession().getTransaction().rollback();
                    } catch (HibernateException e1) {
                        System.out.println("Error rolling back transaction");
                        System.out.println(e1.getMessage());
                    }
                    // throw again the first exception

                    throw e;
                }
            }
        } else {
            String msg = "User " + u.getId() + " / " + u.getFirstName() + " " + u.getLastName() + " doesn't have permission to delete " + object.getClass().getCanonicalName() + " objects.";
            EntityPermissionException e = new EntityPermissionException(msg);
            HDB.severe(e.getMessage());
            throw e;
        }
        return false;
    }

    /**
     * Return the object with the given id
     *
     * @param object Object to be read, used to get the SimpleClassName
     * @param u User to execute the read operation
     * @param id Id of the object in the database
     * @return Object or null if no object found
     *
     * @throws ClassNotFoundException
     * @throws hibernate.EntityPermissionException
     */
    public Object read(Object object, BaseUser u, int id) throws ClassNotFoundException, EntityPermissionException {
        if (u.checkAccessToEntity(object.getClass().getSimpleName(), u, 'R')) {
            try {
                HDB.getSession().flush();
                HDB.getSession().clear();
                HDB.getSession().beginTransaction();

                //################# Harness Data ####################       
                HDB.startSession();
                Query query = HDB.getSession().createQuery(
                        "FROM " + object.getClass().getSimpleName()
                        + " WHERE id = :id").setParameter("id", id);

                HDB.getSession().getTransaction().commit();
                if (query.list().isEmpty()) {
                    return null;
                } else {
                    return Class.forName(object.getClass().getCanonicalName()).cast(query.list().get(0));
                }

            } catch (HibernateException e) {
                if (HDB.getSession().getTransaction() != null && HDB.getSession().getTransaction().isActive()) {
                    try {
                        // Second try catch as the rollback could fail as well
                        HDB.getSession().getTransaction().rollback();
                    } catch (HibernateException e1) {
                        System.out.println("Error rolling back transaction on read method !");
                        System.out.println(e1.getMessage());
                    }
                    // throw again the first exception

                    throw e;
                }
            }
        } else {
            String msg = "User " + u.getId() + " / " + u.getFirstName() + " " + u.getLastName() + " doesn't have permission to read " + object.getClass().getCanonicalName() + " objects.";
            EntityPermissionException e = new EntityPermissionException(msg);
            HDB.severe(e.getMessage());
            throw e;
        }
        return null;
    }

    private <T> List<T> convert(List list, T t) {
        return list;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BaseUser getCreate_user() {
        return create_user;
    }

    public void setCreate_user(BaseUser create_user) {
        this.create_user = create_user;
    }

    public BaseUser getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(BaseUser update_user) {
        this.update_user = update_user;
    }

    @Override
    public String toString() {
        return "BaseEntity{createTime=" + createTime + ", updateTime=" + updateTime + ", create_user=" + create_user + ", update_user=" + update_user + '}';
    }

}
