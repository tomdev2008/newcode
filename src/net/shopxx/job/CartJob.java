package net.shopxx.job;

import javax.annotation.Resource;
import net.shopxx.service.CartService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("cartJob")
@Lazy(false)
public class CartJob
{

  @Resource(name="cartServiceImpl")
  private CartService IIIllIlI;

  @Scheduled(cron="${job.cart_evict_expired.cron}")
  public void evictExpired()
  {
    this.IIIllIlI.evictExpired();
  }
}


 * Qualified Name:     net.shopxx.job.CartJob
