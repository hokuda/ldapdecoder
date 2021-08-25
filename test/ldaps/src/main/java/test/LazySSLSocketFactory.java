package test;

import java.io.PrintWriter;
import java.io.IOException;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.Enumeration;
import java.util.Properties;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;


public class LazySSLSocketFactory extends SSLSocketFactory {
        SSLSocketFactory delegate;
        
        public LazySSLSocketFactory() {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null,
                                new X509TrustManager[]{
                                    new X509TrustManager() {
                                        @Override
                                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                                        }
                                        
                                        @Override
                                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                                        }
                                        
                                        @Override
                                        public X509Certificate[] getAcceptedIssuers() {
                                            return null;
                                        }
                                    }
                                },
                                new SecureRandom());
                
                delegate = sslContext.getSocketFactory();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public String[] getDefaultCipherSuites() {
            return delegate.getDefaultCipherSuites();
        }
        
        @Override
        public String[] getSupportedCipherSuites() {
            return delegate.getSupportedCipherSuites();
        }
        
        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoclose) throws IOException {
            return delegate.createSocket(socket, host, port, autoclose);
        }
        
        @Override
        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            return delegate.createSocket(host, port);
        }
        
        @Override
        public Socket createSocket(String host, int port, InetAddress localaddr, int localport) throws IOException, UnknownHostException {
            return delegate.createSocket(host, port, localaddr, localport);
        }
        
        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            return delegate.createSocket(host, port);
        }
        
        @Override
        public Socket createSocket(InetAddress addr, int port, InetAddress localaddr, int localport) throws IOException {
            return delegate.createSocket(addr, port, localaddr, localport);
        }
        
        public static SocketFactory getDefault() {
            return new LazySSLSocketFactory();
        }
    }
