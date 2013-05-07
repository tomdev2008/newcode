package net.shopxx.service.impl;

import net.shopxx.plugin.PaymentPlugin;
import org.apache.commons.collections.Predicate;

class PluginServiceImpl$1
  implements Predicate
{
  public boolean evaluate(Object object)
  {
    PaymentPlugin localPaymentPlugin = (PaymentPlugin)object;
    return localPaymentPlugin.getIsEnabled() == this.IIIllIll;
  }
}


 * Qualified Name:     net.shopxx.service.impl.PluginServiceImpl.1

