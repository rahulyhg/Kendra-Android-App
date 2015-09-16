package com.appspot.sandeva.myapplication;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

public class CCalculator {
    static double TYL(double A, double B, double C, double D, double T) {
        return A + T * (B + T * (C + T * D));
    }

    static double  MOD(double a, double b) {
        return ((a % b) + b) % b;
    }

    static double  MTYL(double A, double B, double C, double D, double T) {
        return MOD(TYL(A, B, C, D, T), 360);
    }

    static double deg=180/3.141592654;
    // double PI=3.141592654;

    static double deg2rad(double x) {
        return x / deg;
    }

    static double rad2deg(double x) {
        return x * deg;
    }

    static double ATANP(double sn, double cs) {

        if (cs == 0) {
            return 90;
        }
        if (cs > 0) {
            return rad2deg(Math.atan(sn / cs));
        }
        return 180 + rad2deg(Math.atan(sn / cs));
    }

    static double E(double M, double e) {
        double E0 = M;
        double E1 = 0;
        double i = 0;
        while (E0 != E1) {
            E0 = E1;
            E1 = M + e * rad2deg(Math.sin(deg2rad(E0)));
            if (i >= 10000) {
                // echo "Error in E func",E0,E1,"<br>";
                return E1;
            }
            i = i + 1;
        }
        return E1;
    }

    static double [] GeoCentr(double L, double bita, double alp, double e, double i, double OHM, double M, double r, double EE, double bb, double th, double R) {
// 	echo L."---".bita."---".alp."---".e."---".i."---".OHM."---".M."---".r."---".EE."---".bb."---".th."---".R;
        double v = 2 * ATANP(Math.sqrt((1 + e) / (1 - e)) * Math.tan(deg2rad(EE / 2)), 1);
        v = MOD(v, 360);
        r += alp * (1 - e * Math.cos(deg2rad(EE)));
        double u = MOD(L + v - M - OHM, 360);
        double l = ATANP(Math.cos(deg2rad(i)) * Math.sin(deg2rad(u)), Math.cos(deg2rad(u))) + OHM;
        double b = rad2deg(Math.asin(Math.sin(deg2rad(u)) * Math.sin(deg2rad(i))));
        bb += b;
        b = bb;
        double N = r * Math.cos(deg2rad(b)) * Math.sin(deg2rad(l - th));
        double D = r * Math.cos(deg2rad(b)) * Math.cos(deg2rad(l - th)) + R;
        L = MOD(ATANP(N, D) + th, 360);
        double DEL = N * N + D * D + r * Math.sin(deg2rad(b)) * r * Math.sin(deg2rad(b));
        // echo "DEL==================".DEL."<br>";
        bita = rad2deg(Math.asin(r * Math.sin(deg2rad(b)) / Math.sqrt(DEL)));
        return new double[]{L, bita, r, bb};
    }

