package com.ubclaunchpad.room8;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

class EmailValidator implements Runnable {
    final String apiCall = "https://apilayer.net/api/check?access_key=bb3364e8de4f62c612ddf9a0ab1c7009&email=";
    private volatile boolean isValid;
    String email = "";

    @Override
    public void run() {
        BufferedReader br = null;
        URL url = null;
        try {
            url = new URL(apiCall + email);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            InputStream inputStream = url.openStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            br = new BufferedReader(inputStreamReader);
            String line = "";
            isValid(br, line);

        } catch (Exception e) {
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
    }
    public void isValid(BufferedReader br, String line) {
        try {
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            JSONObject jsonObj = new JSONObject(sb.toString());
            isValid = jsonObj.getBoolean("smtp_check");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getValid() {
        return isValid;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}