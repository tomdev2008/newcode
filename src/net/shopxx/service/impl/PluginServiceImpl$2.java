package net.shopxx.service.impl;

import net.shopxx.plugin.StoragePlugin;
import org.apache.commons.collections.Predicate;

class PluginServiceImpl$2
  implements Predicate
{
  public boolean evaluate(Object object)
  {
    StoragePlugin localStoragePlugin = (StoragePlugin)object;
    return localStoragePlugin.getIsEnabled() == this.IIIllIll;
  }
}
