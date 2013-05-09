package net.shopxx.dao.impl;

import net.shopxx.entity.Parameter;
import org.apache.commons.collections.Predicate;

class ParameterGroupDaoImpl$1
  implements Predicate
{
  public boolean evaluate(Object object)
  {
    Parameter localParameter = (Parameter)object;
    return (localParameter != null) && (localParameter.getId() != null);
  }
}
