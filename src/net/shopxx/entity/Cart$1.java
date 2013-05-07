package net.shopxx.entity;

import org.apache.commons.collections.Predicate;

class Cart$1
  implements Predicate
{
  public boolean evaluate(Object object)
  {
    GiftItem localGiftItem = (GiftItem)object;
    return (localGiftItem != null) && (localGiftItem.getGift().equals(this.IIIllIll.getGift()));
  }
}


 * Qualified Name:     net.shopxx.entity.Cart.1

