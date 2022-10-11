package com.quasiris.qsf.commons.nlp;

import com.quasiris.qsf.commons.text.TextSplitter;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SentenceSplitter implements TextSplitter {
    private Locale locale = Locale.GERMAN;
    private Integer minLength = 100;

    public SentenceSplitter() {
    }

    public SentenceSplitter(Locale locale, Integer minLength) {
        this.locale = locale;
        this.minLength = minLength;
    }

    @Override
    public List<String> split(String text) {
        List<String> result = new ArrayList<>();
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.GERMAN);
        iterator.setText(text);
        int start = iterator.first();
        int endTmp = 0;
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             end = iterator.next()) {
            endTmp = end;
            if((end - start) >= minLength) {
                result.add(text.substring(start, end).trim());
                start = end;
            }
        }
        // add last
        if((endTmp - start) < minLength) {
            result.add(text.substring(start, endTmp).trim());
        }
        return result;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }
}
