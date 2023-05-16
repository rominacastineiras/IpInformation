package Model;

import Infrastructure.RemoteApisImplementation.AbstractApiApiProvider;
import Infrastructure.RemoteApisImplementation.ApilayerApiProvider;
import Infrastructure.RemoteApisImplementation.IpapiApiProvider;
import com.eclipsesource.json.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ApiIntanceProvider {
    private NotDefinedApiProvider notDefinedApiProvider;
    private final Map<String, AbstractApiApiProvider> ipsForAbstractApi = new HashMap<>();
    private final Map<String, IpapiApiProvider>  ipsForIpapi = new HashMap<>();
    private final Map<String, ApilayerApiProvider> ipsForApilayer = new HashMap<>();

    private final Properties configuration;
    private final JsonObject defaultJson;

    public ApiIntanceProvider(Properties configuration, JsonObject defaultJson) {
        this.configuration = configuration;
        this.defaultJson = defaultJson;
    }


    ApilayerApiProvider getIpInformationFromApilayer(String currency) {
        ApilayerApiProvider apilayerApiProvider;
        if(ipsForApilayer.get(currency) != null)
            apilayerApiProvider = ipsForApilayer.get(currency);
        else {
            apilayerApiProvider = new ApilayerApiProvider(currency, configuration.getProperty("APILAYER_KEY"));
            ipsForApilayer.put(currency, apilayerApiProvider);
        }
        return apilayerApiProvider;
        
    }
    IpapiApiProvider getIpInformationFromIpapi(String ip) {
        IpapiApiProvider ipapiApiProvider;
        if(ipsForIpapi.get(ip) != null)
            ipapiApiProvider = ipsForIpapi.get(ip);
        else {
            ipapiApiProvider = new IpapiApiProvider(ip, configuration.getProperty("IPAPI_ACCESS_KEY"));
            ipsForIpapi.put(ip, ipapiApiProvider);
        }
        return ipapiApiProvider;
    }


    AbstractApiApiProvider getipinformationfromAbstractapi(String ip) {
        AbstractApiApiProvider abstractApiApiProvider;
        if(ipsForAbstractApi.get(ip) != null)
            abstractApiApiProvider = ipsForAbstractApi.get(ip);
        else {
            abstractApiApiProvider = new AbstractApiApiProvider(ip, configuration.getProperty("ABSTRACTAPI_ACCESS_KEY"));
            ipsForAbstractApi.put(ip, abstractApiApiProvider);
        }
        return abstractApiApiProvider;
    }
    NotDefinedApiProvider getInformationProviderNotDefined() {
        if(notDefinedApiProvider == null)
            notDefinedApiProvider = new NotDefinedApiProvider(defaultJson);

        return notDefinedApiProvider;
    }


}
