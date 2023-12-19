package com.M2D.EmissionAssessment.MultiTenancy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

import java.util.Base64;


@Component
@Slf4j
public class RequestInterceptor extends WebRequestHandlerInterceptorAdapter {
    public static String Token;
    public static Long UserId;
    public static Long OrgId;
    public RequestInterceptor(WebRequestInterceptor requestInterceptor) {
        super(requestInterceptor);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object object) throws Exception {
        Long tenantID = 0L;
        if (!request.getMethod().equals("OPTIONS")) {

            String requestURI = request.getRequestURI();


            if (request.getHeader("Authorization") != null) {

                ObjectMapper mapper = new ObjectMapper();
                String token = request.getHeader("Authorization");
                Token = token;
                String[] chunks = token.split("\\.");
                Base64.Decoder decoder = Base64.getUrlDecoder();
                String payload = new String(decoder.decode(chunks[1]));
                JsonNode rootNode = mapper.readTree(payload);
                tenantID = rootNode.get("TanetId").asLong();
                OrgId = rootNode.get("OrgId").asLong();
                UserId = rootNode.get("UserId").asLong();

                TenantContext.setToken(rootNode);
                TenantContext.setCurrentTenant(tenantID);

            }

//            if (!requestURI.contains("swagger") && !requestURI.contains("api-docs") &&
//                    !requestURI.contains("favicon") && !requestURI.contains("error")) {
//                TenantContext.setRequestedIP(request.getHeader("X-Forwarded-For"));
//                if (request.getHeader("X-TenantID") != null)
//                    tenantID = Long.valueOf(request.getHeader("X-TenantID"));
//                else
//                log.info("RequestURI:: {}" , requestURI);
//                log.info("Search for X-TenantID {} " , tenantID);
//                TenantContext.setCurrentTenant(tenantID);
//            }
        }
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
    {
        TenantContext.clear();
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex){
        TenantContext.clear();
    }

}