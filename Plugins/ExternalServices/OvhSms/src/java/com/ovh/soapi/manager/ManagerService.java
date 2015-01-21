package com.ovh.soapi.manager;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.5
 * 2013-06-18T14:52:00.454+02:00
 * Generated source version: 2.7.5
 * 
 */
@WebServiceClient(name = "managerService", 
                  wsdlLocation = "http://www.ovh.com/soapi/soapi-dlw-1.59.wsdl",
                  targetNamespace = "http://soapi.ovh.com/manager") 
public class ManagerService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://soapi.ovh.com/manager", "managerService");
    public final static QName ManagerPort = new QName("http://soapi.ovh.com/manager", "managerPort");
    static {
        URL url = ManagerService.class.getResource("soapi-dlw-1.59.wsdl");
        if (url == null) {
            url = ManagerService.class.getClassLoader().getResource("soapi-dlw-1.59.wsdl");
        } 
        if (url == null) {
            java.util.logging.Logger.getLogger(ManagerService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "soapi-dlw-1.59.wsdl");
        }       
        WSDL_LOCATION = url;
    }

    public ManagerService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public ManagerService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ManagerService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns ManagerPortType
     */
    @WebEndpoint(name = "managerPort")
    public ManagerPortType getManagerPort() {
        return super.getPort(ManagerPort, ManagerPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ManagerPortType
     */
    @WebEndpoint(name = "managerPort")
    public ManagerPortType getManagerPort(WebServiceFeature... features) {
        return super.getPort(ManagerPort, ManagerPortType.class, features);
    }

}