    static void GetEphe(Map<Consts.Key, Double> name) {// """,BirthTime,Sphuta,LATITUDE,LONGITUDE""")

        Calendar BDt = new GregorianCalendar(1899, 11, 31, 12, 0, 0);


        double T = name.get(Consts.Key.GMT) - BDt.getTimeInMillis();
        T = T / 36525 / 24 / 3600 / 1000;
        name.put(Consts.Key.T, T);
        double [] L = new double[20],ohm =new double[20],OHM =new double[20],M =new double[20],alp =new double[20],e =new double[20],i =new double[20],r =new double[20],EE =new double[20],MM =new double[20],b =new double[20],decli =new double[20];
        //=[],[],[],[],[],[],[],[],[],[],[],[];

        L[3] = MTYL(279.69668, +36000.76892, +0.0003025, 0, T);

        M[3] = MTYL(358.47583, +35999.04975, -0.00015, -0.0000033, T);
        e[3] = TYL(0.01675104, -0.0000418, -0.000000126, 0, T);
        double c = TYL(1.91946, -0.004789, -0.000014, 0, T) * Math.sin(deg2rad(M[3]))
                + TYL(0.020094, -0.0001, 0, 0, T) * Math.sin(deg2rad(M[3] * 2))
                + TYL(0.000293, 0, 0, 0, T) * Math.sin(deg2rad(M[3] * 3));

        L[3] = MOD(L[3] + c, 360);
        MM[3] = MOD(M[3] + c, 360);
        r[3] = 1.0000002 * (1 - e[3] * e[3]) / (1 + e[3] * Math.cos(deg2rad(MM[3])));

        c = MTYL(153.23, +22518.7541, 0, 0, T);
        L[3] += 0.00134 * Math.cos(deg2rad(c));
        r[3] += 0.00000543 * Math.sin(deg2rad(c));
        c = MTYL(216.57, +45037.5082, 0, 0, T);
        L[3] += 0.00154 * Math.cos(deg2rad(c));
        r[3] += 0.00001575 * Math.sin(deg2rad(c));
        c = MTYL(312.69, +32964.3577, 0, 0, T);
        L[3] += 0.00200 * Math.cos(deg2rad(c));
        r[3] += 0.00001627 * Math.sin(deg2rad(c));
        c = MTYL(350.74, +445267.1142, -0.00144, 0, T);
        L[3] += 0.00179 * Math.sin(deg2rad(c));
        r[3] += 0.00003076 * Math.cos(deg2rad(c));
        c = MTYL(231.19, +20.20, 0, 0, T);
        L[3] += 0.00178 * Math.sin(deg2rad(c));
        c = MTYL(353.40, +65928.7155, 0, 0, T);
        r[3] += 0.00000927 * Math.sin(deg2rad(c));

        L[3] = MOD(L[3], 360);
        name.put(Consts.Key.Sun, L[3] * 3600);

        // MEAN PLANETS

        L[1] = MTYL(178.179078, +149474.07078, +0.0003011, 0, T);
        alp[1] = 0.3870986;
        e[1] = TYL(0.20561421, +0.00002046, -0.000000030, 0, T);
        i[1] = MTYL(7.002881, +0.0018608, -0.0000183, 0, T);
        ohm[1] = MTYL(28.753753, +0.3702806, +0.0001208, 0, T);
        OHM[1] = MTYL(47.145944, +1.1852083, +0.0001739, 0, T);

        L[2] = MTYL(342.767053, +58519.21191, +0.0003097, 0, T);
        alp[2] = 0.7233316;
        e[2] = TYL(0.00682069, -0.00004774, +0.000000091, 0, T);
        i[2] = MTYL(3.393631, +0.0010058, -0.0000010, 0, T);
        ohm[2] = MTYL(54.384186, +0.5081861, -0.0013864, 0, T);
        OHM[2] = MTYL(75.779647, +0.8998500, +0.0004100, 0, T);

        L[4] = MTYL(293.737334, +19141.69551, +0.0003107, 0, T);
        alp[4] = 1.5236883;
        e[4] = TYL(0.09331290, +0.000092064, -0.000000077, 0, T);
        i[4] = MTYL(1.850333, -0.0006750, +0.0000126, 0, T);
        ohm[4] = MTYL(285.431761, +1.0697667, +0.0001313, +0.00000414, T);
        OHM[4] = MTYL(48.786442, +0.7709917, -0.0000014, -0.00000533, T);

        L[5] = MTYL(238.049257, +3036.301986, +0.0003347, -0.00000165, T);
        alp[5] = 5.202561;
        e[5] = TYL(0.04833475, +0.000164180, -0.0000004676, -0.0000000017, T);
        i[5] = MTYL(1.308736, -0.0056961, +0.0000039, 0, T);
        ohm[5] = MTYL(273.277558, +0.5994317, +0.00070405, +0.00000508, T);
        OHM[5] = MTYL(99.443414, +1.0105300, +0.00035222, -0.00000851, T);

        L[6] = MTYL(266.564377, +1223.509884, +0.0003245, -0.0000058, T);
        alp[6] = 9.554747;
        e[6] = TYL(0.05589232, -0.00034550, -0.000000728, +0.00000000074, T);
        i[6] = MTYL(2.492519, -0.0039189, -0.00001549, +0.00000004, T);
        ohm[6] = MTYL(338.307800, +1.0852207, +0.00097854, +0.00000992, T);
        OHM[6] = MTYL(112.790414, +0.8731951, -0.00015218, -0.00000531, T);

        L[7] = MTYL(244.197470, +429.863546, +0.0003160, -0.00000060, T);
        alp[7] = 19.21814;
        e[7] = TYL(0.0463444, -0.00002658, +0.000000077, 0, T);
        i[7] = MTYL(0.772464, +0.0006253, +0.0000395, 0, T);
        ohm[7] = MTYL(98.071581, +0.9857650, -0.0010745, -0.00000061, T);
        OHM[7] = MTYL(73.477111, +0.4986678, +0.0013117, 0, T);

        L[8] = MTYL(84.457994, +219.885914, +0.0003205, -0.00000060, T);
        alp[8] = 30.10957;
        e[8] = TYL(0.00899704, +0.000006330, -0.000000002, 0, T);
        i[8] = MTYL(1.779242, -0.0095436, -0.0000091, 0, T);
        ohm[8] = MTYL(276.045975, +0.3256394, +0.00014095, +0.000004113, T);
        OHM[8] = MTYL(130.681389, +1.0989350, +0.00024987, -0.000004718, T);

        M[1] = MTYL(102.27938, 149472.51529, +0.000007, 0, T);
        M[2] = MTYL(212.60322, 58517.80387, +0.001286, 0, T);
        M[4] = MTYL(319.51913, 19139.85475, +0.000181, 0, T);
        M[5] = MTYL(225.32833, 3034.69202, -0.000722, 0, T);
        M[6] = MTYL(175.46622, 1221.55147, -0.000502, 0, T);
        M[7] = MTYL(72.64878, 428.37911, +0.000079, 0, T);
        M[8] = MTYL(37.73063, 218.46134, -0.000070, 0, T);

        // CORRECTION TO MERCURY
        EE[1] = E(M[1], e[1]);
        L[1] += +0.00204 * Math.cos(deg2rad(5 * M[2] - 2 * M[1] + 12.220))
                + 0.00103 * Math.cos(deg2rad(2 * M[2] - M[1] - 160.692))
                + 0.00091 * Math.cos(deg2rad(2 * M[5] - M[1] - 37.003))
                + 0.00078 * Math.cos(deg2rad(5 * M[2] - 3 * M[1] + 10.137));
        L[1] = MOD(L[1], 360);
        r[1] = +0.000007525 * Math.cos(deg2rad(2 * M[5] - M[1] + 53.013))
                + 0.000006802 * Math.cos(deg2rad(5 * M[2] - 3 * M[1] - 259.918))
                + 0.000005457 * Math.cos(deg2rad(2 * M[2] - 2 * M[1] - 71.188))
                + 0.000003569 * Math.cos(deg2rad(5 * M[2] - M[1] - 77.75));
        b[1] = 0;
        MM[1] = M[1];

        // CORRECTION TO VENUS
        c = +0.00077 * Math.sin(deg2rad(MTYL(237.24, 150.27, 0, 0, T)));
        MM[2] = M[2] + c;
        L[2] += c;
        EE[2] = E(MM[2], e[2]);
        L[2] += +0.00313 * Math.cos(deg2rad(2 * M[3] - 2 * M[2] - 148.225))
                + 0.00198 * Math.cos(deg2rad(3 * M[3] - 3 * M[2] + 2.565))
                + 0.00136 * Math.cos(deg2rad(M[3] - M[2] - 119.107))
                + 0.00096 * Math.cos(deg2rad(3 * M[3] - 2 * M[2] - 135.912))
                + 0.00082 * Math.cos(deg2rad(M[5] - M[2] - 208.087));
        r[2] = +0.000022501 * Math.cos(deg2rad(2 * M[3] - 2 * M[2] - 58.208)) + 0.000019045 * Math.cos(deg2rad(3 * M[3] - 3 * M[2] + 92.577)) + 0.000006887 * Math.cos(deg2rad(M[5] - M[2] - 118.090)) + 0.000005172 * Math.cos(deg2rad(M[3] - M[2] - 29.110)) + 0.000003620 * Math.cos(deg2rad(5 * M[3] - 4 * M[2] - 104.208)) + 0.000003283 * Math.cos(deg2rad(4 * M[3] - 4 * M[2] + 63.513)) + 0.000003074 * Math.cos(deg2rad(2 * M[5] - 2 * M[2] - 55.167));
        b[2] = 0;

        // CORRECTION TO MARS
        c = (-0.01133 * Math.sin(deg2rad(3 * M[5] - 8 * M[4] + 4 * M[3]))
                - 0.00933 * Math.cos(deg2rad(3 * M[5] - 8 * M[4] + 4 * M[3])));
        MM[4] = M[4] + c;
        L[4] += c;
        EE[4] = E(MM[4], e[4]);
        L[4] += +0.00705 * Math.cos(deg2rad(M[5] - M[4] - 48.958)) + 0.00607 * Math.cos(deg2rad(2 * M[5] - M[4] - 188.350)) + 0.00445 * Math.cos(deg2rad(2 * M[5] - 2 * M[4] - 191.897)) + 0.00388 * Math.cos(deg2rad(M[3] - 2 * M[4] + 20.495)) + 0.00238 * Math.cos(deg2rad(M[3] - M[4] + 35.097)) + 0.00204 * Math.cos(deg2rad(2 * M[3] - 3 * M[4] + 158.638)) + 0.00177 * Math.cos(deg2rad(3 * M[4] - M[2] - 57.602)) + 0.00136 * Math.cos(deg2rad(2 * M[3] - 4 * M[4] + 154.093)) + 0.00104 * Math.cos(deg2rad(M[5] + 17.618));
        r[4] = +0.000053227 * Math.cos(deg2rad(M[5] - M[4] + 41.1306)) + 0.000050989 * Math.cos(deg2rad(2 * M[5] - 2 * M[4] - 101.9847)) + 0.000038278 * Math.cos(deg2rad(2 * M[5] - M[4] - 98.3292)) + 0.000015996 * Math.cos(deg2rad(M[3] - M[4] - 55.555)) + 0.000014764 * Math.cos(deg2rad(2 * M[3] - 3 * M[4] + 68.622)) + 0.000008966 * Math.cos(deg2rad(M[5] - 2 * M[4] + 43.615)) + 0.000007914 * Math.cos(deg2rad(3 * M[5] - 2 * M[4] - 139.737)) + 0.000007004 * Math.cos(deg2rad(2 * M[5] - 3 * M[4] - 102.888)) + 0.000006620 * Math.cos(deg2rad(M[3] - 2 * M[4] + 113.202)) + 0.000004930 * Math.cos(deg2rad(3 * M[5] - 3 * M[4] - 76.243)) + 0.000004693 * Math.cos(deg2rad(3 * M[3] - 5 * M[4] + 190.603)) + 0.000004571 * Math.cos(deg2rad(2 * M[3] - 4 * M[4] + 244.702)) + 0.000004409 * Math.cos(deg2rad(3 * M[5] - M[4] - 115.828));
        b[4] = 0;

        //CORRECTION TO JUPITER
        // vj,Pj,Qj,Sj,Vj,Wj,Xj,Aj,Bj,Yj;

        double vj = TYL(0.1, 0.2, 0, 0, T);
        double Pj = MTYL(237.47555, 3034.9061, 0, 0, T);
        double Qj = MTYL(265.91650, 1222.1139, 0, 0, T);
        double Sj = MTYL(243.51721, 428.4677, 0, 0, T);
        double Vj = MOD(5 * Qj - 2 * Pj, 360);
        double Wj = MOD(2 * Pj - 6 * Qj + 3 * Sj, 360);
        double Xj = MOD(Qj - Pj, 360);
        double Yj = MOD(Sj - Qj, 360);

        double Aj = (
                +(0.331364 - 0.010281 * vj - 0.004692 * vj * vj) * Math.sin(deg2rad(Vj))
                        + (0.003228 - 0.064436 * vj + 0.002075 * vj * vj) * Math.cos(deg2rad(Vj))
                        - (0.003083 + 0.000275 * vj - 0.000489 * vj * vj) * Math.sin(deg2rad(2 * Vj))
                        + 0.002472 * Math.sin(deg2rad(Wj))
                        + 0.013619 * Math.sin(deg2rad(Xj))
                        + 0.018472 * Math.sin(deg2rad(Xj * 2))
                        + 0.006717 * Math.sin(deg2rad(Xj * 3))
                        + 0.002775 * Math.sin(deg2rad(Xj * 4))
                        + (0.007275 - 0.001253 * vj) * Math.sin(deg2rad(Xj)) * Math.sin(deg2rad(Qj))
                        + 0.006417 * Math.sin(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj))
                        + 0.002439 * Math.sin(deg2rad(Xj * 3)) * Math.sin(deg2rad(Qj))
                        - (0.033839 + 0.001125 * vj) * Math.cos(deg2rad(Xj)) * Math.sin(deg2rad(Qj))
                        - 0.003767 * Math.cos(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj))
                        - (0.035681 + 0.001208 * vj) * Math.sin(deg2rad(Xj)) * Math.cos(deg2rad(Qj))
                        - 0.004261 * Math.sin(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj))
                        + 0.002178 * Math.cos(deg2rad(Qj))
                        + (-0.006333 + 0.001161 * vj) * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj))
                        - 0.006675 * Math.cos(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj))
                        - 0.002664 * Math.cos(deg2rad(Xj * 3)) * Math.cos(deg2rad(Qj))
                        - 0.002572 * Math.sin(deg2rad(Xj)) * Math.sin(deg2rad(Qj * 2))
                        - 0.003567 * Math.sin(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj * 2))
                        + 0.002094 * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj * 2))
                        + 0.003342 * Math.cos(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj * 2))
        );
        double Bj = (
                +(0.007192 - 0.003147 * vj) * Math.sin(deg2rad(Vj))
                        + (-0.020428 - 0.000675 * vj + 0.000197 * vj * vj) * Math.cos(deg2rad(Vj))
                        + (0.007269 + 0.000672 * vj) * Math.sin(deg2rad(Xj)) * Math.sin(deg2rad(Qj))
                        - 0.004344 * Math.sin(deg2rad(Qj))
                        + 0.034036 * Math.cos(deg2rad(Xj)) * Math.sin(deg2rad(Qj))
                        + 0.005614 * Math.cos(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj))
                        + 0.002964 * Math.cos(deg2rad(Xj * 3)) * Math.sin(deg2rad(Qj))
                        + 0.037761 * Math.sin(deg2rad(Xj)) * Math.cos(deg2rad(Qj))
                        + 0.006158 * Math.sin(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj))
                        - 0.006603 * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj))
                        - 0.005356 * Math.sin(deg2rad(Xj)) * Math.sin(deg2rad(Qj * 2))
                        + 0.002722 * Math.sin(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj * 2))
                        + 0.004483 * Math.cos(deg2rad(Xj)) * Math.sin(deg2rad(Qj * 2))
                        - 0.002642 * Math.cos(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj * 2))
                        + 0.004403 * Math.sin(deg2rad(Xj)) * Math.cos(deg2rad(Qj * 2))
                        - 0.002536 * Math.sin(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj * 2))
                        + 0.005547 * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj * 2))
                        - 0.002689 * Math.cos(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj * 2))
        );
        MM[5] = M[5] + Aj - Bj / e[5];

        e[5] += (
                (3606 + 130 * vj - 43 * vj * vj) * Math.sin(deg2rad(Vj))
                        + (1289 - 580 * vj) * Math.cos(deg2rad(Vj))
                        - 6764 * Math.sin(deg2rad(Xj)) * Math.sin(deg2rad(Qj))
                        - 1110 * Math.sin(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj))
                        - 224 * Math.sin(deg2rad(Xj * 3)) * Math.sin(deg2rad(Qj))
                        - 204 * Math.sin(deg2rad(Qj))
                        + (1284 + 116 * vj) * Math.cos(deg2rad(Xj)) * Math.sin(deg2rad(Qj))
                        + 188 * Math.cos(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj))
                        + (1460 + 130 * vj) * Math.sin(deg2rad(Xj)) * Math.cos(deg2rad(Qj))
                        + 224 * Math.sin(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj))
                        - 817 * Math.cos(deg2rad(Qj))
                        + 6074 * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj))
                        + 992 * Math.cos(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj))
                        + 508 * Math.cos(deg2rad(Xj * 3)) * Math.cos(deg2rad(Qj))
                        + 230 * Math.cos(deg2rad(Xj * 4)) * Math.cos(deg2rad(Qj))
                        + 108 * Math.cos(deg2rad(Xj * 5)) * Math.cos(deg2rad(Qj))
                        - (956 + 73 * vj) * Math.sin(deg2rad(Xj)) * Math.sin(deg2rad(Qj * 2))
                        + 448 * Math.sin(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj * 2))
                        + 137 * Math.sin(deg2rad(Xj * 3)) * Math.sin(deg2rad(Qj * 2))
                        + (-997 + 108 * vj) * Math.cos(deg2rad(Xj)) * Math.sin(deg2rad(Qj * 2))
                        + 480 * Math.cos(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj * 2))
                        + 148 * Math.cos(deg2rad(Xj * 3)) * Math.sin(deg2rad(Qj * 2))
                        + (-956 + 99 * vj) * Math.sin(deg2rad(Xj)) * Math.cos(deg2rad(Qj * 2))
                        + 490 * Math.sin(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj * 2))
                        + 158 * Math.sin(deg2rad(Xj * 3)) * Math.cos(deg2rad(Qj * 2))
                        + 179 * Math.cos(deg2rad(Qj * 2))
                        + (1024 + 75 * vj) * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj * 2))
                        - 437 * Math.cos(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj * 2))
                        - 132 * Math.cos(deg2rad(Xj * 3)) * Math.cos(deg2rad(Qj * 2))
        ) / 10000000;

        alp[5] += (
                -263 * Math.cos(deg2rad(Vj))
                        + 205 * Math.cos(deg2rad(Xj))
                        + 693 * Math.cos(deg2rad(Xj * 2))
                        + 312 * Math.cos(deg2rad(Xj * 3))
                        + 147 * Math.cos(deg2rad(Xj * 4))
                        + 299 * Math.sin(deg2rad(Xj)) * Math.sin(deg2rad(Qj))
                        + 181 * Math.cos(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj))
                        + 204 * Math.sin(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj))
                        + 111 * Math.sin(deg2rad(Xj * 3)) * Math.cos(deg2rad(Qj))
                        - 337 * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj))
                        - 111 * Math.cos(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj))
        ) / 1000000;

        L[5] += Aj;
        ohm[5] += Bj;
        r[5] = 0;
        b[5] = 0;
        EE[5] = E(MM[5], e[5]);


        //CORRECTION TO SATURN
        Aj = (
                +(-0.814181 + 0.018150 * vj + 0.016714 * vj * vj) * Math.sin(deg2rad(Vj))
                        + (-0.010497 + 0.160906 * vj - 0.004100 * vj * vj) * Math.cos(deg2rad(Vj))
                        + 0.007581 * Math.sin(deg2rad(Vj * 2))
                        - 0.007986 * Math.sin(deg2rad(Wj))
                        - 0.148811 * Math.sin(deg2rad(Xj))
                        - 0.040786 * Math.sin(deg2rad(Xj * 2))
                        - 0.015208 * Math.sin(deg2rad(Xj * 3))
                        - 0.006339 * Math.sin(deg2rad(Xj * 4))
                        - 0.006244 * Math.sin(deg2rad(Qj))
                        + (0.008931 + 0.002728 * vj) * Math.sin(deg2rad(Xj)) * Math.sin(deg2rad(Qj))
                        - 0.016500 * Math.sin(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj))
                        - 0.005775 * Math.sin(deg2rad(Xj * 3)) * Math.sin(deg2rad(Qj))
                        + (0.081344 + 0.003206 * vj) * Math.cos(deg2rad(Xj)) * Math.sin(deg2rad(Qj))
                        + 0.015019 * Math.cos(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj))
                        + (0.085581 + 0.002494 * vj) * Math.sin(deg2rad(Xj)) * Math.cos(deg2rad(Qj))
                        + (0.025328 - 0.003117 * vj) * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj))
                        + 0.014394 * Math.cos(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj))
                        + 0.006319 * Math.cos(deg2rad(Xj * 3)) * Math.cos(deg2rad(Qj))
                        + 0.006369 * Math.sin(deg2rad(Xj)) * Math.sin(deg2rad(Qj * 2))
                        + 0.009156 * Math.sin(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj * 2))
                        + 0.007525 * Math.sin(deg2rad(Yj * 3)) * Math.sin(deg2rad(Qj * 2))
                        - 0.005236 * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj * 2))
                        - 0.007736 * Math.cos(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj * 2))
                        - 0.007528 * Math.cos(deg2rad(Yj * 3)) * Math.cos(deg2rad(Qj * 2))
        );
        Bj = (
                +(0.077108 + 0.007186 * vj - 0.001533 * vj * vj) * Math.sin(deg2rad(Vj))
                        + (0.045803 - 0.014766 * vj - 0.000536 * vj * vj) * Math.cos(deg2rad(Vj))
                        - 0.007075 * Math.sin(deg2rad(Xj))
                        - 0.075825 * Math.sin(deg2rad(Xj)) * Math.sin(deg2rad(Qj))
                        - 0.024839 * Math.sin(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj))
                        - 0.008631 * Math.sin(deg2rad(Xj * 3)) * Math.sin(deg2rad(Qj))
                        - 0.072586 * Math.cos(deg2rad(Qj))
                        - 0.150383 * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj))
                        + 0.026897 * Math.cos(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj))
                        + 0.010053 * Math.cos(deg2rad(Xj * 3)) * Math.cos(deg2rad(Qj))
                        - (0.013597 + 0.001719 * vj) * Math.sin(deg2rad(Xj)) * Math.sin(deg2rad(Qj * 2))
                        + (-0.007742 + 0.001517 * vj) * Math.cos(deg2rad(Xj)) * Math.sin(deg2rad(Qj * 2))
                        + (0.013586 - 0.001375 * vj) * Math.cos(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj * 2))
                        + (-0.013667 + 0.001239 * vj) * Math.sin(deg2rad(Xj)) * Math.cos(deg2rad(Qj * 2))
                        + 0.011981 * Math.sin(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj * 2))
                        + (0.014861 + 0.001136 * vj) * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj * 2))
                        - (0.013064 + 0.001628 * vj) * Math.cos(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj * 2))
        );
        MM[6] = M[6] + Aj - Bj / e[6];

        e[6] += (
                (-7927 + 2548 * vj + 91 * vj * vj) * Math.sin(deg2rad(Vj))
                        + (13381 + 1226 * vj - 253 * vj * vj) * Math.cos(deg2rad(Vj))
                        + (248 - 121 * vj) * Math.sin(deg2rad(Vj * 2))
                        - (305 + 91 * vj) * Math.cos(deg2rad(Vj * 2))
                        + 412 * Math.sin(deg2rad(Xj * 2))
                        + 12415 * Math.sin(deg2rad(Qj))
                        + (390 - 617 * vj) * Math.sin(deg2rad(Xj)) * Math.sin(deg2rad(Qj))
                        + (165 - 204 * vj) * Math.sin(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj))
                        + 26599 * Math.cos(deg2rad(Xj)) * Math.sin(deg2rad(Qj))
                        - 4687 * Math.cos(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj))
                        - 1870 * Math.cos(deg2rad(Xj * 3)) * Math.sin(deg2rad(Qj))
                        - 821 * Math.cos(deg2rad(Xj * 4)) * Math.sin(deg2rad(Qj))
                        - 377 * Math.cos(deg2rad(Xj * 5)) * Math.sin(deg2rad(Qj))
                        + 497 * Math.cos(deg2rad(Yj * 2)) * Math.sin(deg2rad(Qj))
                        + (163 - 611 * vj) * Math.cos(deg2rad(Qj))
                        - 12696 * Math.sin(deg2rad(Xj)) * Math.cos(deg2rad(Qj))
                        - 4200 * Math.sin(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj))
                        - 1503 * Math.sin(deg2rad(Xj * 3)) * Math.cos(deg2rad(Qj))
                        - 619 * Math.sin(deg2rad(Xj * 4)) * Math.cos(deg2rad(Qj))
                        - 268 * Math.sin(deg2rad(Xj * 5)) * Math.cos(deg2rad(Qj))
                        - (282 + 1306 * vj) * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj))
                        + (-86 + 230 * vj) * Math.cos(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj))
                        + 461 * Math.sin(deg2rad(Yj * 2)) * Math.cos(deg2rad(Qj))
                        - 350 * Math.sin(deg2rad(Qj * 2))
                        + (2211 - 286 * vj) * Math.sin(deg2rad(Xj)) * Math.sin(deg2rad(Qj * 2))
                        - 2208 * Math.sin(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj * 2))
                        - 568 * Math.sin(deg2rad(Xj * 3)) * Math.sin(deg2rad(Qj * 2))
                        - 346 * Math.sin(deg2rad(Xj * 4)) * Math.sin(deg2rad(Qj * 2))
                        - (2780 + 222 * vj) * Math.cos(deg2rad(Xj)) * Math.sin(deg2rad(Qj * 2))
                        + (2022 + 263 * vj) * Math.cos(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj * 2))
                        + 248 * Math.cos(deg2rad(Xj * 3)) * Math.sin(deg2rad(Qj * 2))
                        + 242 * Math.sin(deg2rad(Yj * 3)) * Math.sin(deg2rad(Qj * 2))
                        + 467 * Math.cos(deg2rad(Yj * 3)) * Math.sin(deg2rad(Qj * 2))
                        - 490 * Math.cos(deg2rad(Qj * 2))
                        - (2842 + 279 * vj) * Math.sin(deg2rad(Xj)) * Math.cos(deg2rad(Qj * 2))
                        + (128 + 226 * vj) * Math.sin(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj * 2))
                        + 224 * Math.sin(deg2rad(Xj * 3)) * Math.cos(deg2rad(Qj * 2))
                        + (-1594 + 282 * vj) * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj * 2))
                        + (2162 - 207 * vj) * Math.cos(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj * 2))
                        + 561 * Math.cos(deg2rad(Xj * 3)) * Math.cos(deg2rad(Qj * 2))
                        + 343 * Math.cos(deg2rad(Xj * 4)) * Math.cos(deg2rad(Qj * 2))
                        + 469 * Math.sin(deg2rad(Yj * 3)) * Math.cos(deg2rad(Qj * 2))
                        - 242 * Math.cos(deg2rad(Yj * 3)) * Math.cos(deg2rad(Qj * 2))
                        - 205 * Math.sin(deg2rad(Xj)) * Math.sin(deg2rad(Qj * 3))
                        + 262 * Math.sin(deg2rad(Xj * 3)) * Math.sin(deg2rad(Qj * 3))
                        + 208 * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj * 3))
                        - 271 * Math.cos(deg2rad(Xj * 3)) * Math.cos(deg2rad(Qj * 3))
                        - 382 * Math.cos(deg2rad(Xj * 3)) * Math.sin(deg2rad(Qj * 4))
                        - 376 * Math.sin(deg2rad(Xj * 3)) * Math.cos(deg2rad(Qj * 4))
        ) / 10000000;

        alp[6] += (
                +572 * vj * Math.sin(deg2rad(Vj))
                        + 2933 * Math.cos(deg2rad(Vj))
                        + 33629 * Math.cos(deg2rad(Xj))
                        - 3081 * Math.cos(deg2rad(Xj * 2))
                        - 1423 * Math.cos(deg2rad(Xj * 3))
                        - 671 * Math.cos(deg2rad(Xj * 4))
                        - 320 * Math.cos(deg2rad(Xj * 5))
                        + 1098 * Math.sin(deg2rad(Qj))
                        - 2812 * Math.sin(deg2rad(Xj)) * Math.sin(deg2rad(Qj))
                        + 688 * Math.sin(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj))
                        - 393 * Math.sin(deg2rad(Xj * 3)) * Math.sin(deg2rad(Qj))
                        - 228 * Math.sin(deg2rad(Xj * 4)) * Math.sin(deg2rad(Qj))
                        + 2138 * Math.cos(deg2rad(Xj)) * Math.sin(deg2rad(Qj))
                        - 999 * Math.cos(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj))
                        - 642 * Math.cos(deg2rad(Xj * 3)) * Math.sin(deg2rad(Qj))
                        - 325 * Math.cos(deg2rad(Xj * 4)) * Math.sin(deg2rad(Qj))
                        - 890 * Math.cos(deg2rad(Qj))
                        + 2206 * Math.sin(deg2rad(Xj)) * Math.cos(deg2rad(Qj))
                        - 1590 * Math.sin(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj))
                        - 647 * Math.sin(deg2rad(Xj * 3)) * Math.cos(deg2rad(Qj))
                        - 344 * Math.sin(deg2rad(Xj * 4)) * Math.cos(deg2rad(Qj))
                        + 2885 * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj))
                        + (2172 + 102 * vj) * Math.cos(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj))
                        + 296 * Math.cos(deg2rad(Xj * 3)) * Math.cos(deg2rad(Qj))
                        - 267 * Math.sin(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj * 2))
                        - 778 * Math.cos(deg2rad(Xj)) * Math.sin(deg2rad(Qj * 2))
                        + 495 * Math.cos(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj * 2))
                        + 250 * Math.cos(deg2rad(Xj * 3)) * Math.sin(deg2rad(Qj * 2))
                        - 856 * Math.sin(deg2rad(Xj)) * Math.cos(deg2rad(Qj * 2))
                        + 441 * Math.sin(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj * 2))
                        + 296 * Math.cos(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj * 2))
                        + 211 * Math.cos(deg2rad(Xj * 3)) * Math.cos(deg2rad(Qj * 2))
                        - 427 * Math.sin(deg2rad(Xj)) * Math.sin(deg2rad(Qj * 3))
                        + 398 * Math.sin(deg2rad(Xj * 3)) * Math.sin(deg2rad(Qj * 3))
                        + 344 * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj * 3))
                        - 427 * Math.cos(deg2rad(Xj * 3)) * Math.cos(deg2rad(Qj * 3))
        ) / 1000000;

        L[6] += Aj;
        ohm[6] += Bj;
        r[6] = 0;
        EE[6] = E(MM[6], e[6]);
        b[6] = (
                +0.000747 * Math.cos(deg2rad(Xj)) * Math.sin(deg2rad(Qj))
                        + 0.001069 * Math.cos(deg2rad(Xj)) * Math.cos(deg2rad(Qj))
                        + 0.002108 * Math.sin(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj * 2))
                        + 0.001261 * Math.cos(deg2rad(Xj * 2)) * Math.sin(deg2rad(Qj * 2))
                        + 0.001236 * Math.sin(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj * 2))
                        - 0.002075 * Math.cos(deg2rad(Xj * 2)) * Math.cos(deg2rad(Qj * 2))
        );
        //CORRECTION TO URANUS
        // Gu,Hu,Nu,TH;
        double Gu = MTYL(83.76922, 218.4907, 0, 0, T);
        double Hu = MOD(2 * Gu - Sj, 360);
        Xj = MOD(Sj - Pj, 360);
        double Nu = MOD(Sj - Qj, 360);
        double TH = MOD(Gu - Sj, 360);

        Aj = (
                +(0.864319 - 0.001583 * vj) * Math.sin(deg2rad(Hu))
                        + (0.082222 - 0.006833 * vj) * Math.cos(deg2rad(Hu))
                        + 0.036017 * Math.sin(deg2rad(Hu * 2))
                        - 0.003019 * Math.cos(deg2rad(Hu * 2))
                        + 0.008122 * Math.sin(deg2rad(Wj))
        );

        Bj = (
                +0.120303 * Math.sin(deg2rad(Hu))
                        + (0.019472 - 0.000947 * vj) * Math.cos(deg2rad(Hu))
                        + 0.006197 * Math.sin(deg2rad(Hu * 2))
        );

        MM[7] = M[7] + Aj - Bj / e[7];

        e[7] += (
                +(-3349 + 163 * vj) * Math.sin(deg2rad(Hu))
                        + 20981 * Math.cos(deg2rad(Hu))
                        + 1311 * Math.cos(deg2rad(Hu * 2))
        ) / 10000000;

        alp[7] += -0.003825 * Math.cos(deg2rad(Hu));

        L[7] += Aj;
        ohm[7] += Bj;
        EE[7] = E(MM[7], e[7]);

        L[7] += (
                +(0.010122 - 0.000988 * vj) * Math.sin(deg2rad(Sj + Nu))
                        + (-0.038581 + 0.002031 * vj - 0.001910 * vj * vj) * Math.cos(deg2rad(Sj + Nu))
                        + (0.034964 - 0.001038 * vj + 0.000868 * vj * vj) * Math.cos(deg2rad(Sj * 2 + Nu))
                        + 0.005594 * Math.sin(deg2rad(Sj + TH * 3))
                        - 0.014808 * Math.sin(deg2rad(Xj))
                        - 0.005794 * Math.sin(deg2rad(Nu))
                        + 0.002347 * Math.cos(deg2rad(Nu))
                        + 0.009872 * Math.sin(deg2rad(TH))
                        + 0.008803 * Math.sin(deg2rad(TH * 2))
                        - 0.004308 * Math.sin(deg2rad(TH * 3))
        );
        r[7] = (
                -25948
                        + 4985 * Math.cos(deg2rad(Xj))
                        - 1230 * Math.cos(deg2rad(Sj))
                        + 3354 * Math.cos(deg2rad(Nu))
                        + (5795 * Math.cos(deg2rad(Sj)) - 1165 * Math.sin(deg2rad(Sj)) + 1388 * Math.cos(deg2rad(Sj * 2))) * Math.sin(deg2rad(Nu))
                        + (1351 * Math.cos(deg2rad(Sj)) + 5702 * Math.sin(deg2rad(Sj)) + 1388 * Math.sin(deg2rad(Sj * 2))) * Math.cos(deg2rad(Nu))
                        + 904 * Math.cos(deg2rad(TH * 2))
                        + 894 * (Math.cos(deg2rad(TH)) - Math.cos(deg2rad(TH * 3)))
        ) / 1000000;

        b[7] = (
                +0.000458 * Math.sin(deg2rad(Nu)) * Math.sin(deg2rad(Sj))
                        - 0.000642 * Math.cos(deg2rad(Nu)) * Math.sin(deg2rad(Sj))
                        - 0.000517 * Math.cos(deg2rad(TH * 4)) * Math.sin(deg2rad(Sj))
                        - 0.000347 * Math.sin(deg2rad(Nu)) * Math.cos(deg2rad(Sj))
                        - 0.000853 * Math.cos(deg2rad(Nu)) * Math.cos(deg2rad(Sj))
                        - 0.000517 * Math.sin(deg2rad(Nu * 4)) * Math.cos(deg2rad(Sj))
                        + 0.000403 * (Math.cos(deg2rad(TH * 2)) * Math.sin(deg2rad(Sj * 2)) + Math.sin(deg2rad(TH * 2)) * Math.cos(deg2rad(Sj * 2)))
        );
        //Neptune
        Xj = MOD(Gu - Pj, 360);
        Nu = MOD(Gu - Qj, 360);
        TH = MOD(Gu - Sj, 360);

        Aj = (
                +(-0.589833 + 0.001089 * vj) * Math.sin(deg2rad(Hu))
                        + (-0.056094 + 0.004658 * vj) * Math.cos(deg2rad(Hu))
                        - 0.024286 * Math.sin(deg2rad(Hu * 2))
        );
        Bj = (
                +0.024039 * Math.sin(deg2rad(Hu))
                        - 0.025303 * Math.cos(deg2rad(Hu))
                        + 0.006206 * Math.sin(deg2rad(Hu * 2))
                        - 0.005992 * Math.cos(deg2rad(Hu * 2))
        );
        MM[8] = M[8] + Aj - Bj / e[8];

        e[8] += (
                +4389 * Math.sin(deg2rad(Hu))
                        + 4262 * Math.cos(deg2rad(Hu))
                        + 1129 * Math.sin(deg2rad(Hu * 2))
                        + 1089 * Math.cos(deg2rad(Hu * 2))
        ) / 10000000;

        alp[8] += (
                -817 * Math.sin(deg2rad(Hu))
                        + 8189 * Math.cos(deg2rad(Hu))
                        + 781 * Math.cos(deg2rad(Hu * 2))
        ) / 1000000;

        L[8] += Aj;
        ohm[8] += Bj;
        EE[8] = E(MM[8], e[8]);

        L[8] += (
                -0.009556 * Math.sin(deg2rad(Xj))
                        - 0.005178 * Math.sin(deg2rad(Nu))
                        + 0.002572 * Math.sin(deg2rad(TH * 2))
                        - 0.002972 * Math.cos(deg2rad(TH * 2)) * Math.sin(deg2rad(Gu))
                        - 0.002833 * Math.sin(deg2rad(TH * 2)) * Math.cos(deg2rad(Gu))
        );
        r[8] = (
                -40596
                        + 4992 * Math.cos(deg2rad(Xj))
                        + 2744 * Math.cos(deg2rad(Nu))
                        + 2044 * Math.cos(deg2rad(TH))
                        + 1051 * Math.cos(deg2rad(TH * 2))
        ) / 1000000;

        b[8] = (
                +0.000336 * Math.cos(deg2rad(TH * 2)) * Math.sin(deg2rad(Gu))
                        + 0.000364 * Math.sin(deg2rad(TH * 2)) * Math.cos(deg2rad(Gu))
        );
        for (int k = 1; k < 9; k++) {
            if (k != 3) {
                double [] refs = GeoCentr(L[k], decli[k], alp[k], e[k], i[k], OHM[k], MM[k], r[k], EE[k], b[k], L[3], r[3]);
                L[k] = refs[0];
                decli[k] = refs[1];
                r[k] = refs[2];
                b[k] = refs[3];
            }
        }

        name.put(Consts.Key.Mercury,  MOD(L[1], 360) * 3600);
        // name.put(Consts.Key.Mercury.Decli,  MOD(decli[1], 360) * 3600);
        name.put(Consts.Key.Venus,  MOD(L[2], 360) * 3600);
        // name.put(Consts.Key.Venus.Decli,  MOD(decli[2], 360) * 3600);
        name.put(Consts.Key.Mars,  MOD(L[4], 360) * 3600);
