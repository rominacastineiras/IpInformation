package Infrastructure.Repositories;

import Interfaces.IpInformationRespositoryInterface;
import Model.IpInformation;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.set;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class IpInformationInMongoDB  implements IpInformationRespositoryInterface {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    @Override
    public void connectIfNecessary() {
        Properties propiedades = new Properties();
        try{
            propiedades.load(new FileReader(new File("config.properties").getAbsolutePath()));
        }catch(IOException error){
        }

        String userName = (String) propiedades.getOrDefault("DB_USERNAME", "");
        String password = (String) propiedades.getOrDefault("DB_PASSWORD", "");

        mongoClient = MongoClients.create("mongodb+srv://" + userName + ":" + password + "@cluster0.xok7qpl.mongodb.net/cafeDB");
        database = mongoClient.getDatabase("cafeDB"); //TODO: cambiar nombre
        collection = database.getCollection("IpInformation");
    }

    @Override
    public Map<String, String> getMostFarCountry() {
       Document result = collection.find().sort(descending("distance")).first();
       Map<String, String> map = new HashMap<>();

       if(result != null) {
           map.put("country_name", (String) result.get("country_name"));
           map.put("country_code", (String) result.get("country_code"));
           map.put("distance", (String) result.get("distance"));
           map.put("invocations", (String) result.get("invocations"));
       }else{
           map.put("Info", "Todavía no hay estadísticas, consulte más tarde");
       }

       return map;
    }

    @Override
    public Map<String, String> getLeastFarCountry() {
        Document result = collection.find().sort(ascending("distance")).first();
        Map<String, String> map = new HashMap<>();

        if(result != null) {
           map.put("country_name", (String) result.get("country_name"));
            map.put("country_code", (String) result.get("country_code"));
            map.put("distance", (String) result.get("distance"));
            map.put("invocations", (String) result.get("invocations"));
        }else{
            map.put("Info", "Todavía no hay estadísticas, consulte más tarde");
        }
        return map;    }

    @Override
    public Map<String, String> getAverageDistance() {
        FindIterable<Document> results = collection.find();
        MongoCursor<Document> cursor = results.iterator();
        double operando = 0.00;
        int divisor = 0;

        while (cursor.hasNext()) {
            Document elementOnStudy = cursor.next();
            operando = operando + parseDouble((String) elementOnStudy.get("distance")) *  parseInt((String) elementOnStudy.get("invocations"));
            divisor = divisor + parseInt((String) elementOnStudy.get("invocations"));
        }
        Map<String, String> map = new HashMap<>();

        map.put("averageDistance",String.valueOf(operando / divisor));

        return map;
    }

    @Override
    public String lastPersistedIpTimestamp() {
        Document result = collection.find().sort(descending("timestamp")).first();
        return result.get("timestamp").toString();
    }

    @Override
    public void save(IpInformation ipInformation) {

        Map<String, String> result = ipInformation.result();

        Document ipInformationFound = collection.find(new Document("country_code", result.get("countryIsoCode"))).first();

        int invocations = 1;

        if(ipInformationFound != null){
            invocations = parseInt((String) ipInformationFound.get("invocations"));
            invocations++;
            Document someIpInformation = new Document("_id", new ObjectId());
            someIpInformation.append("invocations", String.valueOf(invocations));
            Bson filter = eq("country_code", result.get("countryIsoCode"));
            Bson updateOperation = set("invocations", String.valueOf(invocations));
            collection.updateOne(filter, updateOperation);

        }else{
            Document someIpInformation = new Document("_id", new ObjectId());
            someIpInformation.append("country_name", result.get("countryName"))
                    .append("country_code", result.get("countryIsoCode"))
                    .append("distance", result.get("distanceToBuenosAires"))
                    .append("invocations", String.valueOf(invocations))
                    .append("timestamp", result.get("timestamp"));

            collection.insertOne(someIpInformation);

        }


    }
}
