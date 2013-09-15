/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package astarreittiopas;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 *
 * @author Pete
 */
public class Reittiopas2 {

    /**
     * Toteuta reittiopas A*-algoritmin avulla.
     *
     * Viime viikon leveyssuuntaiseen hakuun lisÃ¤tÃ¤Ã¤n nyt raitiovaunulinjat,
     * joiden avulla kuljetaan pysÃ¤kiltÃ¤ toiselle. PysÃ¤kki-oliot tietÃ¤vÃ¤t
     * viereiset pysÃ¤kin ja kaikki niille matkustavat linjat. Linja-oliot
     * tietÃ¤vÃ¤t kunkin pysÃ¤kin vÃ¤lisen ajan (voi vaihdella
     * linjakohtaisesti). Mielikuvitusmaailmassamme kaikki raitiovaunulinjat
     * lÃ¤htevÃ¤t 10 minuutin vÃ¤lein linjan ensimmÃ¤iseltÃ¤ pysÃ¤kiltÃ¤ ja
     * matkustaminen lÃ¤htÃƒÂ¶pysÃ¤kiltÃ¤ aloitetaan 0-9 minuutin kohdalla.
     *
     * Leveyssuuntaisesta hausta poiketen tarvitset nyt pitÃ¤Ã¤ muistissa omassa
     * hakutila-oliossasi ainakin: - hakutila, josta tÃ¤hÃ¤n tilaan on tultu -
     * aika, joka on jo kulunut lÃ¤hdÃ¤stÃ¤ - kellon aika joka tÃ¤llÃ¤ hetkellÃ¤
     * on (kulunut aika + lÃ¤htÃ¤aika) - heuristinen arvio kauan tÃ¤stÃ¤
     * hakutilasta kuluu aikaa maaliin (voit olettaa maksiminopeuden olevan 526
     * koordinaattipistettÃ¤ minuutissa)
     *
     * LisÃ¤ksi reitin tulostusta varten on hyÃ¶dyllistÃ¤ muistaa: - millÃ¤
     * linjalla tÃ¤hÃ¤n tilaan tultiin - kauan odotettiin viime pysÃ¤killÃ¤ -
     * kauan matkustettiin viime pysÃ¤kiltÃ¤
     *
     * JÃ¤rjestÃ¤ kÃ¤ytÃ¤vÃ¤t tilat kuluneen ajan + heuristisen arvion
     * perusteella nopein ensimmÃ¤iseksi.
     *
     * Maaliin pÃ¤Ã¤styÃ¤si tulosta reitin jokaisen pysÃ¤kin koodi, nimi ja
     * aika, jolloin sillÃ¤ pysÃ¤killÃ¤ ollaan, sekÃ¤ millÃ¤ linjalla pysÃ¤kkien
     * vÃ¤li matkustetaan (odotusaika ei ole vÃ¤lttÃ¤mÃ¤tÃ¤n). Leveyssuuntaisen
     * haun tapaan voit halutessasi visualisoida kuljettua reittiÃ¤
     * rLine-metodin avulla.
     *
     * Vinkki: kÃ¤ytÃ¤ hakutila-listauksessa PriorityQueue:ta ja toteuta omalle
     * hakutila-oliollesi Comparable<hakutila>-rajapinta, eli kirjoita sille
     * metodi compareTo, joka palauttaa arvot vastaavasti kuin esim.
     * String-luokan compareTo. Jos kÃ¤ytÃ¤t valmiina annettua luokkaa Tila2, on
     * se valmiiksi toteutettuna.
     *
     * http://docs.oracle.com/javase/6/docs/api/java/util/PriorityQueue.html
     * http://docs.oracle.com/javase/6/docs/api/java/lang/Comparable.html
     *
     * @param verkko pysÃ¤kkiverkko
     * @param lahto lÃ¤htÃ¶pysÃ¤kin koodi
     * @param maali maalipysÃ¤kin koodi
     * @param aika LÃ¤htÃ¶aika (voit olettaa sen olevan 0-9 minuuttia)
     */
    public static void haku(Pysakkiverkko2 verkko, String lahto, String maali, int aika) {

        PriorityQueue<Tila2> jono = new PriorityQueue<Tila2>();
        ArrayList<Pysakki> kaydyt = new ArrayList<Pysakki>();

        jono.add(new Tila2(lahto, null, null, (10-aika), 0, aika, laskeHeuristiikka(verkko.getPysakki(lahto), verkko.getPysakki(maali))));
        while (!jono.isEmpty()) {
            Tila2 t = jono.poll();
            if (!kaydyt.contains(verkko.getPysakki(t.getPysakki()))) {
                kaydyt.add(verkko.getPysakki(t.getPysakki()));
                if (t.getPysakki().equals(maali)) {
                    tulostaPolku(verkko, t);
                    return;
                }
                Pysakki p = verkko.getPysakki(t.getPysakki());
                lisaa(verkko, p, jono, t, maali);
            }
        }
        System.out.println("Ei ratkaisua :<");
    }

