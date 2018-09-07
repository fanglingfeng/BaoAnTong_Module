package com.tjsoft.util;

/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.HostNameResolver;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

// Referenced classes of package org.apache.http.conn.ssl:
//            AllowAllHostnameVerifier, BrowserCompatHostnameVerifier, StrictHostnameVerifier, X509HostnameVerifier

public class SSLSocketFactory
    implements LayeredSocketFactory
{

    public static SSLSocketFactory getSocketFactory()
    {
        return DEFAULT_FACTORY;
    }

    public SSLSocketFactory(String algorithm, KeyStore keystore, String keystorePassword, KeyStore truststore, SecureRandom random, HostNameResolver nameResolver)
        throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
    {
        hostnameVerifier = BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
        if(algorithm == null)
            algorithm = "TLS";
        KeyManager keymanagers[] = null;
        if(keystore != null)
            keymanagers = createKeyManagers(keystore, keystorePassword);
        TrustManager trustmanagers[] = null;
        if(truststore != null)
            trustmanagers = createTrustManagers(truststore);
        sslcontext = SSLContext.getInstance(algorithm);
        sslcontext.init(keymanagers, trustmanagers, random);
        socketfactory = sslcontext.getSocketFactory();
        this.nameResolver = nameResolver;
    }

    public SSLSocketFactory(KeyStore keystore, String keystorePassword, KeyStore truststore)
        throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
    {
        this("TLS", keystore, keystorePassword, truststore, null, null);
    }

    public SSLSocketFactory(KeyStore keystore, String keystorePassword)
        throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
    {
        this("TLS", keystore, keystorePassword, null, null, null);
    }

    public SSLSocketFactory(KeyStore truststore)
        throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
    {
        this("TLS", null, null, truststore, null, null);
    }

    public SSLSocketFactory(SSLContext sslContext, HostNameResolver nameResolver)
    {
        hostnameVerifier = BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
        sslcontext = sslContext;
        socketfactory = sslcontext.getSocketFactory();
        this.nameResolver = nameResolver;
    }

    public SSLSocketFactory(SSLContext sslContext)
    {
        this(sslContext, ((HostNameResolver) (null)));
    }

    private SSLSocketFactory()
    {
        hostnameVerifier = BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
        sslcontext = null;
        socketfactory = HttpsURLConnection.getDefaultSSLSocketFactory();
        nameResolver = null;
    }

    private static KeyManager[] createKeyManagers(KeyStore keystore, String password)
        throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException
    {
        if(keystore == null)
        {
            throw new IllegalArgumentException("Keystore may not be null");
        } else
        {
            KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmfactory.init(keystore, password == null ? null : password.toCharArray());
            return kmfactory.getKeyManagers();
        }
    }

    private static TrustManager[] createTrustManagers(KeyStore keystore)
        throws KeyStoreException, NoSuchAlgorithmException
    {
        if(keystore == null)
        {
            throw new IllegalArgumentException("Keystore may not be null");
        } else
        {
            TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmfactory.init(keystore);
            return tmfactory.getTrustManagers();
        }
    }

    public Socket createSocket()
        throws IOException
    {
        return (SSLSocket)socketfactory.createSocket();
    }

    public Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress, int localPort, HttpParams params)
        throws IOException
    {
        if(host == null)
            throw new IllegalArgumentException("Target host may not be null.");
        if(params == null)
            throw new IllegalArgumentException("Parameters may not be null.");
        SSLSocket sslsock = (SSLSocket)(sock == null ? createSocket() : sock);
        if(localAddress != null || localPort > 0)
        {
            if(localPort < 0)
                localPort = 0;
            InetSocketAddress isa = new InetSocketAddress(localAddress, localPort);
            sslsock.bind(isa);
        }
        int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
        int soTimeout = HttpConnectionParams.getSoTimeout(params);
        InetSocketAddress remoteAddress;
        if(nameResolver != null)
            remoteAddress = new InetSocketAddress(nameResolver.resolve(host), port);
        else
            remoteAddress = new InetSocketAddress(host, port);
        try
        {
            sslsock.connect(remoteAddress, connTimeout);
        }
        catch(SocketTimeoutException ex)
        {
            throw new ConnectTimeoutException((new StringBuilder()).append("Connect to ").append(remoteAddress).append(" timed out").toString());
        }
        sslsock.setSoTimeout(soTimeout);
        try
        {
            hostnameVerifier.verify(host, sslsock);
        }
        catch(IOException iox)
        {
            try
            {
                sslsock.close();
            }
            catch(Exception x) { }
            throw iox;
        }
        return sslsock;
    }

    public boolean isSecure(Socket sock)
        throws IllegalArgumentException
    {
        if(sock == null)
            throw new IllegalArgumentException("Socket may not be null.");
        if(!(sock instanceof SSLSocket))
            throw new IllegalArgumentException("Socket not created by this factory.");
        if(sock.isClosed())
            throw new IllegalArgumentException("Socket is closed.");
        else
            return true;
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
        throws IOException, UnknownHostException
    {
        SSLSocket sslSocket = (SSLSocket)socketfactory.createSocket(socket, host, port, autoClose);
        hostnameVerifier.verify(host, sslSocket);
        return sslSocket;
    }

    public void setHostnameVerifier(X509HostnameVerifier hostnameVerifier)
    {
        if(hostnameVerifier == null)
        {
            throw new IllegalArgumentException("Hostname verifier may not be null");
        } else
        {
            this.hostnameVerifier = hostnameVerifier;
            return;
        }
    }

    public X509HostnameVerifier getHostnameVerifier()
    {
        return hostnameVerifier;
    }

    public static final String TLS = "TLS";
    public static final String SSL = "SSL";
    public static final String SSLV2 = "SSLv2";
    public static final X509HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
    public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = new BrowserCompatHostnameVerifier();
    public static final X509HostnameVerifier STRICT_HOSTNAME_VERIFIER = new StrictHostnameVerifier();
    private static final SSLSocketFactory DEFAULT_FACTORY = new SSLSocketFactory();
    private final SSLContext sslcontext;
    private final javax.net.ssl.SSLSocketFactory socketfactory;
    private final HostNameResolver nameResolver;
    private volatile X509HostnameVerifier hostnameVerifier;

}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\Workspaces\MyEclipse6.5\JEECMS2012\WebRoot\WEB-INF\lib\httpclient-4.0.3.jar
	Total time: 64 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/
