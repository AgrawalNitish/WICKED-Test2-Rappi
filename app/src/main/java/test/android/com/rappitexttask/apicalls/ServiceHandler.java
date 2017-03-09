package test.android.com.rappitexttask.apicalls;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;


public class ServiceHandler {

    static String response = null;
    public static final String AUTHORIZATION = "Authorization";

    public static enum RequestMethod {
        GET, POST, PUT, PATCH, DELETE;
    }
    /*public final static int GET = 1;
    public final static int POST = 2;
    public final static int PUT = 3;
    public final static int PATCH = 4;
    public final static int DELETE = 5;*/

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     */

    private String getURlFormPath(String path, boolean isTakeOnlyPath) {
        if (isTakeOnlyPath) {
            return path;
        }
        return "http://" + Requests.getSubDomain() + Requests.baseUrl + Requests.middleUrl + path;
        //return "http://requestb.in/1b3ocol1";
    }

/*
    private String getURlFormPath(String path,List<NameValuePair> params) {
        if (params != null) {
            String paramString = URLEncodedUtils
                    .format(params, "utf-8");
            return this.baseUrl + path + "?" + paramString;
        }
        return this.baseUrl + path;
    }

    private HttpPost postRequest(String path,List<NameValuePair> params) {
        HttpPost httpPost = new HttpPost(this.getURlFormPath(path));
        if (params != null) {
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return httpPost;
    }

    private HttpPatch patchRequest(String path,List<NameValuePair> params) {
        HttpPatch httpPatch = new HttpPatch(this.getURlFormPath(path));
        if (params != null) {
            try {
                httpPatch.setEntity(new UrlEncodedFormEntity(params));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return httpPatch;
    }

    private HttpGet getRequest(String path,List<NameValuePair> params) {
        return new HttpGet(this.getURlFormPath(path,params));
    }
    private HttpPut putRequest(String path,List<NameValuePair> params) {
        return new HttpPut(this.getURlFormPath(path,params));
    }
    private HttpDelete deleteRequest(String path,List<NameValuePair> params) {
        return new HttpDelete(this.getURlFormPath(path,params));
    }

    */

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */

    /*
    public String makeServiceCall1(String path, int method,
                                  List<NameValuePair> params) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = this.postRequest(path,params);
                httpResponse = httpClient.execute(httpPost);
            } else if (method == GET) {
                HttpGet httpGet = this.getRequest(path,params);
                httpResponse = httpClient.execute(httpGet);
            } else if (method == PUT) {
                HttpPut httpPut = this.putRequest(path,params);
                httpResponse = httpClient.execute(httpPut);
            } else if (method == DELETE) {
                HttpDelete httpDelete = this.deleteRequest(path,params);
                httpResponse = httpClient.execute(httpDelete);
            }

            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }
    */

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     */

