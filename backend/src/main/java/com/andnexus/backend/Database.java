package com.andnexus.backend;

import com.andnexus.model.Data;

public class Database {

    private static final Data[] DATA = {new Data(123), new Data(456)};

    public Data[] getData() {
        return DATA;
    }
}