    public static void tulostaPolku(Pysakkiverkko2 bfs, Tila2 t) {
        Stack<Tila2> pino = new Stack<Tila2>();

        while (t != null) {
            pino.push(t);
            t = t.getEdellinen();
        }

        int[] x = new int[pino.size()];
        int[] y = new int[pino.size()];
        int i = 0;

        while (!pino.isEmpty()) {
            Pysakki p = bfs.getPysakki(pino.pop().getPysakki());
            System.out.println(i + ": " + p.getKoodi() + " " + p.getNimi());
            x[i] = p.getXKoordinaatti();
            y[i] = p.getYKoordinaatti();
            i++;
        }
       // rLine(x, y);
    }

    public static void lisaa(Pysakkiverkko2 verkko, Pysakki pysakki, PriorityQueue jono, Tila2 edellinen, String maali) {
        String[] naapurit = pysakki.getNaapurit();
        for (int i = 0; i < naapurit.length; i++) {
            Object[] nopein = nopein(verkko, pysakki, naapurit[i], edellinen.getNykyinenAika());
            jono.add(new Tila2(naapurit[i], edellinen, (String) nopein[0], (int) nopein[1], (int) nopein[2], (edellinen.getNykyinenAika() + (int) nopein[1] + (int) nopein[2]), laskeHeuristiikka(verkko.getPysakki(naapurit[i]), verkko.getPysakki(maali))));
        }
    }

    private static double laskeHeuristiikka(Pysakki pysakki, Pysakki maali) {
        double a = Math.abs(pysakki.getXKoordinaatti() - maali.getXKoordinaatti());
        double b = Math.abs(pysakki.getYKoordinaatti() - maali.getYKoordinaatti());
        
        double etaisyys = Math.sqrt((a*a)+ (b*b));
        return etaisyys/526;
        
    }

    /**
     * Laskee nopeimman linjan ja siltÃ¤ kuluvan ajan pysÃ¤kiltÃ¤
     * naapuripysÃ¤kille.
     *
     * @param verkko tutkittava pysÃ¤kkiverkko
     * @param p pysÃ¤kki jolla ollaan nyt
     * @param n naapuripysÃ¤kin koodi
     * @param aika aika juuri nyt
     * @return Object[3], jossa [0] String = Linja.koodi, [1] int = odotusaika,
     * [2] int = matka-aika
     */
    public static Object[] nopein(Pysakkiverkko2 verkko, Pysakki p, String n, int aika) {
        Object[] ret = {"", 100, 100}; // linja.koodi, odotusaika, matka-aika

        for (String s : p.getNaapuriinKulkevatLinjat(n)) {
            Linja l = verkko.getLinja(s);
            for (int i = 0; i < l.getPysakkikoodit().length; i++) {
                if (l.getPysakkikoodit()[i].equals(p.getKoodi())) {
                    // Lasketaan odotusaika modulo 10
                    int odotus = (l.getPysahtymisajat()[i] % 10) - (aika % 10);
                    odotus = odotus < 0 ? odotus + 10 : odotus;
                    int matka = l.getPysahtymisajat()[i + 1] - l.getPysahtymisajat()[i];
                    // Jos odotus + matka-aika on pienempi kuin aikaisempi,
                    // vaihdetaan nopeinta matkustuslinjaa
                    if ((Integer) ret[1] + (Integer) ret[2] > (odotus + matka)) {
                        ret[0] = l.getKoodi();
                        ret[1] = odotus;
                        ret[2] = matka;
                    }
                }
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        Pysakkiverkko2 verkko = new Pysakkiverkko2("./verkko2.json", "./linjat.json");
        System.out.println("");
        haku(verkko, "1150435", "1130446", 2);
    }

    /**
     * Tulostaa annetun reitin R:n piirtokomennot. Kutsu tÃ¤tÃ¤ metodia, ja
     * copypastea tulostuvat kolmeriviÃ¤ R:n komentoriville sen jÃ¤lkeen, kun
     * olet kirjoittanut source("/PATH/TO/rplot.txt") - komennon ja R on
     * piirtÃ¤nyt raitiovaunuverkon uuteen ikkunaan. Valitsemasi reitin tulisi
     * nÃ¤kyÃ¤ verkon pÃ¤Ã¤llÃ¤ oranssina.
     *
     * Vapaavalintainen visualisointi tyÃ¶kalu jos haluaa kÃ¤yttÃ¤Ã¤.
     *
     * @param x reitin (pysÃ¤kkien) x-koordinaatit
     * @param y reitin (pysÃ¤kkien) y-koordinaatit
     */
    public static void rLine(int[] x, int[] y) {
        String rx = "x <- c(";
        String ry = "y <- c(";

        for (int i = 0; i < x.length; i++) {
            rx = rx + x[i] + ", ";
            ry = ry + y[i] + ", ";
        }
        rx = rx.substring(0, rx.length() - 2) + ")";
        ry = ry.substring(0, ry.length() - 2) + ")";
        System.out.println(rx);
        System.out.println(ry);
        System.out.println("lines(x,y, lwd = 2, col = \"orange\")");
    }
}