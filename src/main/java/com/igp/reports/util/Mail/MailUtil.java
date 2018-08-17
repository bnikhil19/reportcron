package com.igp.reports.util.Mail;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class MailUtil
{
    public static boolean sendGenericMail(String action , String subject , String body,String recipient,boolean noReply){
        boolean result=false;

        try
        {
            URL url = new URL("http://admin.indiangiftsportal.com/myshop/sendEmail.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String postData = "",noReplyString="";
            // CURLOPT_POST
            con.setRequestMethod("POST");

            // CURLOPT_FOLLOWLOCATION
            con.setInstanceFollowRedirects(true);

            if(noReply){
                noReplyString="&noreply=1";
            }

            postData+="action="+action+"&sub="+subject+noReplyString+"&to="+recipient+"&body="+ URLEncoder.encode(body,"UTF-8");

            con.setRequestProperty("Content-length", String.valueOf(postData.length()));

            con.setDoOutput(true);
            con.setDoInput(true);

            DataOutputStream output = new DataOutputStream(con.getOutputStream());
            output.writeBytes(postData);
            output.close();

            // "Post data send ... waiting for reply");
            int code = con.getResponseCode(); // 200 = HTTP_OK
            if(code==200){
                result=true;
            }
            System.out.println("Post data for sentMail "+postData);
        }
        catch (Exception exception)
        {
            System.out.println("Exception while sending Mail "+ exception);
        }

        return result;
    }
}