//         name.put(Consts.Key.Mars.Decli,  MOD(decli[4], 360) * 3600);
        name.put(Consts.Key.Jupiter,  MOD(L[5], 360) * 3600);
//         name.put(Consts.Key.Jupiter.Decli,  MOD(decli[5], 360) * 3600);
        name.put(Consts.Key.Saturn,  MOD(L[6], 360) * 3600);
//         name.put(Consts.Key.Saturn.Decli,  MOD(decli[6], 360) * 3600);
        name.put(Consts.Key.Uranus,  MOD(L[7], 360) * 3600);
//         name.put(Consts.Key.Uranus.Decli,  MOD(decli[7], 360) * 3600);
        name.put(Consts.Key.Neptune,  MOD(L[8], 360) * 3600);
//         name.put(Consts.Key.Neptune.Decli,  MOD(decli[8], 360) * 3600);

//	 Dm,Fm,em,e2;
        L[9] = MTYL(270.434164, +481267.8831, -0.001133, +0.0000019, T);
//	M[3]=MTYL(		358.475833,	+35999.0498,	-0.000150,		-0.0000033);
        M[9] = MTYL(296.104608, +477198.8491, +0.009192, +0.0000144, T);
        double Dm = MTYL(350.737486, +445267.1142, -0.001436, +0.0000019, T);
        double Fm = MTYL(11.250889, +483202.0251, -0.003211, -0.0000003, T);
        OHM[9] = MTYL(259.183275, -1934.1420, +0.002078, +0.0000022, T);

        MM[3] = M[3];


        L[9] += +0.000233 * Math.sin(deg2rad(51.2 + 20.2 * T));
        MM[3] += -0.001778 * Math.sin(deg2rad(51.2 + 20.2 * T));
        M[9] += +0.000817 * Math.sin(deg2rad(51.2 + 20.2 * T));
        Dm += +0.002011 * Math.sin(deg2rad(51.2 + 20.2 * T));
        Xj = +0.003964 * Math.sin(deg2rad(MTYL(346.560, 132.870, -0.0091731, 0, T)));
        L[9] += +0.001964 * Math.sin(deg2rad(OHM[9]));
        M[9] += +0.002541 * Math.sin(deg2rad(OHM[9]));
        Dm += +0.001964 * Math.sin(deg2rad(OHM[9]));
        Fm += -0.024691 * Math.sin(deg2rad(OHM[9]));
        Fm += -0.004328 * Math.sin(deg2rad(OHM[9] + 275.05 - 2.30 * T));
        L[9] += Xj;
        M[9] += Xj;
        Dm += Xj;
        Fm += Xj;

        double em = TYL(1, -0.002495, -0.00000752, 0, T);
        double e2 = em * em;
        L[9] += (
                +6.288750 * Math.sin(deg2rad(M[9]))
                        + 1.274018 * Math.sin(deg2rad(2 * Dm - M[9]))
                        + 0.658309 * Math.sin(deg2rad(2 * Dm))
                        + 0.213616 * Math.sin(deg2rad(2 * M[9]))
                        - 0.185596 * Math.sin(deg2rad(MM[3])) * em
                        - 0.114336 * Math.sin(deg2rad(2 * Fm))
                        + 0.058793 * Math.sin(deg2rad(2 * Dm - 2 * M[9]))
                        + 0.057212 * Math.sin(deg2rad(2 * Dm - MM[3] - M[9])) * em
                        + 0.053320 * Math.sin(deg2rad(2 * Dm + M[9]))
                        + 0.045874 * Math.sin(deg2rad(2 * Dm - MM[3])) * em
                        + 0.041024 * Math.sin(deg2rad(M[9] - MM[3])) * em
                        - 0.034718 * Math.sin(deg2rad(Dm))
                        - 0.030465 * Math.sin(deg2rad(MM[3] + M[9])) * em
                        + 0.015326 * Math.sin(deg2rad(2 * Dm - 2 * Fm))
                        - 0.012528 * Math.sin(deg2rad(2 * Fm + M[9]))
                        - 0.010980 * Math.sin(deg2rad(2 * Fm - M[9]))
                        + 0.010674 * Math.sin(deg2rad(4 * Dm - M[9]))
                        + 0.010034 * Math.sin(deg2rad(3 * M[9]))
                        + 0.008548 * Math.sin(deg2rad(4 * Dm - 2 * M[9]))
                        - 0.007910 * Math.sin(deg2rad(MM[3] - M[9] + 2 * Dm)) * em
                        - 0.006783 * Math.sin(deg2rad(2 * Dm + MM[3])) * em
                        + 0.005162 * Math.sin(deg2rad(M[9] - Dm))
                        + 0.005000 * Math.sin(deg2rad(MM[3] + Dm)) * em
                        + 0.004049 * Math.sin(deg2rad(M[9] - MM[3] + 2 * Dm)) * em
                        + 0.003996 * Math.sin(deg2rad(2 * M[9] + 2 * Dm))
                        + 0.003862 * Math.sin(deg2rad(4 * Dm))
                        + 0.003665 * Math.sin(deg2rad(2 * Dm - 3 * M[9]))
                        + 0.002695 * Math.sin(deg2rad(2 * M[9] - MM[3])) * em
                        + 0.002602 * Math.sin(deg2rad(M[9] - 2 * Fm - 2 * Dm))
                        + 0.002396 * Math.sin(deg2rad(2 * Dm - MM[3] - 2 * M[9])) * em
                        - 0.002349 * Math.sin(deg2rad(M[9] + Dm))
                        + 0.002249 * Math.sin(deg2rad(2 * Dm - 2 * MM[3])) * e2
                        - 0.002125 * Math.sin(deg2rad(2 * M[9] + MM[3])) * em
                        - 0.002079 * Math.sin(deg2rad(2 * MM[3])) * e2
                        + 0.002059 * Math.sin(deg2rad(2 * Dm - M[9] - 2 * MM[3])) * e2
                        - 0.001773 * Math.sin(deg2rad(M[9] + 2 * Dm - 2 * Fm))
                        - 0.001595 * Math.sin(deg2rad(2 * Fm + 2 * Dm))
                        + 0.001220 * Math.sin(deg2rad(4 * Dm - MM[3] - M[9])) * em
                        - 0.001110 * Math.sin(deg2rad(2 * M[9] + 2 * Fm))
                        + 0.000892 * Math.sin(deg2rad(M[9] - 3 * Dm))
                        - 0.000811 * Math.sin(deg2rad(MM[3] + M[9] + 2 * Dm)) * em
                        + 0.000761 * Math.sin(deg2rad(4 * Dm - MM[3] - 2 * M[9])) * em
                        + 0.000717 * Math.sin(deg2rad(M[9] - 2 * MM[3])) * e2
                        + 0.000704 * Math.sin(deg2rad(M[9] - 2 * MM[3] - 2 * Dm)) * e2
                        + 0.000693 * Math.sin(deg2rad(MM[3] - 2 * M[9] + 2 * Dm)) * em
                        + 0.000598 * Math.sin(deg2rad(2 * Dm - MM[3] - 2 * Fm)) * em
                        + 0.000550 * Math.sin(deg2rad(M[9] + 4 * Dm))
                        + 0.000538 * Math.sin(deg2rad(4 * M[9]))
                        + 0.000521 * Math.sin(deg2rad(4 * Dm - MM[3])) * em
                        + 0.000486 * Math.sin(deg2rad(2 * M[9] - Dm))
        );
        double Bm = (
                +5.128189 * Math.sin(deg2rad(Fm))
                        + 0.280606 * Math.sin(deg2rad(M[9] + Fm))
                        + 0.277693 * Math.sin(deg2rad(M[9] - Fm))
                        + 0.173238 * Math.sin(deg2rad(2 * Dm - Fm))
                        + 0.055413 * Math.sin(deg2rad(2 * Dm + Fm - M[9]))
                        + 0.046272 * Math.sin(deg2rad(2 * Dm - Fm - M[9]))
                        + 0.032573 * Math.sin(deg2rad(2 * Dm + Fm))
                        + 0.017198 * Math.sin(deg2rad(2 * M[9] + Fm))
                        + 0.009267 * Math.sin(deg2rad(2 * Dm + M[9] - Fm))
                        + 0.008823 * Math.sin(deg2rad(2 * M[9] - Fm))
                        + 0.008247 * Math.sin(deg2rad(2 * Dm - MM[3] - Fm)) * em
                        + 0.004323 * Math.sin(deg2rad(2 * Dm - Fm - 2 * M[9]))
                        + 0.004200 * Math.sin(deg2rad(2 * Dm + Fm + M[9]))
                        + 0.003372 * Math.sin(deg2rad(Fm - MM[3] - 2 * Dm)) * em
                        + 0.002472 * Math.sin(deg2rad(2 * Dm + Fm - MM[3] - M[9])) * em
                        + 0.002222 * Math.sin(deg2rad(2 * Dm + Fm - MM[3])) * em
                        + 0.002072 * Math.sin(deg2rad(2 * Dm - Fm - MM[3] - M[9])) * em
                        + 0.001877 * Math.sin(deg2rad(Fm - MM[3] + M[9])) * em
                        + 0.001828 * Math.sin(deg2rad(4 * Dm - Fm - M[9]))
                        - 0.001803 * Math.sin(deg2rad(Fm + MM[3])) * em
                        - 0.001750 * Math.sin(deg2rad(3 * Fm))
                        + 0.001570 * Math.sin(deg2rad(M[9] - MM[3] - Fm)) * em
                        - 0.001487 * Math.sin(deg2rad(Fm + Dm))
                        - 0.001481 * Math.sin(deg2rad(Fm + MM[3] + M[9])) * em
                        + 0.001417 * Math.sin(deg2rad(Fm - MM[3] - M[9])) * em
                        + 0.001350 * Math.sin(deg2rad(Fm - MM[3])) * em
                        + 0.001330 * Math.sin(deg2rad(Fm - Dm))
                        + 0.001106 * Math.sin(deg2rad(Fm + 3 * M[9]))
                        + 0.001020 * Math.sin(deg2rad(4 * Dm - Fm))
                        + 0.000833 * Math.sin(deg2rad(Fm + 4 * Dm - M[9]))
                        + 0.000781 * Math.sin(deg2rad(M[9] - 3 * Fm))
                        + 0.000670 * Math.sin(deg2rad(Fm + 4 * Dm - 2 * M[9]))
                        + 0.000606 * Math.sin(deg2rad(2 * Dm - 3 * Fm))
                        + 0.000597 * Math.sin(deg2rad(2 * Dm + 2 * M[9] - Fm))
                        + 0.000492 * Math.sin(deg2rad(2 * Dm + M[9] - MM[3] - Fm)) * em
                        + 0.000450 * Math.sin(deg2rad(2 * M[9] - Fm - 2 * Dm))
                        + 0.000439 * Math.sin(deg2rad(3 * M[9] - Fm))
                        + 0.000423 * Math.sin(deg2rad(Fm + 2 * Dm + 2 * M[9]))
                        + 0.000422 * Math.sin(deg2rad(2 * Dm - Fm - 3 * M[9]))
                        - 0.000367 * Math.sin(deg2rad(MM[3] + Fm + 2 * Dm - M[9])) * em
                        - 0.000353 * Math.sin(deg2rad(MM[3] + Fm + 2 * Dm)) * em
                        + 0.000331 * Math.sin(deg2rad(Fm + 4 * Dm))
                        + 0.000317 * Math.sin(deg2rad(2 * Dm + Fm - MM[3] + M[9])) * em
                        + 0.000306 * Math.sin(deg2rad(2 * Dm - 2 * MM[3] - Fm)) * e2
                        - 0.000283 * Math.sin(deg2rad(M[9] + 3 * Fm))
        );

        double ohm1 = 0.0004664 * Math.cos(deg2rad(OHM[9]));
        double ohm2 = 0.0000754 * Math.cos(deg2rad(OHM[9] + 275.05 - 2.30 * T));

        name.put(Consts.Key.Moon, MOD(L[9], 360) * 3600);
        // name.put(Consts.Key.MoonDecli,  MOD(Bm * (1 - ohm1 - ohm2), 360) * 3600);

        name.put(Consts.Key.Moon_Node, MOD(OHM[9], 360) * 3600);

        //PLUTO
