package com.quasiris.qsf.commons.text.normalizer;

/**
 * App Text Normalizer config
 */
public class NormalizerConfig {
    /**
     * Profile id
     */
    private String id;

    /**
     * Filepath of stopword file
     */
    private String stopwordFilepath;

    /**
     * Filepath of synonyms file
     */
    private String synonymsFilepath;

    /**
     * Stemming enabled if true
     */
    private boolean stem;

    /**
     * Normalize german umlauts like ä to ae, ö to oe
     */
    private boolean normalizeUmlaut;

    /**
     * Remove duplicate tokens
     */
    private boolean removeDuplicates;

    /**
     * Keep ,.!?: if true
     */
    private boolean keepPunctuation;

    /**
     * Whether to remove numbers like 8, 8.0 or 8,0
     */
    private boolean removeNumbers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStopwordFilepath() {
        return stopwordFilepath;
    }

    public void setStopwordFilepath(String stopwordFilepath) {
        this.stopwordFilepath = stopwordFilepath;
    }

    public String getSynonymsFilepath() {
        return synonymsFilepath;
    }

    public void setSynonymsFilepath(String synonymsFilepath) {
        this.synonymsFilepath = synonymsFilepath;
    }

    public boolean isStem() {
        return stem;
    }

    public void setStem(boolean stem) {
        this.stem = stem;
    }

    public boolean isNormalizeUmlaut() {
        return normalizeUmlaut;
    }

    public void setNormalizeUmlaut(boolean normalizeUmlaut) {
        this.normalizeUmlaut = normalizeUmlaut;
    }

    public boolean isRemoveDuplicates() {
        return removeDuplicates;
    }

    public void setRemoveDuplicates(boolean removeDuplicates) {
        this.removeDuplicates = removeDuplicates;
    }

    public boolean isKeepPunctuation() {
        return keepPunctuation;
    }

    public void setKeepPunctuation(boolean keepPunctuation) {
        this.keepPunctuation = keepPunctuation;
    }

    public boolean isRemoveNumbers() {
        return removeNumbers;
    }

    public void setRemoveNumbers(boolean removeNumbers) {
        this.removeNumbers = removeNumbers;
    }
}
