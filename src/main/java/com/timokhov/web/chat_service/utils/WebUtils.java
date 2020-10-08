package com.timokhov.web.chat_service.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.util.*;
import java.util.function.Predicate;

public class WebUtils {

    //see http://detectmobilebrowsers.com/
    private static final String MOBILE_USER_AGENT_REGEX = "(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino|android|ipad|playbook|silk).*";
    private static final String MOBILE_USER_AGENT_REGEX_SHORT = "(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-";

    public interface Method {
        String GET = "GET";
        String POST = "POST";
        String HEAD = "HEAD";
        String OPTIONS = "OPTIONS";
        String PUT = "PUT";
        String DELETE = "DELETE";
        String TRACE = "TRACE";
        String CONNECT = "CONNECT";

    }

    public interface ContentType {
        String APPLICATION = "application";
        String IMAGE = "image";
        String ANY = "*";

        String APPLICATION_ANY = APPLICATION + "/*";
        String APPLICATION_PDF = APPLICATION + "/pdf";
        String APPLICATION_XLS = APPLICATION + "/xls";
        String APPLICATION_OCTET_STREAM = APPLICATION + "/octet-stream";

        String IMAGE_ANY = IMAGE + "/*";
        String IMAGE_PNG = IMAGE + "/png";

        String ANY_ANY = ANY + "/*";
    }

    public interface Header {
        String CONTENT_DISPOSITION = "Content-Disposition";
        String CONTENT_DISPOSITION_ATTACHMENT_FILENAME_FORMAT = "attachment; filename=\"%s\"";

        String USER_AGENT = "User-Agent";

        String CONTENT_TYPE = "Content-Type";
        String CONTENT_LENGTH = "Content-Length";
        String LOCATION = "Location";

        String PRAGMA = "Pragma";
        String PRAGMA_NO_CACHE = "no-cache";

        String EXPIRES = "Expires";

        String CACHE_CONTROL = "Cache-Control";
        String CACHE_CONTROL_NO_CACHE = "no-cache";
        String CACHE_CONTROL_NO_STORE = "no-store";

        String ORIGIN = "Origin";

        String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
        String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";

        String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
        String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
        String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
        String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
        String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
        String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

        String X_FORWARDED_FOR = "X-Forwarded-For";
    }

    public static <T> ResponseEntity<T> getRedirectEntity(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(WebUtils.Header.LOCATION, url);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    public static HttpEntity<byte[]> getFileEntity(byte[] bytes, String fileName) {
        return getFileEntity(bytes, fileName, null);
    }

    public static HttpEntity<byte[]> getFileEntity(byte[] bytes, String fileName, String contentType) {
        return new HttpEntity<>(bytes, createHeadersForFileEntity(bytes, fileName, contentType));
    }

    public static ResponseEntity getFileResponseEntity(byte[] bytes, String fileName, String contentType) {
        HttpHeaders headers = createHeadersForFileEntity(bytes, fileName, contentType);
        InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(bytes));

        return new ResponseEntity<>(isr, headers, HttpStatus.OK);
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }

    public static boolean isMobile(HttpServletRequest request) {
        String userAgent = request.getHeader(Header.USER_AGENT);
        if (userAgent == null) {
            return false;
        }

        userAgent = StringUtils.lowerCase(userAgent);
        return userAgent.matches(MOBILE_USER_AGENT_REGEX) || userAgent.substring(0, 4).matches(MOBILE_USER_AGENT_REGEX_SHORT);
    }

    public static String getRemoteAddress(HttpServletRequest request) {
        return getRemoteAddress(request, true, null);
    }

    public static String getRemoteAddress(HttpServletRequest request, boolean useForwardedAddress) {
        return getRemoteAddress(request, useForwardedAddress, null);
    }

    public static String getRemoteAddress(HttpServletRequest request, String forwardedAddressHeader) {
        return getRemoteAddress(request, true, forwardedAddressHeader);
    }