//	 Pp,Sp,Jp,bp;
        double Jp = 238.74 + 3034.9057 * T;
        double Sp = 267.26 + 1222.1138 * T;
        double Pp = 93.48 + 144.9600 * T;

        L[10] = 93.297471 + 144.9600 * T + (
                -19977972 * Math.sin(deg2rad(Pp)) - 738 * Math.sin(deg2rad(Sp + 3 * Pp)) + 394 * Math.sin(deg2rad(Jp + Pp))
                        + 19667536 * Math.cos(deg2rad(Pp)) + 3443 * Math.cos(deg2rad(Sp + 3 * Pp)) - 55 * Math.cos(deg2rad(Jp + Pp))
                        + 987114 * Math.sin(deg2rad(Pp * 2)) + 1234 * Math.sin(deg2rad(2 * Sp - 2 * Pp)) + 119 * Math.sin(deg2rad(Jp + 2 * Pp))
                        - 4939350 * Math.cos(deg2rad(Pp * 2)) + 472 * Math.cos(deg2rad(2 * Sp - 2 * Pp)) - 264 * Math.cos(deg2rad(Jp + 2 * Pp))
                        + 577978 * Math.sin(deg2rad(Pp * 3)) + 1101 * Math.sin(deg2rad(2 * Sp - Pp)) - 46 * Math.sin(deg2rad(Jp + 3 * Pp))
                        + 1226898 * Math.cos(deg2rad(Pp * 3)) - 894 * Math.cos(deg2rad(2 * Sp - Pp)) - 156 * Math.cos(deg2rad(Jp + 3 * Pp))
                        - 334695 * Math.sin(deg2rad(Pp * 4)) + 625 * Math.sin(deg2rad(2 * Sp)) - 77 * Math.sin(deg2rad(Jp + 4 * Pp))
                        - 201966 * Math.cos(deg2rad(Pp * 4)) - 1214 * Math.cos(deg2rad(2 * Sp)) - 33 * Math.cos(deg2rad(Jp + 4 * Pp))
                        + 130519 * Math.sin(deg2rad(Pp * 5)) + 2485 * Math.sin(deg2rad(Jp - Sp)) - 34 * Math.sin(deg2rad(Jp + Sp - 3 * Pp))
                        - 29025 * Math.cos(deg2rad(Pp * 5)) - 486 * Math.cos(deg2rad(Jp - Sp)) - 26 * Math.cos(deg2rad(Jp + Sp - 3 * Pp))
                        - 39851 * Math.sin(deg2rad(Pp * 6)) + 852 * Math.sin(deg2rad(Jp - Sp + Pp)) - 43 * Math.sin(deg2rad(Jp + Sp - 2 * Pp))
                        + 28968 * Math.cos(deg2rad(Pp * 6)) - 1407 * Math.cos(deg2rad(Jp - Sp + Pp)) - 15 * Math.sin(deg2rad(Jp + Sp - Pp))
                        + 20387 * Math.sin(deg2rad(Sp - Pp)) - 948 * Math.sin(deg2rad(Jp - 3 * Pp)) + 21 * Math.cos(deg2rad(Jp + Sp - Pp))
                        - 9832 * Math.cos(deg2rad(Sp - Pp)) + 1073 * Math.cos(deg2rad(Jp - 3 * Pp)) + 10 * Math.sin(deg2rad(2 * Jp - 3 * Pp))
                        - 3986 * Math.sin(deg2rad(Sp)) - 2309 * Math.sin(deg2rad(Jp - 2 * Pp)) + 22 * Math.cos(deg2rad(2 * Jp - 3 * Pp))
                        - 4954 * Math.cos(deg2rad(Sp)) - 1204 * Math.cos(deg2rad(Jp - 2 * Pp)) - 57 * Math.sin(deg2rad(2 * Jp - 2 * Pp))
                        - 5817 * Math.sin(deg2rad(Sp + Pp)) + 7047 * Math.sin(deg2rad(Jp - Pp)) - 32 * Math.cos(deg2rad(2 * Jp - 2 * Pp))
                        - 3365 * Math.cos(deg2rad(Sp + Pp)) + 770 * Math.cos(deg2rad(Jp - Pp)) + 158 * Math.sin(deg2rad(2 * Jp - Pp))
                        - 3903 * Math.sin(deg2rad(Sp + Pp * 2)) + 1184 * Math.sin(deg2rad(Jp)) - 43 * Math.cos(deg2rad(2 * Jp - Pp))
                        + 2895 * Math.cos(deg2rad(Sp + Pp * 2)) - 344 * Math.cos(deg2rad(Jp))
        ) / 1000000;

        double bp = -3.909434 + (
                -5323113 * Math.sin(deg2rad(Pp)) + 312 * Math.sin(deg2rad(Sp)) - 177 * Math.sin(deg2rad(Jp - Sp))
                        - 15024245 * Math.cos(deg2rad(Pp)) - 128 * Math.cos(deg2rad(Sp)) + 259 * Math.cos(deg2rad(Jp - Sp))
                        + 3497557 * Math.sin(deg2rad(Pp * 2)) + 2057 * Math.sin(deg2rad(Sp + Pp)) + 15 * Math.sin(deg2rad(Jp - Sp + Pp))
                        + 1735457 * Math.cos(deg2rad(Pp * 2)) - 904 * Math.cos(deg2rad(Sp + Pp)) + 235 * Math.cos(deg2rad(Jp - Sp + Pp))
                        - 1059559 * Math.sin(deg2rad(Pp * 3)) + 19 * Math.sin(deg2rad(Sp + 2 * Pp)) + 578 * Math.sin(deg2rad(Jp - 3 * Pp))
                        + 299464 * Math.cos(deg2rad(Pp * 3)) - 674 * Math.cos(deg2rad(Sp + 2 * Pp)) - 293 * Math.cos(deg2rad(Jp - 3 * Pp))
                        + 189102 * Math.sin(deg2rad(Pp * 4)) - 307 * Math.sin(deg2rad(Sp + 3 * Pp)) - 294 * Math.sin(deg2rad(Jp - 2 * Pp))
                        - 285383 * Math.cos(deg2rad(Pp * 4)) - 576 * Math.cos(deg2rad(Sp + 3 * Pp)) + 694 * Math.cos(deg2rad(Jp - 2 * Pp))
                        + 14231 * Math.sin(deg2rad(Pp * 5)) - 65 * Math.sin(deg2rad(2 * Sp - 2 * Pp)) + 156 * Math.sin(deg2rad(Jp - Pp))
                        + 101218 * Math.cos(deg2rad(Pp * 5)) + 39 * Math.cos(deg2rad(2 * Sp - 2 * Pp)) + 201 * Math.cos(deg2rad(Jp - Pp))
                        - 29164 * Math.sin(deg2rad(Pp * 6)) - 97 * Math.sin(deg2rad(2 * Sp - Pp)) + 294 * Math.sin(deg2rad(Jp))
                        - 27461 * Math.cos(deg2rad(Pp * 6)) + 208 * Math.cos(deg2rad(2 * Sp - Pp)) + 829 * Math.cos(deg2rad(Jp))
                        + 4935 * Math.sin(deg2rad(Sp - Pp)) - 160 * Math.cos(deg2rad(2 * Sp)) - 123 * Math.sin(deg2rad(Jp + Pp))
                        + 11282 * Math.cos(deg2rad(Sp - Pp)) - 31 * Math.cos(deg2rad(Jp + Pp))
        ) / 1000000;

        r[10] = 40.724725 + (
                +6623876 * Math.sin(deg2rad(Pp)) - 3 * Math.sin(deg2rad(Sp + 2 * Pp)) - 4 * Math.sin(deg2rad(Jp - Pp))
                        + 6955990 * Math.cos(deg2rad(Pp)) + 79 * Math.cos(deg2rad(Sp + 2 * Pp)) + 4564 * Math.cos(deg2rad(Jp - Pp))
                        - 1181808 * Math.sin(deg2rad(2 * Pp)) + 50 * Math.sin(deg2rad(Sp + 3 * Pp)) + 852 * Math.sin(deg2rad(Jp))
                        - 54836 * Math.cos(deg2rad(2 * Pp)) + 54 * Math.cos(deg2rad(Sp + 3 * Pp)) + 855 * Math.cos(deg2rad(Jp))
                        + 163227 * Math.sin(deg2rad(3 * Pp)) - 1 * Math.sin(deg2rad(2 * Sp - 2 * Pp)) - 88 * Math.sin(deg2rad(Jp + Pp))
                        - 139603 * Math.cos(deg2rad(3 * Pp)) - 22 * Math.cos(deg2rad(2 * Sp - 2 * Pp)) - 82 * Math.cos(deg2rad(Jp + Pp))
                        - 3644 * Math.sin(deg2rad(4 * Pp)) + 84 * Math.sin(deg2rad(2 * Sp - Pp)) + 21 * Math.sin(deg2rad(Jp + 2 * Pp))
                        + 48144 * Math.cos(deg2rad(4 * Pp)) - 48 * Math.cos(deg2rad(2 * Sp - Pp)) - 12 * Math.cos(deg2rad(Jp + 2 * Pp))
                        - 6268 * Math.sin(deg2rad(5 * Pp)) - 30 * Math.sin(deg2rad(2 * Sp)) - 14 * Math.sin(deg2rad(Jp + 3 * Pp))
                        - 8851 * Math.cos(deg2rad(5 * Pp)) + 61 * Math.cos(deg2rad(2 * Sp)) + 6 * Math.cos(deg2rad(Jp + 3 * Pp))
                        + 3111 * Math.sin(deg2rad(6 * Pp)) + 26 * Math.sin(deg2rad(Jp - Sp)) - 6 * Math.sin(deg2rad(2 * Jp - 3 * Pp))
                        - 408 * Math.cos(deg2rad(6 * Pp)) - 39 * Math.cos(deg2rad(Jp - Sp)) + 1 * Math.cos(deg2rad(2 * Jp - 3 * Pp))
                        - 621 * Math.sin(deg2rad(Sp - Pp)) - 19 * Math.sin(deg2rad(Jp - Sp + Pp)) + 13 * Math.sin(deg2rad(2 * Jp - 2 * Pp))
                        + 2223 * Math.cos(deg2rad(Sp - Pp)) - 40 * Math.cos(deg2rad(Jp - Sp + Pp)) - 23 * Math.cos(deg2rad(2 * Jp - 2 * Pp))
                        + 438 * Math.sin(deg2rad(Sp)) - 321 * Math.sin(deg2rad(Jp - 3 * Pp)) + 25 * Math.sin(deg2rad(2 * Jp - Pp))
                        + 450 * Math.cos(deg2rad(Sp)) + 42 * Math.cos(deg2rad(Jp - 3 * Pp)) + 107 * Math.cos(deg2rad(2 * Jp - Pp))
                        - 153 * Math.sin(deg2rad(Sp + Pp)) + 797 * Math.sin(deg2rad(Jp - 2 * Pp)) + 25 * Math.sin(deg2rad(2 * Jp))
                        + 61 * Math.cos(deg2rad(Sp + Pp)) - 792 * Math.cos(deg2rad(Jp - 2 * Pp)) + 16 * Math.cos(deg2rad(2 * Jp))
        ) * 0.000001;

        L[10] = MOD(L[3] + ATANP(r[10] * Math.cos(deg2rad(bp)) * Math.sin(deg2rad(L[10] - L[3])), r[10] * Math.cos(deg2rad(bp)) * Math.cos(deg2rad(L[10] - L[3])) + r[3]), 360);
        name.put(Consts.Key.Pluto, MOD(L[10], 360) * 3600);

        double sdtm = MOD((T + 0.5) * 36525, 1) * 360;
        sdtm += MOD(TYL(6.6460656, 2400.051262, 0.00002581, 0, T), 24) * 360 / 24;
        sdtm += name.get(Consts.Key.longitude) / 3600.0;
//	echo name.get("Longitude")/3600 ."===============".name.get("Longitude")."<br>";
        name.put(Consts.Key.SideraelTime,  sdtm * 240);
//	Bhava(name,T,((double )LATITUDE)/3600,sdtm);
    }
}