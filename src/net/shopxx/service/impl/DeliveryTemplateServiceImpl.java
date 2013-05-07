package net.shopxx.service.impl;

import javax.annotation.Resource;
import net.shopxx.dao.DeliveryTemplateDao;
import net.shopxx.entity.DeliveryTemplate;
import net.shopxx.service.DeliveryTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("deliveryTemplateServiceImpl")
public class DeliveryTemplateServiceImpl extends BaseServiceImpl<DeliveryTemplate, Long>
  implements DeliveryTemplateService
{

  @Resource(name="deliveryTemplateDaoImpl")
  private DeliveryTemplateDao IIIllIlI;

  @Resource(name="deliveryTemplateDaoImpl")
  public void setBaseDao(DeliveryTemplateDao deliveryTemplateDao)
  {
    super.setBaseDao(deliveryTemplateDao);
  }

  @Transactional(readOnly=true)
  public DeliveryTemplate findDefault()
  {
    return this.IIIllIlI.findDefault();
  }
}


 * Qualified Name:     net.shopxx.service.impl.DeliveryTemplateServiceImpl

