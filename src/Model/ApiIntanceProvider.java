package Model;

import Infrastructure.RemoteApisImplementation.IpInformationFromAbstractApi;
import Infrastructure.RemoteApisImplementation.IpInformationFromApilayer;
import Infrastructure.RemoteApisImplementation.IpInformationFromIpapi;
import com.eclipsesource.json.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ApiIntanceProvider {
    private InformationProviderNotDefined informationProviderNotDefined;
    private final Map<String, IpInformationFromAbstractApi> ipsForAbstractApi = new HashMap<>();
    private final Map<String, IpInformationFromIpapi>  ipsForIpapi = new HashMap<>();
    private final Map<String, IpInformationFromApilayer> ipsForApilayer = new HashMap<>();

    private final Properties propiedades;
    private final JsonObject defaultJson;

    public ApiIntanceProvider(Properties propiedades, JsonObject defaultJson) {
        this.propiedades = propiedades;
        this.defaultJson = defaultJson;
    }


    IpInformationFromApilayer getIpInformationFromApilayer(String currency) {
        IpInformationFromApilayer ipInformationFromApilayer;
        if(ipsForApilayer.get(currency) != null)
            ipInformationFromApilayer = ipsForApilayer.get(currency);
        else {
            ipInformationFromApilayer = new IpInformationFromApilayer(currency, propiedades.getProperty("APILAYER_KEY"));
            ipsForApilayer.put(currency, ipInformationFromApilayer);
        }
        return ipInformationFromApilayer;
        
    }
    IpInformationFromIpapi getIpInformationFromIpapi(String ip) {
        IpInformationFromIpapi ipInformationFromIpapi;
        if(ipsForIpapi.get(ip) != null)
            ipInformationFromIpapi = ipsForIpapi.get(ip);
        else {
            ipInformationFromIpapi = new IpInformationFromIpapi(ip, propiedades.getProperty("IPAPI_ACCESS_KEY"));
            ipsForIpapi.put(ip, ipInformationFromIpapi);
        }
        return ipInformationFromIpapi;
    }


    IpInformationFromAbstractApi getipinformationfromAbstractapi(String ip) {
        IpInformationFromAbstractApi ipInformationFromAbstractApi;
        if(ipsForAbstractApi.get(ip) != null)
            ipInformationFromAbstractApi = ipsForAbstractApi.get(ip);
        else {
            ipInformationFromAbstractApi = new IpInformationFromAbstractApi(ip, propiedades.getProperty("ABSTRACTAPI_ACCESS_KEY"));
            ipsForAbstractApi.put(ip, ipInformationFromAbstractApi);
        }
        return ipInformationFromAbstractApi;
    }
    InformationProviderNotDefined getInformationProviderNotDefined() {
        if(informationProviderNotDefined == null)
            informationProviderNotDefined = new InformationProviderNotDefined(defaultJson);

        return informationProviderNotDefined;
    }


}
