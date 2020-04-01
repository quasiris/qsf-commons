package com.quasiris.qsf.commons.text.normalizer;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TextNormalizerServiceTest {
    Map<String, TextNormalizerService> textNormalizers;

    @Before
    public void setUp() throws Exception {
        textNormalizers = new HashMap<>();

        NormalizerConfig normalizerConfigSearch = new NormalizerConfig();
        normalizerConfigSearch.setId("vector_search");
        normalizerConfigSearch.setStopwordFilepath("german-stopwords.txt");
        normalizerConfigSearch.setSynonymsFilepath("synonyms.txt");
        normalizerConfigSearch.setStem(true);
        normalizerConfigSearch.setNormalizeUmlaut(false);
        normalizerConfigSearch.setRemoveDuplicates(true);
        normalizerConfigSearch.setKeepPunctuation(false);
        textNormalizers.put(normalizerConfigSearch.getId(), new TextNormalizerService(normalizerConfigSearch));

        NormalizerConfig normalizerConfigBert = new NormalizerConfig();
        normalizerConfigBert.setId("bert_universal");
        normalizerConfigBert.setStem(false);
        normalizerConfigBert.setNormalizeUmlaut(true);
        normalizerConfigBert.setRemoveDuplicates(false);
        normalizerConfigBert.setKeepPunctuation(true);
        textNormalizers.put(normalizerConfigBert.getId(), new TextNormalizerService(normalizerConfigBert));
    }

    @Test
    public void normalize() {
        assertEquals("hallo world world whats-up open", textNormalizers.get("vector_search").normalize("  Hällo,      der World- world.: - -whats-up?   <a href>open</a>"));
        assertEquals("haello, der world world.: whats-up? open", textNormalizers.get("bert_universal").normalize("  Hällo,      der World- world.: - -whats-up?   <a href>open</a>"));
    }

    @Test
    public void trimUrl() {
        assertEquals("pleas visit", textNormalizers.get("vector_search").normalize("Please visit https://quasiris.de/new-product/pricing-offer"));
    }

    @Test
    public void fixFritzBox() {
        assertEquals("fritz-box", textNormalizers.get("vector_search").normalize("Fritz!Box"));
        assertEquals("fritz-box", textNormalizers.get("vector_search").normalize("Fritz.Box"));
    }

    @Test
    public void others() {
        assertEquals("13-", textNormalizers.get("vector_search").normalize("13-er"));
        assertEquals("xtra-und-los", textNormalizers.get("vector_search").normalize("xtra los"));
        assertEquals("bluetooth-musik-und-anrufsteuerung", textNormalizers.get("vector_search").normalize("bluetooth musik und anrufsteuerung"));
        assertEquals("nicht-pc-gerat", textNormalizers.get("vector_search").normalize("Nicht-PC-Geräte"));
        assertEquals("nicht-stor", textNormalizers.get("vector_search").normalize("nicht-Stören"));
    }

    @Test
    public void removeEscapedNewlines() {
        assertEquals("hello world whats-up open", textNormalizers.get("vector_search").normalize("  Hello   \\n  \\r\\n der World- - -whats-up?   <a href>open</a>"));
    }

    @Test
    public void removeHtml() {
        // given
        String html = "<p>Apple macht mit dem iPhone X offenbar richtig Kasse. Analysten zufolge entfallen mehr als ein Drittel aller Gewinne auf dem Handymarkt auf das Top-Smartphone aus Cupertino.<!--more--></p><p>Ersten Untersuchungen aus dem vergangenen Herbst zufolge <a href=\"\"https://t3n.de/news/iphone-x-kosten-herstellung-857893/\"\">kostet Apple das iPhone X in der Herstellung</a> rund 430 US-Dollar – nicht eingerechnet Marketing- oder Entwicklungskosten. Verkauft wird das Top-Smartphone für über 1.000 Dollar. Kein Wunder, dass <a href=\"\"https://t3n.de/news/iphone-apple-rekord-quartal-932572/\"\">Apple mit dem iPhone X</a> im Schlussquartal 2017 einen Anteil von satten 35 Prozent der weltweiten Smartphone-Gewinne eingestrichen hat, wie die Analysefirma <a href=\"\"https://www.counterpointresearch.com/iphone-x-alone-generated-35-total-handset-industry-profits-q4-2017/\"\">Counterpoint Research ausgerechnet</a> hat.</p><h2>Gewinne mit Smartphones: 8 iPhones in den Top-10, iPhone X klar voran</h2><p>Dass Apples Gewinnmarge im Vergleich mit der Konkurrenz überdurchschnittlich hoch ist, zeigt unter anderem, dass das iPhone X „nur“ 21 Prozent Anteil am Umsatz der Branche hat. Insgesamt finden sich unter den zehn Smartphones mit dem höchsten Anteil am Gesamtgewinn auf dem Smartphone-Sektor acht iPhones – neben den iPhone-8- und -7-Modellen auch das iPhone 6 und das iPhone SE. Selbst mit älteren Geräten macht Apple also noch gute Gewinne. Lediglich Samsungs Galaxy Note 8 und Galaxy S8 Plus können die iPhone-Phalanx in der Top-10-Liste etwas auflockern.</p><figure class=\"\"caption alignnone\"\" id=\"\"attachment_1061843\"\"><img alt=\"\"\"\" class=\"\"size-large wp-image-1061843\"\" height=\"\"338\"\" src=\"\"https://assets.t3n.sc/news/wp-content/uploads/2018/04/iphone-x-gewinne-smartphone-markt-620x338.jpg?auto=compress\"\" width=\"\"620\"\" /><figcaption class=\"\"tg-noadgoal\"\">Das iPhone X macht mehr als ein Drittel aller Gewinne im Smartphone-Bereich. (Screenshot: Counterpoint Research/t3n.de)</figcaption></figure><p>Insgesamt verlor der Handymarkt im vierten Quartal 2017 rund ein Prozent, was die Gewinne angeht. Apple legte dagegen um ein Prozent zu. Der iPhone-Konzern macht mit seinen Geräten 86 Prozent aller Gewinne auf dem Smartphone-Sektor. Die chinesischen Hersteller wie Huawei, Oppo oder Vivo sehen die Analysten aber ebenfalls im Aufwind. Alle chinesischen Smartphone-Konzerne zusammen haben zwischen Oktober und Dezember rund 1,3 Milliarden Dollar Gewinn gemacht.</p><div class=\"\"tg-article-gallery c-gallery__preview  c-box\"\">    <strong class=\"\"u-text-medium\"\">iPhone X: Das ist Apples Neuinterpretation des iPhones</strong>            <figure>            <div class=\"\"c-gallery__preview-image\"\">                <img alt=\"\"iPhone X. (Bild: Apple)\"\" src=\"\"https://assets.t3n.sc/news/wp-content/uploads/2017/09/iphone-x-2.jpg?auto=compress\"\" />                <a class=\"\"c-gallery__icon tg-gallerylink\"\" href=\"\"https://t3n.de/news/apple-iphone-x-856291/iphone-x_2/?parent=1061838\"\">                    <span class=\"\"c-button c-gallery__link -highlight\"\">                        Bilder                        <svg class=\"\"u-gap-left-small\"\" fill=\"\"#FFFFFF\"\" height=\"\"10px\"\" width=\"\"10px\"\" xmlns=\"\"http://www.w3.org/2000/svg\"\">                            <use xlink:href=\"\"#s-arrow-right\"\" xmlns:xlink=\"\"http://www.w3.org/1999/xlink\"\"></use>                        </svg>                    </span>                </a>            </div>            <div class=\"\"o-grid\"\">                <figcaption class=\"\"o-grid__item u-gap-small u-nopadded-outer\"\">iPhone X. (Bild: Apple)</figcaption>                <p class=\"\"u-color-mute u-pull-right u-text-extrasmall u-gap-left u-gap-top-small\"\">                    1 von 16                </p>            </div>        </figure>    </div><p>Allein Huawei konnte laut Counterpoint Research seinen Gewinn gegenüber dem vergleichbaren Vorjahreszeitraum um 59 Prozent steigern. Mit steigenden Verkaufspreisen und hochwertigeren Geräten sollen die chinesischen Hersteller ihren Anteil am Gesamtgewinn der Branche in den kommenden Quartalen weiter steigern, so die Prognose.</p><p>Noch eine beeindruckende Zahl zum Abschluss: Das iPhone X hat Apple im vierten Quartal 2017 – obwohl nur zwei Monate davon im Verkauf – fünf Mal soviel Gewinn eingebracht, wie über 600 Android-Smartphone-Hersteller zusammen generieren konnten, schreiben die Analysten von Counterpoint Research.</p><p><strong>Ebenfalls interessant: <a href=\"\"https://t3n.de/news/samsung-iphone-x-galaxy-s8-863362/\"\">Analysten – Samsung verdient am iPhone X mehr als am Galaxy S8</a> und <a href=\"\"https://t3n.de/news/apple-iphone-x-umsatz-king-952034/\"\">Dank iPhone X – Apple macht mehr Umsatz als alle anderen Hersteller zusammen</a></strong></p>";

        // when
        String text = textNormalizers.get("vector_search").normalize(html);

        // then
        assertEquals("appl macht i-phon x offenbar richtig kass analyst zufolg entfall drittel gewinn handymarkt top-smartphon cupertino untersuchung vergang herb zufolg kostet appl i-phon x herstellung rund 430 us-dollar nicht eingerechnet marketing entwicklungskost verkauft top-smartphon 1-000 dollar wund appl i-phon x schlussquartal 2017 anteil satt 35 prozent weltweit smartphone-gewinn eingestrich analysefirma counterpoint research ausgerechnet gewinn smart-phon 8 i-phon top-10 i-phon x klar voran appl gewinnmarg vergleich konkurrenz uberdurchschnittlich hoch zeigt i-phon x 21 prozent anteil umsatz branch insgesamt find smart-phon hoch anteil gesamtgewinn smartphone-sektor i-phon iphone-8 7-modell i-phon 6 i-phon se alt-gerat macht appl gut gewinn lediglich samsung galaxy not 8 galaxy s8 plus iphone-phalanx top-10-list auflock i-phon x macht drittel gewinn smartphone-bereich screenshot counterpoint research t3n-d insgesamt verlor handymarkt viert quartal 2017 rund prozent gewinn angeht appl legt dageg prozent iphone-konz macht gerat 86 prozent gewinn smartphone-sektor chinesisch herstell huawei oppo vivo analyst aufwind chinesisch smartphone-konzern zusamm oktober-dezemb rund 1 3 milliard dollar gewinn gemacht i-phon x appl neuinterpretation i-phon bild i-phon x bild appl 1 16 allein huawei laut counterpoint research gewinn gegenub vergleichbar vorjahreszeitraum 59 prozent steig steigend verkaufspreis hochwertig gerat soll chinesisch herstell anteil gesamtgewinn branch kommend quartal steig prognos beeindruckend zahl abschluss i-phon x appl viert quartal 2017 obwohl monat davon verkauf mal soviel gewinn eingebracht 600 android-smartphone-herstell zusamm generi konnt schreib analyst counterpoint research interessant analyst samsung verdient i-phon x galaxy s8 i-phon x appl macht umsatz herstell zusamm", text);
    }

    @Test
    public void stem() {
        assertEquals("neust link grun-rot e-fahrzeug", textNormalizers.get("vector_search").normalize("die neusten <a href=\"http://link.com\">(Link)</a>, grün-roten <b>E-Fahrzeuge</b> sind hier!"));
    }

    @Test
    public void normalizeWhitespace() {
        assertEquals("hello world what up", textNormalizers.get("vector_search").normalize("  Hello      World whats    up?   "));
    }

    @Test
    public void compound() {
        assertEquals("festnetz-rufnumm", textNormalizers.get("vector_search").normalize("Festnetz-Rufnummer"));
        assertEquals("festnetz-rufnumm", textNormalizers.get("vector_search").normalize("Festnetz Rufnummer"));
        assertEquals("festnetz-rufnumm", textNormalizers.get("vector_search").normalize("Festnetz      Rufnummer"));
        assertEquals("festnetz-rufnumm", textNormalizers.get("vector_search").normalize("Festnetzrufnummer"));

        assertEquals("festnetz-rufnumm", textNormalizers.get("vector_search").normalize("Festnetz-Rufnummern"));
        assertEquals("festnetz-rufnumm", textNormalizers.get("vector_search").normalize("Festnetz Rufnummern"));
        assertEquals("festnetz-rufnumm", textNormalizers.get("vector_search").normalize("Festnetzrufnummern"));

        assertEquals("festnetz-rufnumm", textNormalizers.get("vector_search").normalize("festnetz-rufnummer"));
        assertEquals("festnetz-rufnumm", textNormalizers.get("vector_search").normalize("festnetz rufnummer"));
        assertEquals("festnetz-rufnumm", textNormalizers.get("vector_search").normalize("festnetzrufnummer"));

        assertEquals("festnetz-rufnumm", textNormalizers.get("vector_search").normalize("festnetz-rufnummern"));
        assertEquals("festnetz-rufnumm", textNormalizers.get("vector_search").normalize("festnetz rufnummern"));
        assertEquals("festnetz-rufnumm", textNormalizers.get("vector_search").normalize("festnetzrufnummern"));

        assertEquals("fritz-box", textNormalizers.get("vector_search").normalize("Fritz-Box"));
        assertEquals("fritz-box", textNormalizers.get("vector_search").normalize("Fritz Box"));
        assertEquals("fritz-box", textNormalizers.get("vector_search").normalize("Fritzbox"));
        assertEquals("fritz-box", textNormalizers.get("vector_search").normalize("Fritz.Box"));
        assertEquals("fritz-box", textNormalizers.get("vector_search").normalize("Fritz.   Box"));
        assertEquals("fritz-box", textNormalizers.get("vector_search").normalize("Fritz!Box"));
        assertEquals("fritz-box", textNormalizers.get("vector_search").normalize("Fritz ! Box"));

        assertEquals("endgerate-probl", textNormalizers.get("vector_search").normalize("Endgeräte-Problem"));
        assertEquals("endgerate-problem", textNormalizers.get("vector_search").normalize("Endgeräte-Probleme"));

        assertEquals("octa-cor", textNormalizers.get("vector_search").normalize("Octa-Core"));
        assertEquals("octa-core-prozessor", textNormalizers.get("vector_search").normalize("Octa-Core-Prozessor"));
        assertEquals("octa-core-prozessor", textNormalizers.get("vector_search").normalize("Octa-Core Prozessor"));
        assertEquals("octa-core-prozessor", textNormalizers.get("vector_search").normalize("Octa Core-Prozessor"));
        assertEquals("octa-core-prozessor", textNormalizers.get("vector_search").normalize("Octa Core Prozessor"));

        assertEquals("octa-core-mobile-prozessor", textNormalizers.get("vector_search").normalize("Octa-Core-Mobile Prozessor"));
        assertEquals("octa-core-mobile-prozessor", textNormalizers.get("vector_search").normalize("Octa-Core Mobile-Prozessor"));
        assertEquals("octa-core-mobile-prozessor", textNormalizers.get("vector_search").normalize("Octa Core Mobile-Prozessor"));
        assertEquals("octa-core-mobile-prozessor", textNormalizers.get("vector_search").normalize("Octa Core-Mobile Prozessor"));
        assertEquals("octa-core-mobile-prozessor", textNormalizers.get("vector_search").normalize("Octa Core Mobile Prozessor"));
        assertEquals("octa-core-mobile-prozessor", textNormalizers.get("vector_search").normalize("OctaCoreMobileProzessor"));
        assertEquals("octa-core-mobile-prozessor", textNormalizers.get("vector_search").normalize("Octa Core-Mobile-Prozessor"));
        assertEquals("octa-core-mobile-prozessor", textNormalizers.get("vector_search").normalize("Octa-Core-Mobile-Prozessor"));
        assertEquals("octa-core-mobile-prozessor", textNormalizers.get("vector_search").normalize("Octa-Core Mobile Prozessor"));
        assertEquals("octa-core-mobile-prozessor", textNormalizers.get("vector_search").normalize("Octa-Core          Mobile Prozessor"));
        assertEquals("octa-core-mobile-prozessor", textNormalizers.get("vector_search").normalize("Octa-Core     ,     Mobile Prozessor"));

        assertEquals("mobile-prozessor", textNormalizers.get("vector_search").normalize("Mobile Prozessor"));
        assertEquals("mobile-prozessor", textNormalizers.get("vector_search").normalize("Mobile-Prozessor"));
        assertEquals("mobile-prozessor", textNormalizers.get("vector_search").normalize("MobileProzessor"));
    }
}