    /*
    public JSONObject makeServiceCall(String path, int method,List<NameValuePair> params) {
        String paramsStr = null;
        try {
            paramsStr = getQuery(params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return makeServiceCallBase(path, method,paramsStr);
    }
    */
    public ResponseModel makeServiceCall(ApiRequest apiRequest,
                                         Map<String, String> params) throws JSONException {
        String paramsStr = null;
        try {
            if (params != null)
                paramsStr = getQuery(params, ((apiRequest.getMethod() == RequestMethod.GET) ? false : apiRequest.isJsonRequest()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return makeServiceCallBase(apiRequest, paramsStr);
    }

    public ResponseModel makeServiceCall(ApiRequest apiRequest,
                                         Object params) throws JSONException {
        Object paramsStr = null;
        try {
            if (params != null) {
                if (params instanceof Map) {
                    paramsStr = getQuery((Map<String, String>) params, ((apiRequest.getMethod() == RequestMethod.DELETE || apiRequest.getMethod() == RequestMethod.GET) ? false : apiRequest.isJsonRequest()));
                } else if (params instanceof String) {
                    paramsStr = (String) params;
                } else if (params instanceof JSONObject) {
                    //paramsStr = getPostDataStringBilder((JSONObject) params).toString();
                    paramsStr = params;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return makeServiceCallBase(apiRequest, paramsStr);
    }


    public ResponseModel makeServiceCallBase(ApiRequest apiRequest, Object paramsStr) {
        HttpURLConnection urlConnection = null;

        URL url = null;
        ResponseModel responseModel;
        JSONObject object = null;
        InputStream inStream = null;
        String temp, response = "";
        int status = 0;
        try {
            String urlStr = this.getURlFormPath(apiRequest.getPath(), apiRequest.isTakeOnlyPath());
            if ((apiRequest.getMethod() == RequestMethod.DELETE || apiRequest.getMethod() == RequestMethod.GET) && paramsStr != null) {
                urlStr = urlStr + "?" + paramsStr;
            }
            url = new URL(urlStr);

            Log.e("URL is ", "" + this.getURlFormPath(apiRequest.getPath(), apiRequest.isTakeOnlyPath()));
            Log.e("Method is ", "" + apiRequest.getMethod());
            Log.e("peramers is ", "" + paramsStr);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept",
                    apiRequest.isJsonRequest() ? "application/json" : "application/x-www-form-urlencoded");
            if (apiRequest.getMethod() == RequestMethod.POST) {
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type",
                        apiRequest.isJsonRequest() ? "application/json" : "application/x-www-form-urlencoded");
            } else if (apiRequest.getMethod() == RequestMethod.GET) {
                urlConnection.setRequestMethod("GET");
            } else if (apiRequest.getMethod() == RequestMethod.PUT) {
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type",
                        apiRequest.isJsonRequest() ? "application/json" : "application/x-www-form-urlencoded");
            } else if (apiRequest.getMethod() == RequestMethod.PATCH) {
                urlConnection.setRequestMethod("PATCH");
                urlConnection.setRequestProperty("Content-Type",
                        apiRequest.isJsonRequest() ? "application/json" : "application/x-www-form-urlencoded");
            } else if (apiRequest.getMethod() == RequestMethod.DELETE) {
                urlConnection.setDoInput(true);
                urlConnection.setInstanceFollowRedirects(false);
                if (apiRequest.isAuthunticate()) {
                    urlConnection.setRequestProperty(AUTHORIZATION, "Bearer " + Requests.getUserToker());
                }
                urlConnection.setRequestProperty("Connection", "Keep-Alive");
                urlConnection.setRequestProperty("Content-Type",
                        apiRequest.isJsonRequest() ? "application/json" : "application/x-www-form-urlencoded");//  charset=UTF-8
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setRequestProperty("charset", "utf-8");
                urlConnection.setUseCaches(false);
                urlConnection.connect();
            }

            if (apiRequest.isAuthunticate() && apiRequest.getMethod() != RequestMethod.DELETE) {
                urlConnection.setRequestProperty(AUTHORIZATION, "Bearer " + Requests.getUserToker());
            }
            if (apiRequest.getMethod() != RequestMethod.DELETE && apiRequest.getMethod() != RequestMethod.GET) {
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                if (apiRequest.getMethod() == RequestMethod.PUT) {
                    OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream());
                    Writer bw = new BufferedWriter(osw);
                    if (paramsStr != null && paramsStr.toString().length() > 0)
                        bw.write(paramsStr.toString());
                    bw.flush();
                    bw.close();
                    System.err.println(urlConnection.getResponseCode());
                } else {
                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(paramsStr.toString());

                    //Log.e("paramsStr", "is " + URLEncoder.encode(paramsStr.toString(), "UTF-8"));
                    writer.flush();
                    writer.close();
                    os.close();
                    urlConnection.connect();
                }
            }
            try {
                try {
                    status = urlConnection.getResponseCode();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    status = -10;
                } catch (IOException e) {
                    e.printStackTrace();
                    status = -10;
                }
                Log.e("Print Response Code", "" + status);
                //if((status%100) != HttpStatus.SC_BAD_REQUEST)
                if ((status / 100) != 2)
                    inStream = urlConnection.getErrorStream();
                else
                    inStream = urlConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
                inStream = urlConnection.getErrorStream();
                if (inStream == null) {
                    inStream = urlConnection.getInputStream();
                }
            }

            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            while ((temp = bReader.readLine()) != null) {
                response += temp;
            }

            Log.e("Response is ", "" + response);
            object = (JSONObject) new JSONTokener(response).nextValue();

            //object= new JSONObject(getStringFromInputStream(inStream));

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (inStream != null) {
                try {
                    // this will close the bReader as well
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        try {
            Log.e("JsonObject", "" + object.toString());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return new ResponseModel(status, object);
    }


    public JSONObject makeDownloadCall(String downloadURLKey, String savePathKey, Map<String, String> params) {
        String downloadUrl = params.get(downloadURLKey);
        String saveFilePath = params.get(savePathKey);
        if (downloadUrl == null || saveFilePath == null) {
            return null;
        }
        JSONObject object = null;
        final int BYTE = 1024;
        final int MEGABYTE = BYTE * 1024;
        HttpURLConnection urlConnection = null;
        URL url = null;
        try {
            url = new URL(downloadUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setRequestMethod("GET");
            //urlConnection.setDoInput(true);
            //urlConnection.setDoOutput(true);

            String afterSaveFilePath = saveFilePath;
            String lastStr = saveFilePath.substring(saveFilePath.lastIndexOf('/') + 1);
            saveFilePath = saveFilePath.replace(lastStr, "tempData__" + lastStr);
            Log.e("FileName: ", afterSaveFilePath);
            Log.e("FileName: ", saveFilePath);


            urlConnection.connect();
            File tmpFile = new File(saveFilePath);
            FileOutputStream fileOutput = new FileOutputStream(tmpFile);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;
            float presentSize = totalSize / 100;
            byte[] buffer = new byte[MEGABYTE];
            int bufferSize = 0;
            while ((bufferSize = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferSize);
                downloadedSize += bufferSize;
                //Log.e("Progress", "Downloaded : "+  Math.round(downloadedSize/presentSize)+" %");
            }
            fileOutput.close();

            File file = new File(afterSaveFilePath);
            if (tmpFile.exists())
                tmpFile.renameTo(file);

            object = (JSONObject) new JSONTokener("{\"success\":true}").nextValue();
        } catch (Exception e) {
            //Log.e("Progress", "Error : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return object;
    }

    /*  public JSONObject makeUploadCall(String path, int method,Map<String, Object> params){
          String fileName = params.get("path").toString();
          JSONObject object = null;
          int serverResponseCode = 0;
          HttpURLConnection conn = null;
          DataOutputStream dos = null;
          String lineEnd = "\r\n";
          String twoHyphens = "--";
          String boundary = "*****";
          int bytesRead, bytesAvailable, bufferSize;
          byte[] buffer;
          int maxBufferSize = 1 * 1024 * 1024;
          File sourceFile = new File(fileName);

          if (sourceFile.isFile()) {
              try {

                  // open a URL connection to the Servlet
                  FileInputStream fileInputStream = new FileInputStream(sourceFile);
                  URL url = new URL(""+urls[APIBASEURL]+path);

                  // Open a HTTP  connection to  the URL
                  conn = (HttpURLConnection) url.openConnection();
                  conn.setDoInput(true); // Allow Inputs
                  conn.setDoOutput(true); // Allow Outputs
                  conn.setUseCaches(false); // Don't use a Cached Copy
                  conn.setRequestMethod("POST");
                  conn.setRequestProperty("Connection", "Keep-Alive");
                  conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                  conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                  conn.setRequestProperty("uploaded_file", fileName);

                  dos = new DataOutputStream(conn.getOutputStream());

                  dos.writeBytes(twoHyphens + boundary + lineEnd);
                  dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName +"\"" + lineEnd);
                  dos.writeBytes(lineEnd);

                  // create a buffer of  maximum size
                  bytesAvailable = fileInputStream.available();

                  bufferSize = Math.min(bytesAvailable, maxBufferSize);
                  buffer = new byte[bufferSize];

                  // read file and write it into form...
                  bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                  while (bytesRead > 0) {
                      dos.write(buffer, 0, bufferSize);
                      bytesAvailable = fileInputStream.available();
                      bufferSize = Math.min(bytesAvailable, maxBufferSize);
                      bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                       int progress = (int)((bytesRead / (float) bufferSize) * 100);
                       //publishProgress(progress);

                  }


                 // publishProgress(100);

                  // send multipart form data necesssary after file data...
                  dos.writeBytes(lineEnd);
                  dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                  // Responses from the server (code and message)
                  serverResponseCode = conn.getResponseCode();
                  String serverResponseMessage = conn.getResponseMessage();

                  Log.i("uploadFile", "HTTP Response is : "
                          + serverResponseMessage + ": " + serverResponseCode);

                  if(serverResponseCode == 200){
                      RondogoApp.alertToast("Done");
                  }

                  //close the streams //
                  fileInputStream.close();
                  dos.flush();
                  dos.close();
                  object = (JSONObject) new JSONTokener("{\"success\":true}").nextValue();
              } catch (MalformedURLException e) {
                  e.printStackTrace();
                  return null;
              } catch (Exception e) {
                  e.printStackTrace();
                  return null;
              }
          } // End else block
          return object;
      }*/
    /*
    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
    */
    public static String getQuery(Map<String, String> params,
                                  boolean isJsonRequest)
            throws Exception {
        StringBuffer requestParams = new StringBuffer();
        JSONObject jsonParam = new JSONObject();
        if (params != null && params.size() > 0) {
            Iterator<String> paramIterator = params.keySet().iterator();
            while (paramIterator.hasNext()) {
                String key = paramIterator.next();
                String value = params.get(key);
                if (isJsonRequest) {
                    jsonParam.put(key, value);
                } else {
                    requestParams.append(URLEncoder.encode(key, "UTF-8"));
                    requestParams.append("=").append(URLEncoder.encode(value, "UTF-8"));
                    requestParams.append("&");
                }
            }
            if (isJsonRequest) {
                //requestParams.append(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                //requestParams.append(getPostDataStringBilder(jsonParam));
                requestParams.append(jsonParam);
            }
        }
        int lenght = requestParams.toString().length();
        if (lenght > 0) {
            if (isJsonRequest) {
                return requestParams.toString();
            } else {
                return requestParams.toString().substring(0, (lenght - 1));
            }
        } else
            return requestParams.toString();
    }

   /* public static String getPostDataString(JSONObject params) throws Exception {

        Logger.printLog(Logger.ERROR,"JsonObject to convert "+params.toString());
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if(value instanceof JSONObject){
                value = ServiceHandler.getPostDataStringBilder((JSONObject)value);
            }else if(value instanceof JSONArray){
                value = ServiceHandler.getPostDataStringBilder((JSONArray)value);
            }
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            //result.append(URLEncoder.encode(value.toString(), java.nio.charset.StandardCharsets.UTF_8.toString()));

        }
        return result.toString();
    }

    public static StringBuilder getPostDataStringBilder(JSONObject params) throws Exception {

        Logger.printLog(Logger.ERROR,"JsonObject to convert "+params.toString());
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if(value instanceof JSONObject){
                value = ServiceHandler.getPostDataStringBilder((JSONObject)value);
            }else if(value instanceof JSONArray){
                value = ServiceHandler.getPostDataStringBilder((JSONArray)value);
            }
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
           *//* if(value instanceof StringBuilder){
                result.append(value);
            }else*//*
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            //result.append(URLEncoder.encode(value.toString(), java.nio.charset.StandardCharsets.UTF_8.toString()));

        }
        return result;
    }

    public static StringBuilder getPostDataStringBilder(JSONArray params) throws Exception {
        for(int i=0;i<params.length();i++){
            return getPostDataStringBilder(params.getJSONObject(i));

        }
        return new StringBuilder();
    }*/

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
