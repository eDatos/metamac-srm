package org.siemac.metamac.srm.web.external;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.util.swagger.SwaggerUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

// TODO: Modificar función en SwaggerUtils
// En la función original se hace un return url.getProtocol() + "://", con el despliegue en docker swarm con un Haproxy
// que hace de gateway al servicio desplegado, url.getProtocol siempre devuelve el protocolo http, sin embargo si se quita
// el protocolo, las urls se construyen correctamente en cliente, ya sea con http o https.
public class SRMSwaggerUtils extends SwaggerUtils {

    public static String getResourceBaseURL(HttpServletRequest request) throws MalformedURLException {
        URL url = new URL(request.getRequestURL().toString());
        return "//" + url.getAuthority() + url.getPath().replace("/docs/api/index.jsp", StringUtils.EMPTY);
    }
}
