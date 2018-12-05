package com.frohlich.it.config.jgit;

import org.eclipse.jgit.util.Base64;

import javax.servlet.http.HttpServletRequest;

import static com.google.common.base.Strings.emptyToNull;
import static java.nio.charset.StandardCharsets.UTF_8;

public class RemoteUserUtil {
    public static final String AUTHORIZATION = "Authorization";
  /**
   * Tries to get username from a request with following strategies:
   *
   * <ul>
   *   <li>ServletRequest#getRemoteUser
   *   <li>HTTP 'Authorization' header
   *   <li>Custom HTTP header
   * </ul>
   *
   * @param req request to extract username from.
   * @param loginHeader name of header which is used for extracting username.
   * @return the extracted username or null.
   */
  public static String getRemoteUser(HttpServletRequest req, String loginHeader) {
    if (AUTHORIZATION.equals(loginHeader)) {
      String user = emptyToNull(req.getRemoteUser());
      if (user != null) {
        // The container performed the authentication, and has the user
        // identity already decoded for us. Honor that as we have been
        // configured to honor HTTP authentication.
        return user;
      }

      // If the container didn't do the authentication we might
      // have done it in the front-end web server. Try to split
      // the identity out of the Authorization header and honor it.
      String auth = req.getHeader(AUTHORIZATION);
      return extractUsername(auth);
    }
    // Nonstandard HTTP header. We have been told to trust this
    // header blindly as-is.
    return emptyToNull(req.getHeader(loginHeader));
  }

  /**
   * Extracts username from an HTTP Basic or Digest authentication header.
   *
   * @param auth header value which is used for extracting.
   * @return username if available or null.
   */
  public static String extractUsername(String auth) {
    auth = emptyToNull(auth);

    if (auth == null) {
      return null;

    } else if (auth.startsWith("Basic ")) {
      auth = auth.substring("Basic ".length());
      auth = new String(Base64.decode(auth), UTF_8);
      final int c = auth.indexOf(':');
      return c > 0 ? auth.substring(0, c) : null;

    } else if (auth.startsWith("Digest ")) {
      final int u = auth.indexOf("username=\"");
      if (u <= 0) {
        return null;
      }
      auth = auth.substring(u + 10);
      final int e = auth.indexOf('"');
      return e > 0 ? auth.substring(0, e) : null;

    } else {
      return null;
    }
  }
}
