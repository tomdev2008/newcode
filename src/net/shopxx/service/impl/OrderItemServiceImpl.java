package net.shopxx.service.impl;

import javax.annotation.Resource;
import net.shopxx.dao.OrderItemDao;
import net.shopxx.entity.OrderItem;
import net.shopxx.service.OrderItemService;
import org.springframework.stereotype.Service;

@Service("orderItemServiceImpl")
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem, Long>
  implements OrderItemService
{
  @Resource(name="orderItemDaoImpl")
  public void setBaseDao(OrderItemDao orderItemDao)
  {
    super.setBaseDao(orderItemDao);
  }
}


 * Qualified Name:     net.shopxx.service.impl.OrderItemServiceImpl

