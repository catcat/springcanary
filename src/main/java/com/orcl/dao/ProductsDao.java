package com.orcl.dao;


import com.orcl.entity.*;
import com.orcl.view.TextEntry;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigInteger;
import java.util.*;

@Repository
@Scope("prototype")
public class ProductsDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(propagation = Propagation.REQUIRED)
    public Product findById(long id) {
        Session s = cs();
        Product p = (Product)s.get(Product.class, id);
        s.lock(p, LockMode.OPTIMISTIC);
        return p;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void transactionalUpdate(long id, Product row) {
        Product oldRow = findById(id);
        oldRow.setName(row.getName());
        oldRow.setUser(row.getUser());
        oldRow.setQuality(row.getQuality());
        oldRow.setPrice(row.getPrice());
        oldRow.setCreated(row.getCreated());
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Product> getAll() {
        Query q = cs().createQuery("FROM Product ");
        return q.list();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<User> getAllUsers() {
        Query q = cs().createQuery("FROM User ");
        return q.list();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Property> getAllTexts(Long productId) {

        Query q = cs().createQuery(
            "FROM Property p " +
            "LEFT JOIN p.texts t " +
            "WHERE t.product.id=:id " +
            "ORDER By p.name "
        ).setLong("id", productId);


        List<Property> rs = q.list();
        return rs;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<TextEntry> getAllTextsNativeSql(Long productId) {

        Query q = cs()
                //.getNamedQuery("SPRING_USER.F2")
                //.setParameter("RATIO", "23");

                .createSQLQuery(
                        " select p.id, p.name, coalesce(t.value, v.value)  " +
                        "      from properties p " +
                        "      left join TEXTS t " +
                        "      on ( t.property_id = p.id AND t.PRODUCT_ID = :productId) " +
                        "      left join Variants v " +
                        "      on (v.id = t.variant_id) "+
                        "      order by p.name desc"
                ).setParameter("productId", productId);

        List rs = q.list();
        List<TextEntry> result = new ArrayList<TextEntry>();
        for(Object o:rs) {
            Object[] a = (Object[])o;
            TextEntry te = new TextEntry();
            te.setId(((BigInteger)a[0]).longValue());
            te.setName((String)a[1]);
            te.setValue((String)a[2]);//null!
            result.add(te);
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Property> formProps(Long productId) {
        List<Property> properties = cs().createQuery(" FROM Property p ORDER BY p.name DESC").list();
        List<Text> texts = cs()
                .createQuery(" FROM Text t " +
                             //" LEFT JOIN t.variant v" +
                             " WHERE t.product.id = :pid ")
                .setParameter("pid", productId)
                .list();
        //System.out.println("############");

        //todo:optimize
        for(Property property: properties) {
            for(Text text : texts) {
                if(!text.getProperty().equals(property)) continue;
                property.setSingleText(text);
            }
        }

        return properties;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Map<Long, List<Variant>> formVariants(Long productId) {
        List<Variant> variants = cs().createQuery(" FROM Variant ").list();

        Map<Long, List<Variant>> result = new HashMap<Long, List<Variant>>();


        List<Variant> variantsOfCurrentType;
        for(Variant v: variants) {
            long typeId = v.getType().getId();
            variantsOfCurrentType = result.get(typeId);
            if(variantsOfCurrentType == null) {
                variantsOfCurrentType = new ArrayList<Variant>();
                result.put(typeId, variantsOfCurrentType);
            }
            variantsOfCurrentType = result.get(typeId);
            variantsOfCurrentType.add(v);
        }

        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void flush() {
        cs().flush();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String getDeterminant(Long productId) {
        return "42";
        /*

        Query q = cs()
                //.getNamedQuery("SPRING_USER.F2")
                //.setParameter("RATIO", "23");
                .createSQLQuery(
                        "SELECT SPRING_USER.F2(:productId) det from dual")
                .setParameter("productId", productId);

        return (String)q.uniqueResult();
        */
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void clearTextsOfProduct(Long productId) {
        Query query = cs().createQuery(
                "DELETE FROM Text t WHERE t.product.id=:id")
                .setLong("id", productId);
        query.executeUpdate();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Object merge(Object row) {
        return cs().merge(row);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrUpdate(Object row) {
        cs().saveOrUpdate(row);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void refresh(Object row) {
        cs().refresh(row);
    }

    
    public Session cs(){
        return sessionFactory.getCurrentSession();
    }
}
