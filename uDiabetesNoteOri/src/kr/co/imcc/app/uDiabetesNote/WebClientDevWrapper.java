package kr.co.imcc.app.uDiabetesNote;

import java.io.*;
import java.security.cert.*;

import javax.net.ssl.*;

import org.apache.http.client.*;
import org.apache.http.conn.*;
import org.apache.http.conn.scheme.*;
import org.apache.http.conn.ssl.*;
import org.apache.http.impl.client.*;



/*
This code is public domain: you are free to use, link and/or modify it in any way you want, for all purposes including commercial applications. 
*/
public class WebClientDevWrapper {
 
    public static HttpClient wrapClient(HttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
 
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }
 
                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }
 
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            X509HostnameVerifier verifier = new X509HostnameVerifier() {
 
                @Override
                public void verify(String string, SSLSocket ssls) throws IOException {
                }
 
                @Override
                public void verify(String string, X509Certificate xc) throws SSLException {
                }
 
                @Override
                public void verify(String string, String[] strings, String[] strings1) throws SSLException {
                }
 
                @Override
                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            MySSLSocketFactory ssf = new MySSLSocketFactory(ctx);
            ssf.setHostnameVerifier(verifier);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

