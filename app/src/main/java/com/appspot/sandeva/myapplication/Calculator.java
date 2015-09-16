package com.appspot.sandeva.myapplication;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

public class Calculator {

    static double calculator_TYL(double A, double B, double C, double D, double T) {
        return (A + T * (B + T * (C + T * (D))));
    }

    static double calculator_dSIN(double A) {
        return Math.sin(CCalculator.deg2rad(A));
    }

    static double calculator_dCOS(double A) {
        return Math.cos(CCalculator.deg2rad(A));
    }

    static double calculator_dTAN(double A) {
        return Math.tan(CCalculator.deg2rad(A));
    }

    static double calculator_AdSIN(double A) {
        return CCalculator.rad2deg(Math.asin(A));
    }

    static double calculator_AdCOS(double A) {
        return CCalculator.rad2deg(Math.acos(A));
    }

    static double calculator_AdTAN(double sn, double cs) {
        double ang = CCalculator.rad2deg(Math.atan(sn / cs));
        if (cs > 0) {
            return ang;
        }
        return ang + 180;
    }

    static double calculator_MOD(double A, double B) {
        if (A > B) {
            return calculator_MOD(A - B, B);
        }
        if (A < 0) {
            return calculator_MOD(A + B, B);
        }
        return A;
    }


    static void GetBhava(Map<Consts.Key, Double> name) {
        double sdtm = name.get(Consts.Key.SideraelTime) / 240;
        double T = name.get(Consts.Key.T);
        double LT = name.get(Consts.Key.latitude) / 3600.0;

        double h = -120;
        double H, X, dlt;
        double a = calculator_TYL(23.452294, -0.0130125, -0.00000164, +0.000000503, T);
        for (int i = 0; i < 6; i++) {
            h += 30;
            dlt = calculator_AdSIN(calculator_dSIN(h) * calculator_dSIN(LT));

            double P = LT == 0 ? calculator_dSIN(h) : calculator_dTAN(dlt) / calculator_dTAN(LT);
            if (P < -1) {
                P = -1;
            }
            if (P > 1) {
                P = 1;
            }
            H = calculator_AdCOS(P);
            X = sdtm - H;
            X = calculator_AdTAN((calculator_dSIN(X) * calculator_dCOS(a) + calculator_dTAN(dlt) * calculator_dSIN(a)), calculator_dCOS(X));
            X = calculator_MOD(X - 90, 360) * 3600;
            name.put(Consts.getBhavas()[i], Math.floor(X));
        }
        for (int i = 6; i < 12; i++) {
            name.put(Consts.getBhavas()[i], (name.get(Consts.getBhavas()[i - 6]) + 180 * 3600) % (360 * 3600));
        }
    }

    static double GetAyanamsa(double Dt) {
        Calendar BDt = new GregorianCalendar(1900, 6, 30);
        double x = Dt - BDt.getTimeInMillis();
        x = ((50.26) * x / 365.2425 / 24 / 3600 / 1000);
        return 22 * 3600 + 21 * 60 + 37 + x;
    }

    static void GetKendraSayana(Map<Consts.Key, Double> name) {
        CCalculator.GetEphe(name);
        name.put(Consts.Key.Moon_S_Node, (name.get(Consts.Key.Moon_Node) + 180 * 3600) % (360 * 3600));
        GetBhava(name);
    }

    static void GetKendraNirayana(Map<Consts.Key, Double> name) {
        GetKendraSayana(name);
        double ayana = GetAyanamsa(name.get(Consts.Key.GMT));
        for (Consts.Key x : Consts.getPoints()) {
            name.put(x, (name.get(x) + 360 * 3600 - ayana) % (360 * 3600));
        }
    }

    static public int getVarga(Map<Consts.Key, Double> kendra, Consts.Key x, int varga) {
        int c = (int) (kendra.get(x) / (108000 / varga));
        int rashi = (int) (kendra.get(x) / 108000) % 12;

        int odd = rashi % 2;
        int cMod = c % varga;
        switch (varga) {
            case 1:
            case 6:
            case 7:
            case 8:
            case 9:
            case 11:
            case 16:
            case 20:
            case 27:
                return c % 12;

            case 2:
                return c % 12 ^ odd;

            case 3:
            case 4:
                return (rashi + 12 / varga * cMod) % 12;

            case 5:
                return new int[]{0, 10, 8, 2, 6, 1, 5, 11, 9, 7}[c % 10];

            case 10:
                return (8 * odd + rashi + cMod) % 12;

            case 12:
            case 60:
                return (rashi + cMod) % 12;

            case 24:
                return (4 - odd + cMod) % 12;

            case 30:
                int v = c % 60;
                return v < 5
                        ? 0 : v < 10
                        ? 10 : v < 8
                        ? 8 : v < 25
                        ? 2 : v < 30
                        ? 6 : v < 35
                        ? 1 : v < 42
                        ? 5 : v < 50
                        ? 11 : v < 55
                        ? 9 : 7;

            case 40:
                return (6 * odd + cMod) % 12;

            case 45:
                return (4 * rashi + cMod) % 12;
        }
        throw new InvalidParameterException();
    }
}