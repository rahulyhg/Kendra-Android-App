package com.appspot.sandeva.myapplication;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by sandeva on 15/09/22.
 */
public class Consts {
    public static final String[] Rashi = new String[]{"මේෂ", "වෘෂභ", "මිථුන", "කටක", "සිංහ", "කන්‍යා", "තුලා", "වෘශ්චික", "ධනු", "මකර", "කුම්භ", "මීන"};
    private String getRashiName(int x) {
        return Rashi[x];
    }
    static int counter=0;
    public enum Key {
        I("I", "I"),
        II("II", "II"),
        III("III", "III"),
        IV("IV", "IV"),
        V("V", "V"),
        VI("VI", "VI"),
        VII("VII", "VII"),
        VIII("VIII", "VIII"),
        IX("IX", "IX"),
        X("X", "X"),
        XI("XI", "XI"),
        XII("XII", "XII"),

        Sun("රවි", "ර"),
        Moon("චන්ද්", "ච"),
        Mars("කුජ", "කු"),
        Mercury("බුධ", "බු"),
        Jupiter("ගුරු", "ගු"),
        Venus("ශුක්", "ශු"),
        Saturn("ශනි", "ශ"),
        Moon_Node("රාහු", "රා"),
        Moon_S_Node("කේතු", "කේ"),
        Uranus("යුරේනස්", "යු"),
        Neptune("නැප්චුන්", "නැ"),
        Pluto("ප්ලුටෝ", "ප්"),

        GMT("", ""),
        Timezone("", ""),
        latitude("", ""),
        longitude("", ""),
        T("", ""),
        SideraelTime("", "");


        public String getName() {
            return name;
        }

        private String name;

        public String getShortName() {
            return shortName;
        }

        private String shortName;
        private int id;

        Key (String name, String shortName) {
            this.name = name;
            this.shortName = shortName;
            this.id = counter++;
        }
    }
    // static Key[] planets = getPlanets();
    static Key[] getPlanets() {
        return new Key[]{
                Key.Sun,
                Key.Moon,
                Key.Mars,
                Key.Mercury,
                Key.Jupiter,
                Key.Venus,
                Key.Saturn,
                Key.Moon_Node,
                Key.Moon_S_Node,
                Key.Uranus,
                Key.Neptune,
                Key.Pluto
        }; // ,Key.Chiron
    }

    // static Key[] bhavas = getBhavas();
    static Key[] getBhavas (){
        return new Key[]{
                Key.I, Key.II, Key.III, Key.IV, Key.V, Key.VI,
                Key.VII, Key.VIII, Key.IX, Key.X, Key.XI, Key.XII};
    }

    private static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }


    static Key[] getPoints(){
        return concat(getPlanets(), getBhavas());
    }
}
