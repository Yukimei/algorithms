//reference: http://stackoverflow.com/questions/14445024/how-to-validate-invalidate-sessions-jsp-servlets
package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
//import org.json.simple.JSONValue;


public class LogoutAction extends Action {

    public String getName() {
        return "logout";
    }

    public String perform(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null)
        session.invalidate();

        JSONObject jObject = new JSONObject();
//        jObject = new LinkedHashMap();
        
        jObject.put("Message", "You've been logged out");
//        Map<String, String> jObject = new HashMap<String, String>();
//        jObject.put("Message", "You've been logged out");
//        StringWriter out = new StringWriter();
//        try {
//            JSONValue.writeJSONString(jObject, out);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        String jsonText = out.toString();
        return jObject.toJSONString();

//        return jObject.toJSONString();
    }
}
