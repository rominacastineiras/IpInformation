package Infrastructure.Repositories;

import Interfaces.IpInformationRespositoryInterface;
import Model.IpInformation;
import com.eclipsesource.json.JsonObject;
import com.mongodb.MongoClientException;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.set;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class IpInformationInMongoDB  implements IpInformationRespositoryInterface {
    private MongoCollection<Document> collection;
    Properties configuration;

    public IpInformationInMongoDB(Properties configuration) {
        this.configuration = configuration;
    }


    @Override
    public void connectIfNecessary() {

        String userName = (String) configuration.getOrDefault("DB_USERNAME", "");
        String password = (String) configuration.getOrDefault("DB_PASSWORD", "");
        String collectionName = (String) configuration.getOrDefault("COLLECTION_NAME", "");

        MongoClient mongoClient = MongoClients.create("mongodb+srv://" + userName + ":" + password + "@cluster0.xok7qpl.mongodb.net/cafeDB");
        MongoDatabase database = mongoClient.getDatabase("IpInformation");

        collection = database.getCollection(collectionName);


    }

    private static void createStadisticsRelations(Document result, Map<String, String> map) {
        if(result != null) {
            map.put("country_name", (String) result.get("country_name"));
            map.put("country_code", (String) result.get("country_code"));
            map.put("distance", (String) result.get("distance"));
            map.put("invocations", (String) result.get("invocations"));
        }else{
            map.put("Info", "Todavía no hay estadísticas, consulte más tarde");
        }
    }

    @Override
    public Map<String, String> getMostFarCountry() {
        Map<String, String> map = new HashMap<>();
        try{
            Document result = collection.find().sort(descending("distance")).first();
            createStadisticsRelations(result, map);
        } catch(MongoClientException e){
            map.put("Info", "Por favor consulte nuevamente");
        }
        return map;
    }

    @Override
    public Map<String, String> getLeastFarCountry() {

        Map<String, String> map = new HashMap<>();
        try{
            Document result = collection.find().sort(ascending("distance")).first();
            createStadisticsRelations(result, map);
        } catch(MongoClientException e){
            map.put("Info", "Por favor consulte nuevamente");
        }
        return map;
    }

    private static void calculateAverage(MongoCursor<Document> cursor, double operando, int divisor, Map<String, String> map) {
        if(!cursor.hasNext())
            map.put("Info", "Por favor consulte nuevamente");
        else {
            while (cursor.hasNext()) {
                Document elementOnStudy = cursor.next();
                operando = operando + parseDouble((String) elementOnStudy.get("distance")) * parseInt((String) elementOnStudy.get("invocations"));
                divisor = divisor + parseInt((String) elementOnStudy.get("invocations"));
            }
            map.put("averageDistance",String.valueOf(operando / divisor));
        }

    }

    @Override
    public Map<String, String> getAverageDistance() {
        Map<String, String> map = new HashMap<>();
       try{
            FindIterable<Document> results = collection.find();
            MongoCursor<Document> cursor = results.iterator();
            double operando = 0.00;
            int divisor = 0;

            calculateAverage(cursor, operando, divisor, map);
        } catch(MongoClientException e){
        map.put("Info", "Por favor consulte nuevamente");
    }
        return map;

    }

    @Override
    public String lastPersistedIpTimestamp() {
     try {
         Document result = collection.find().sort(descending("timestamp")).first();
         return result.get("timestamp").toString();

     } catch(MongoClientException | NullPointerException e) {
         return "Por favor consulte nuevamente";
     }
    }

    @Override
    public boolean isInMemory() {
        return false;
    }

    public void saveFromJson(JsonObject object){
        save(new IpInformation(
                object.getString("countryName",""),
                object.getString("countryIsoCode",""),
                object.getString("currency",""),
                object.getDouble("longitude",0.00),
                object.getDouble("latitude",0.00),
                new ArrayList<>(Collections.singleton(object.getString("languages", ""))),
                object.getDouble("quoteAgainstDollar",0.00),
                object.getString("timezone", "")
        ));
    }
    @Override
    public void save(IpInformation ipInformation) {

        Map<String, String> result = ipInformation.result();

        try{
            Document ipInformationFound = collection.find(new Document("country_code", result.get("countryIsoCode"))).first();
            if(ipInformationFound == null){
                int invocations = 1;
                insertStadistic(result, invocations);
            }else
                updateStadistic(result, ipInformationFound);
        } catch(MongoClientException e){
            int invocations = 1;
            insertStadistic(result, invocations);
        }

    }

    private void insertStadistic(Map<String, String> result, int invocations) {
        Document someIpInformation = new Document("_id", new ObjectId());
        someIpInformation.append("country_name", result.get("countryName"))
                .append("country_code", result.get("countryIsoCode"))
                .append("distance", result.get("distanceToBuenosAires"))
                .append("invocations", String.valueOf(invocations))
                .append("timestamp", result.get("timestamp"));

        collection.insertOne(someIpInformation);
    }

    private void updateStadistic(Map<String, String> result, Document ipInformationFound) {
        int invocations;
        invocations = parseInt((String) ipInformationFound.get("invocations"));
        invocations++;
        Document someIpInformation = new Document("_id", new ObjectId());
        someIpInformation.append("invocations", String.valueOf(invocations));
        Bson filter = eq("country_code", result.get("countryIsoCode"));
        Bson updateOperation = set("invocations", String.valueOf(invocations));
        collection.updateOne(filter, updateOperation);
    }
}
