package net.shopxx.service.impl;

import javax.annotation.Resource;
import net.shopxx.dao.LogDao;
import net.shopxx.entity.Log;
import net.shopxx.service.LogService;
import org.springframework.stereotype.Service;

@Service("logServiceImpl")
public class LogServiceImpl extends BaseServiceImpl<Log, Long>
  implements LogService
{

  @Resource(name="logDaoImpl")
  private LogDao IIIllIlI;

  @Resource(name="logDaoImpl")
  public void setBaseDao(LogDao logDao)
  {
    super.setBaseDao(logDao);
  }

  public void clear()
  {
    this.IIIllIlI.removeAll();
  }
}


 * Qualified Name:     net.shopxx.service.impl.LogServiceImpl

