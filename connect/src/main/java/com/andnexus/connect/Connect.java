package com.andnexus.connect;

import com.andnexus.model.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.andnexus.connect.Connect.ConnectException.DeliveryException;
import static com.andnexus.connect.Request.RequestBuilder;

public class Connect {

    final RequestBuilder mRequestBuilder;

    final Parser mParser;

    public Connect(String url) {
        this(new RequestBuilder(url, 8000, "/backend/data"), new Parser());
    }

    Connect(RequestBuilder requestBuilder, Parser parser) {
        mRequestBuilder = requestBuilder;
        mParser = parser;
    }

    public List<Data> getData() throws ConnectException {
        final InputStream inputStream;
        try {
            inputStream = mRequestBuilder.create().connect().getInputStream();
            return mParser.getModels(inputStream);
        } catch (IOException e) {
            throw new ConnectException(new DeliveryException(e));
        }
    }

    public static class ConnectException extends Exception {

        public ConnectException(Throwable e) {
            super(e);
        }

        public static class HostException extends Throwable {

            public HostException(Throwable cause) {
                super(cause);
            }
        }

        public static class ParserException extends Throwable {

            public ParserException(Throwable cause) {
                super(cause);
            }
        }

        public static class RequestException extends Throwable {

            public RequestException(Throwable cause) {
                super(cause);
            }
        }

        public static class DeliveryException extends Throwable {

            public DeliveryException(Throwable cause) {
                super(cause);
            }
        }
    }
}
