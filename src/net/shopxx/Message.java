package net.shopxx;

import net.shopxx.util.SpringUtils;

public class Message
{
	public enum MessageType
	{
	  success, warn, error;
	}
  private MessageType type;
  private String content;

  public Message()
  {
  }

  public Message(MessageType type, String content)
  {
    this.type = type;
    this.content = content;
  }

  public Message(MessageType type, String content, Object[] args)
  {
    this.type = type;
    this.content = SpringUtils.getMessage(content, args);
  }

  public static Message success(String content, Object[] args)
  {
    return new Message(MessageType.success, content, args);
  }

  public static Message warn(String content, Object[] args)
  {
    return new Message(MessageType.warn, content, args);
  }

  public static Message error(String content, Object[] args)
  {
    return new Message(MessageType.error, content, args);
  }

  public MessageType getType()
  {
    return this.type;
  }

  public void setType(MessageType type)
  {
    this.type = type;
  }

  public String getContent()
  {
    return this.content;
  }

  public void setContent(String content)
  {
    this.content = content;
  }

  public String toString()
  {
    return SpringUtils.getMessage(this.content, new Object[0]);
  }
}
