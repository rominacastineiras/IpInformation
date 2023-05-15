package Model;

import Infrastructure.RemoteApisImplementation.IpInformationFromAbstractApi;
import Infrastructure.RemoteApisImplementation.IpInformationFromApilayer;
import Infrastructure.RemoteApisImplementation.IpInformationFromIpapi;
import Interfaces.IpInformationInterface;
import com.eclipsesource.json.JsonObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ApiIntanceProvider {
    private InformationProviderNotDefined informationProviderNotDefined;
    private IpInformationFromIpapi ipInformationFromIpapi;
    private IpInformationFromAbstractApi ipInformationFromAbstractApi;
    private Map<String, IpInformationFromAbstractApi> ipsForAbstractApi = new HashMap<>();
    private IpInformationFromApilayer ipInformationFromApilayer;
    private Map<String, IpInformationFromIpapi>  ipsForIpapi = new HashMap<>();
    private Map<String, IpInformationFromApilayer> ipsForApilayer = new HashMap<>();

    private static final String NODATA = "No Data";

    private static final  JsonObject DEFAULT_INFORMATION = new JsonObject()
            .add("countryName",NODATA)
            .add("countryIsoCode",NODATA)
            .add("currency",NODATA)
            .add("timezone",NODATA)
            .add("longitude",0.00)
            .add("latitude",0.00)
            .add("languages", NODATA)
            .add("quoteAgainstDollar", 0.00);

    IpInformationFromApilayer getIpInformationFromApilayer(String currency) {
        if(ipsForApilayer.get(currency) != null)
            ipInformationFromApilayer = ipsForApilayer.get(currency);
        else {
            //TODO: ErrorJAR  ipInformationFromApilayer = new IpInformationFromApilayer(this.currency, propiedades.getProperty("APILAYER_KEY"));
            ipInformationFromApilayer = new IpInformationFromApilayer(currency, "BiQ1BDEeQiyWPzYIsJKpFCnGg84HVcuf");
            ipsForApilayer.put(currency, ipInformationFromApilayer);
        }
        return ipInformationFromApilayer;
        
    }
    IpInformationFromIpapi getIpInformationFromIpapi(String ip) {
        if(ipsForIpapi.get(ip) != null)
            ipInformationFromIpapi = ipsForIpapi.get(ip);
        else {
            //TODO: ErrorJAR   ipInformationFromIpapi = new IpInformationFromIpapi(ip, propiedades.getProperty("IPAPI_ACCESS_KEY"));
            ipInformationFromIpapi = new IpInformationFromIpapi(ip,"21d86faa5a30addd1f92a5447e46110c");
            ipsForIpapi.put(ip, ipInformationFromIpapi);
        }
        return ipInformationFromIpapi;
    }


    IpInformationFromAbstractApi getipinformationfromAbstractapi(String ip) {
        if(ipsForAbstractApi.get(ip) != null)
            ipInformationFromAbstractApi = ipsForAbstractApi.get(ip);
        else {
            //TODO: ErrorJAR  ipInformationFromAbstractApi = new IpInformationFromAbstractApi(ip, propiedades.getProperty("ABSTRACTAPI_ACCESS_KEY"));
            ipInformationFromAbstractApi = new IpInformationFromAbstractApi(ip, "0fad83f2089d4548a8603b56947456d1");
            ipsForAbstractApi.put(ip, ipInformationFromAbstractApi);
        }
        return ipInformationFromAbstractApi;
    }
    InformationProviderNotDefined getInformationProviderNotDefined() {
        if(informationProviderNotDefined == null)
            informationProviderNotDefined = new InformationProviderNotDefined(DEFAULT_INFORMATION);

        return informationProviderNotDefined;
    }


}