    public static String getRemoteAddress(HttpServletRequest request, boolean useForwardedAddress, String forwardedAddressHeader) {
        String remoteAddress = null;
        if (useForwardedAddress) {
            if (forwardedAddressHeader == null) {
                forwardedAddressHeader = Header.X_FORWARDED_FOR;
            }
            String forwardedAddress = request.getHeader(forwardedAddressHeader);
            if (!StringUtils.isBlank(forwardedAddress)) {
                int commaIdx = forwardedAddress.indexOf(',');
                remoteAddress = commaIdx > -1 ? forwardedAddress.substring(0, commaIdx) : forwardedAddress;
            }
        }
        if (StringUtils.isBlank(remoteAddress)) {
            remoteAddress = request.getRemoteAddr();
        }
        return remoteAddress;
    }

    public static Map<String, Object> getRequestInfo(HttpServletRequest request) {

        Map<String, Object> result = new LinkedHashMap<>();
        if (request == null) {
            return result;
        }

        result.put("requestURL", request.getRequestURL());
        result.put("requestURI", request.getRequestURI());

        result.put("serverName", request.getServerName());
        result.put("serverPort", request.getServerPort());

        result.put("localAddr", request.getLocalAddr());
        result.put("localName", request.getLocalName());
        result.put("localPort", request.getLocalPort());

        result.put("remoteAddr", request.getRemoteAddr());
        result.put("remoteHost", request.getRemoteHost());
        result.put("remotePort", request.getRemotePort());

        result.put("secure", request.isSecure());
        result.put("protocol", request.getProtocol());
        result.put("scheme", request.getScheme());
        result.put("requestedSessionId", request.getRequestedSessionId());

        result.put("method", request.getMethod());
        result.put("pathInfo", request.getPathInfo());
        result.put("servletPath", request.getServletPath());
        result.put("contextPath", request.getContextPath());
        result.put("pathTranslated", request.getPathTranslated());

        Map<String, Object> headers = new LinkedHashMap<>();

        final Enumeration headerNames = request.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                Object headerName = headerNames.nextElement();
                final Enumeration headerValueEnum = request.getHeaders(String.valueOf(headerName));

                List<String> headerValues = new ArrayList<>();
                while (headerValueEnum.hasMoreElements()) {
                    String header = String.valueOf(headerValueEnum.nextElement());
                    if (!Objects.equals("referer", header)) {
                        headerValues.add(header);
                    }
                }
                headers.put(String.valueOf(headerName), headerValues);
            }

            result.put("headers", headers);
        }

        return result;
    }

    public static boolean isClientConnectionFailure(Throwable e) {
        Predicate<Throwable> isClientConnectionFailureDirect = throwable -> {
            if (throwable instanceof SocketTimeoutException) {
                if ("Read timed out".equals(throwable.getMessage())) {
                    //client sends request too slow
                    return true;
                }
            } else if (throwable instanceof SocketException) {
                if (
                        "Connection reset".equals(throwable.getMessage()) ||
                                "Broken pipe".equals(throwable.getMessage()) ||
                                "Broken pipe (Write failed)".equals(throwable.getMessage())
                ) {
                    //client does not comply the protocol
                    return true;
                }
            }

            return false;
        };

        //servlet container may wrap IOException into its own exception, so we need to check the cause too
        //e.g. Tomcat wraps IOException during response writing into org.apache.catalina.connector.ClientAbortException
        return isClientConnectionFailureDirect.test(e) ||
                (e.getCause() != null && e.getCause() != e && isClientConnectionFailureDirect.test(e.getCause()));

    }

    private static HttpHeaders createHeadersForFileEntity(byte[] bytes, String fileName, String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(WebUtils.Header.CONTENT_LENGTH, String.valueOf(bytes.length));

        if (contentType == null) {
            ByteArrayInputStream bis = null;
            try {
                bis = new ByteArrayInputStream(bytes);
                contentType = URLConnection.guessContentTypeFromStream(bis);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }

        if (contentType == null) {
            contentType = ContentType.APPLICATION_OCTET_STREAM;
        }

        headers.set(WebUtils.Header.CONTENT_TYPE, contentType);

        if (fileName != null) {
            headers.set(WebUtils.Header.CONTENT_DISPOSITION, String.format(
                    WebUtils.Header.CONTENT_DISPOSITION_ATTACHMENT_FILENAME_FORMAT, fileName
            ));
        }

        return headers;
    }
}
