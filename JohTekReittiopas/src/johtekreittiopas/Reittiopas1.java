package johtekreittiopas;

import java.util.*;

/**
 * Reittioppaan pÃ¤Ã¤luokka.
 */
public class Reittiopas1 {
    
    private static Tila tila;
    private static HashMap<String, Tila> prev;

    /**
     * Toteuta leveyssuuntainen haku. Pida muistissa omassa hakutilaoliossasi
     * mikÃ¤ oli edeltava tila, josta ko. tilaan pÃ¤Ã¤stiin.
     *
     * Maaliin pÃ¤Ã¤styÃ¤si tulosta lÃ¤pikÃ¤ytyjen pysÃ¤kkien koodit (ja nimet)
     * Voit halutessasi myos visualisoida kuljettua reittiÃ¤ rLine-metodin
     * avulla.
     *
     * @param bfs pysÃ¤kkiverkko
     * @param lahto Lahtopysakin koodi
     * @param maali Maalipysakin koodi
     */
    public static void haku(Pysakkiverkko bfs, String lahto, String maali) {
        List<Pysakki> solmulista = new ArrayList();
        List<Pysakki> kasitellyt = new ArrayList();
        prev = new HashMap();

        solmulista.add(bfs.getPysakki(lahto));
        
        tila = new Tila(lahto, null);
        prev.put(lahto, tila);
        
        while (!solmulista.isEmpty()) {
            Pysakki pysakki = solmulista.remove(0);

            if (!kasitellyt.contains(pysakki)) {
                kasitellyt.add(pysakki);
                if (pysakki == bfs.getPysakki(maali)) {
                    tila = new Tila(pysakki.getKoodi(), prev.get(pysakki.getKoodi()));
                    tulostaReitti(tila, bfs);
                }
                lisaa(pysakki.getNaapurit(), solmulista, kasitellyt, bfs, pysakki);
            }
        }
    }

    private static void lisaa(String[] naapurit, List solmulista, List kasitellyt, Pysakkiverkko bfs, Pysakki pysakki) {
        for (int i = 0; i < naapurit.length; i++) {
            Pysakki naapuri = bfs.getPysakki(naapurit[i]);
            if (!solmulista.contains(naapuri) && !kasitellyt.contains(naapuri)) {
                solmulista.add(naapuri);
                tila = new Tila(pysakki.getKoodi(), prev.get(pysakki.getKoodi()));
                prev.put(naapuri.getKoodi(), tila);
            }
        }

    }

    private static void tulostaReitti(Tila tila, Pysakkiverkko bfs) {
        ArrayList tulostus = new ArrayList();
        
        while(tila.getEdellinen() != null){
            Pysakki pysakki = bfs.getPysakki(tila.getKoodi());
            tulostus.add("Koodi: " + pysakki.getKoodi() + ", Nimi: " + pysakki.getNimi());
          //  System.out.println("Koodi: " + pysakki.getKoodi() + ", Nimi: " + pysakki.getNimi());
            tila = tila.getEdellinen();
        }
        System.out.println("");
        for(int i = tulostus.size()-1; i >= 0; i--){
            System.out.println(tulostus.get(i));
        }
        
    }

    /**
     * Tulostaa annetun reitin R plot komennot. Kutsu tata metodia, ja
     * copypastea tulostuvat kolmerivia R:n komentoriville sen jalkeen kun olet
     * kirjoittanut source("/PATH/TO/rplot.txt") - komennon ja R on piirtany
     * raitiovaunuverkon uuteen ikkunaan. Valitsemasi reitin tulisi nakya verkon
     * paalla oranssina.
     *
     * Vapaavalintainen visualisointityokalu jos haluaa kayttaa.
     *
     * @param x reitin (pysakkien) x-koordinaatit
     * @param y reitin (pysakkien) y-koordinaatit
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

    /**
     * Tulostaa kahden pysÃ¤kin vÃ¤lisen reitin, jolla on vÃ¤hiten
     * vÃ¤lipysÃ¤kkejÃ¤
     *
     * @param args komentoriviparametrit
     */
    public static void main(String[] args) {

        Pysakkiverkko bfs = new Pysakkiverkko("./verkko.json");
        System.out.println("");
        haku(bfs, "1250429", "1121480");
        System.out.println(bfs.getPysakki("1121480").getOsoite()); // Testi
        
        haku(bfs, "1070423", "1070417");

    }
}
