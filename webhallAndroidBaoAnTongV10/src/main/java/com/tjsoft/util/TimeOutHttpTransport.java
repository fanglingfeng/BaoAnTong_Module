package com.tjsoft.util;

import java.io.IOException;
import java.net.Proxy;

import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;

public class TimeOutHttpTransport extends HttpTransportSE{


//
	private int timeout =20000; //默认超时时间为20s
    public TimeOutHttpTransport(String url) {
     super(url);  
    }  
    public TimeOutHttpTransport(String url, int timeout) {
     super(url); 
     this.timeout = timeout; 
   } 
    protected ServiceConnection getServiceConnection(String url) throws IOException {  
        ServiceConnectionSE serviceConnection = new ServiceConnectionSE(url);  
        serviceConnection.setConnectionTimeOut(timeout);  
        return serviceConnection;  
    }    

}
