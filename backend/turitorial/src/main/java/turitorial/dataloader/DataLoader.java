package turitorial.dataloader;
import org.json .*;

public class DataLoader {
    public String login(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject.getString("id");
    }
    public JSONArray instanceList(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject.getJSONArray("data");
    }
    public class InfoByInstanceName{
        String label;
        JSONArray property,content;
        InfoByInstanceName(String _label, JSONArray _property, JSONArray _content){
            label =_label;
            property=_property;
            content=_content;
        }
        public String label() {
            return label;
        }
        public JSONArray property() {
            return property;
        }
        public JSONArray content() {
            return content;
        }
    }
    public InfoByInstanceName infoByInstanceName(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        jsonObject = jsonObject.getJSONObject("data");
        InfoByInstanceName sample = new InfoByInstanceName(jsonObject.getString("label"),jsonObject.getJSONArray("property"),jsonObject.getJSONArray("content"));
        return sample;
    }
    public JSONArray inputQuestion(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject.getJSONArray("data");
    }
    public JSONArray linkInstance(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        jsonObject = jsonObject.getJSONObject("data");
        return jsonObject.getJSONArray("results");
    }
    public JSONArray questionListByUriName(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject.getJSONArray("data");
    }
    public JSONArray relatedsubject(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject.getJSONArray("data");
    }
    public class KnowledgeCard{
        String entity_type,entity_name;
        JSONArray entity_features;
        KnowledgeCard(String _type, String _name, JSONArray _features){
            entity_type=_type;
            entity_name=_name;
            entity_features=_features;
        }
        public String entity_type() {
            return entity_type;
        }
        public String entity_name() {
            return entity_name;
        }
        public JSONArray entity_features() {
            return entity_features;
        }
    }
    public KnowledgeCard getKnowledgeCard(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        jsonObject = jsonObject.getJSONObject("data");
        KnowledgeCard sample = new KnowledgeCard(jsonObject.getString("entity_type"),jsonObject.getString("entity_name"),jsonObject.getJSONArray("entity_features"));
        return sample;
    }
    public static void main(String[] args) throws JSONException {

    }
}