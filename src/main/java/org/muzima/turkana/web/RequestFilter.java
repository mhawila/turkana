package org.muzima.turkana.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.muzima.turkana.data.RegistrationQuery;
import org.muzima.turkana.data.RegistrationRepository;
import org.muzima.turkana.model.Registration;
import org.muzima.turkana.web.controller.RegistrationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Willa aka Baba Imu on 5/17/19.
 */
@Component
@Order(1)
public class RequestFilter extends GenericFilterBean {
    protected static List<String> MESSAGE_POST_PATHS;
    protected static Map<Integer, String> HTTP_ERROR_CODES;

    protected static Log log = LogFactory.getLog(RequestFilter.class);

    @Autowired
    private RegistrationRepository regRepo;

    @Autowired
    private RegistrationQuery registrationQuery;

    static {
        MESSAGE_POST_PATHS = Arrays.asList(new String[] { "api/mms", "api/sms", "api/media" });
        HTTP_ERROR_CODES = new HashMap<>(10);
        HTTP_ERROR_CODES.put(400, "Bad Request");
        HTTP_ERROR_CODES.put(401, "Unauthorized");
        HTTP_ERROR_CODES.put(403, "Forbidden");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            String path = httpRequest.getServletPath();
            String method = httpRequest.getMethod().toLowerCase();
            if(path.startsWith("/")) path = path.substring(1);

            if(MESSAGE_POST_PATHS.contains(path) && "post".equals(method)) {
                // handle posting
                String signature = httpRequest.getParameter("signature");
                String phoneNumber = httpRequest.getParameter("phoneNumber");
                String deviceId = httpRequest.getParameter("deviceId");

                if(signature == null || (phoneNumber == null && deviceId == null)) {
                    String message = "Request must provide the signature and phoneNumber/deviceId as request parameters";
                    log.warn("/" + path + " :" +message);
                    sendJSONError(httpResponse, 400, message, "/" + path);
                } else {
                    Registration registration = null;
                    if(phoneNumber != null) {
                        registration = registrationQuery.getActiveRegistrationByPhoneNumber(phoneNumber);
                    } else {
                        registration = registrationQuery.getActiveRegistrationByDeviceId(deviceId);
                    }


                    if(registration == null) {
                        String message = "No client found with ";
                        message += phoneNumber != null ? " phoneNumber: " + phoneNumber : " deviceId: " + deviceId;

                        sendJSONError(httpResponse, 400, message, "/" + path);
                    } else {
                        httpRequest.setAttribute("registration", registration);
                        chain.doFilter(httpRequest, response);
                    }
                }
            } else {
                if(RegistrationController.BASE_PATH.equals(path)) {
                    // TODO: Do the registration pre-check stuff.
                    // For now simply pass the request.

                }
                log.debug("Request allowed through path: /" + path);
                chain.doFilter(httpRequest, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    protected void sendJSONError(HttpServletResponse httpServletResponse, int status, String message, String path) throws IOException {
        httpServletResponse.setStatus(status);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        JSONObject errorObject = new JSONObject();
        errorObject.put("timeStamp", LocalDateTime.now().toString());
        errorObject.put("status", status);
        errorObject.put("error", HTTP_ERROR_CODES.get(status));
        errorObject.put("message", message);
        errorObject.put("path", path);
        errorObject.writeJSONString(httpServletResponse.getWriter());
    }
}
