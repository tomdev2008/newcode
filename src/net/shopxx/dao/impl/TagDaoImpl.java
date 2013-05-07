package net.shopxx.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import net.shopxx.dao.TagDao;
import net.shopxx.entity.Tag;
import net.shopxx.entity.Tag.Type;
import org.springframework.stereotype.Repository;

@Repository("tagDaoImpl")
public class TagDaoImpl extends BaseDaoImpl<Tag, Long>
  implements TagDao
{
  public List<Tag> findList(Tag.Type type)
  {
    CriteriaBuilder localCriteriaBuilder = this.IIIllIlI.getCriteriaBuilder();
    CriteriaQuery localCriteriaQuery = localCriteriaBuilder.createQuery(Tag.class);
    Root localRoot = localCriteriaQuery.from(Tag.class);
    localCriteriaQuery.select(localRoot);
    if (type != null)
      localCriteriaQuery.where(localCriteriaBuilder.equal(localRoot.get("type"), type));
    localCriteriaQuery.orderBy(new Order[] { localCriteriaBuilder.asc(localRoot.get("order")) });
    return this.IIIllIlI.createQuery(localCriteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
  }
}


 * Qualified Name:     net.shopxx.dao.impl.TagDaoImpl

