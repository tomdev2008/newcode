package net.shopxx.filter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.shopxx.Setting;
import net.shopxx.util.SettingUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Component("siteStatusFilter")
public class SiteStatusFilter extends OncePerRequestFilter
{
  private static final String[] IIIllIlI = { "/admin/**" };
  private static final String IIIllIll = "/common/site_close.jhtml";
  private static AntPathMatcher IIIlllII = new AntPathMatcher();
  private String[] ignoreUrlPatterns = IIIllIlI;
  private String redirectUrl = "/common/site_close.jhtml";

  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
  {
    Setting localSetting = SettingUtils.get();
    if (localSetting.getIsSiteEnabled().booleanValue())
    {
      filterChain.doFilter(request, response);
    }
    else
    {
      String str1 = request.getServletPath();
      if (str1.equals(this.redirectUrl))
      {
        filterChain.doFilter(request, response);
        return;
      }
      if (this.ignoreUrlPatterns != null)
        for (String str2 : this.ignoreUrlPatterns)
        {
          if (!IIIlllII.match(str2, str1))
            continue;
          filterChain.doFilter(request, response);
          return;
        }
      response.sendRedirect(request.getContextPath() + this.redirectUrl);
    }
  }

  public String[] getIgnoreUrlPatterns()
  {
    return this.ignoreUrlPatterns;
  }

  public void setIgnoreUrlPatterns(String[] ignoreUrlPatterns)
  {
    this.ignoreUrlPatterns = ignoreUrlPatterns;
  }

  public String getRedirectUrl()
  {
    return this.redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl)
  {
    this.redirectUrl = redirectUrl;
  }
}
