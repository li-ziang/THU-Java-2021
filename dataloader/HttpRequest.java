import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpRequest {
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 鎵撳紑鍜孶RL涔嬮棿鐨勮繛鎺�
            URLConnection connection = realUrl.openConnection();
            // 璁剧疆閫氱敤鐨勮姹傚睘鎬�
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 寤虹珛瀹為檯鐨勮繛鎺�
            connection.connect();
            // 鑾峰彇鎵�鏈夊搷搴斿ご瀛楁
            Map<String, List<String>> map = connection.getHeaderFields();
            // 閬嶅巻鎵�鏈夌殑鍝嶅簲澶村瓧娈�
            // for (String key : map.keySet()) {
            //     System.out.println(key + "--->" + map.get(key));
            // }
            // 瀹氫箟 BufferedReader杈撳叆娴佹潵璇诲彇URL鐨勫搷搴�
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("鍙戦�丟ET璇锋眰鍑虹幇寮傚父锛�" + e);
            e.printStackTrace();
        }
        // 浣跨敤finally鍧楁潵鍏抽棴杈撳叆娴�
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 鍙戦�丳OST璇锋眰蹇呴』璁剧疆濡備笅涓よ
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 鑾峰彇URLConnection瀵硅薄瀵瑰簲鐨勮緭鍑烘祦
            out = new PrintWriter(conn.getOutputStream());
            // 鍙戦�佽姹傚弬鏁�
            out.print(param);
            // flush杈撳嚭娴佺殑缂撳啿
            out.flush();
            // 瀹氫箟BufferedReader杈撳叆娴佹潵璇诲彇URL鐨勫搷搴�
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("鍙戦�� POST 璇锋眰鍑虹幇寮傚父锛�"+e);
            e.printStackTrace();
        }
        //浣跨敤finally鍧楁潵鍏抽棴杈撳嚭娴併�佽緭鍏ユ祦
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    

    public static void main(String[] args) {
        //鍙戦�� GET 璇锋眰
        // String s=HttpRequest.sendGet("http://localhost:6144/Home/RequestString", "key=123&v=456");
        // System.out.println(s);
        
        //鍙戦�� POST 璇锋眰
//        String sr=HttpRequest.sendPost("http://open.edukg.cn/opedukg/api/typeAuth/user/login", "password=20010120cyy&phone=18971657672");
//        JSONObject jsonObject=new JSONObject(sr);
//        String id = jsonObject.getString("id");
//        System.out.println(id);
        String s=HttpRequest.sendGet("http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList", "course=english&searchKey=apple&id=ee09e883-c628-403a-bd7f-edf0ef3c490a");
        JSONObject jsonObject = new JSONObject(s);
        JSONArray data = jsonObject.getJSONArray("data");
        
        //String data = jsonObject.getString("data");
        jsonObject = data.getJSONObject(0);
        String label = jsonObject.getString("label");
        String category = jsonObject.getString("category");
        String uri = jsonObject.getString("uri");
        System.out.println(label);
         System.out.println(category);
         System.out.println(uri);
    }
}