package com.example.trainningtimer;

import android.provider.BaseColumns;

public final class FeedReaderContract {

    private FeedReaderContract(){}

    public static class FeedEntry implements BaseColumns{

        public static final String NOMBRE_TABLA= "t_entrenos";
        //public static final String ID="id";
        public static final String  TIPO_ENTRENO="tipo_entreno";
        public static final String  FECHA="fecha";


    }
}
