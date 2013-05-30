package net.shopxx.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.shopxx.plugin.PaymentPlugin;
import net.shopxx.plugin.StoragePlugin;
import net.shopxx.service.PluginService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Service;

@Service("pluginServiceImpl")
public class PluginServiceImpl
  implements PluginService
{

  @Resource
  private List<PaymentPlugin> paymentPlugin = new ArrayList();

  @Resource
  private List<StoragePlugin> storagePlugins = new ArrayList();

  @Resource
  private Map<String, PaymentPlugin> paymentPluginMap = new HashMap();

  @Resource
  private Map<String, StoragePlugin> storagePluginMap = new HashMap();

  public List<PaymentPlugin> getPaymentPlugins()
  {
    Collections.sort(this.paymentPlugin);
    return this.paymentPlugin;
  }

  public List<StoragePlugin> getStoragePlugins()
  {
    Collections.sort(this.storagePlugins);
    return this.storagePlugins;
  }

  public List<PaymentPlugin> getPaymentPlugins(boolean isEnabled)
  {
	  CollectionUtils v = new CollectionUtils();
    ArrayList localArrayList = new ArrayList();
    CollectionUtils.select(this.paymentPlugin, new Predicate(){
    	    PaymentPlugin localPaymentPlugin = (PaymentPlugin)object;
    	    return localPaymentPlugin.getIsEnabled() == this.storagePlugins;
    }, localArrayList);
    Collections.sort(localArrayList);
    return localArrayList;
  }

  public List<StoragePlugin> getStoragePlugins(boolean isEnabled)
  {
    ArrayList localArrayList = new ArrayList();
    CollectionUtils.select(this.storagePlugins, new PluginServiceImpl.2(this, isEnabled), localArrayList);
    Collections.sort(localArrayList);
    return localArrayList;
  }

  public PaymentPlugin getPaymentPlugin(String id)
  {
    return (PaymentPlugin)this.paymentPluginMap.get(id);
  }

  public StoragePlugin getStoragePlugin(String id)
  {
		return (StoragePlugin) this.storagePluginMap.get(id);
  }

}
