package com.quasiris.qsf.commons.text.query;

import com.quasiris.qsf.commons.text.normalizer.TextNormalizer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

public class QueryHasher implements TextNormalizer {

    private int maxLength;
    private Set<String> stopwords;

    // Constructor to configure the max length and stopwords
    public QueryHasher(int maxLength, Set<String> stopwords) {
        this.maxLength = maxLength;
        this.stopwords = stopwords != null ? stopwords : Collections.emptySet();
    }

    /**
     * Removes special characters, keeping only letters, digits, and spaces.
     */
    public String removeSpecialCharacters(String input) {
        return input.replaceAll("[^a-zA-Z0-9\\s]", "");
    }

    /**
     * Converts a token to lowerca0106873se.
     */
    public String toLowerCase(String token) {
        return token.toLowerCase();
    }

    /**
     * Reduces a token to a maximum of `maxLength` characters.
     */
    public String reduceToMaxLength(String token) {
        return token.length() > maxLength ? token.substring(0, maxLength) : token;
    }

    /**
     * Removes stopwords from the tokens.
     */
    public boolean removeStopwords(String token) {
        return !stopwords.contains(token);
    }

    /**
     * Converts a string to an MD5 hash.
     */
    public String toMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    @Override
    public String normalize(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        String processedString = Arrays.stream(removeSpecialCharacters(text).split("\\s+"))
                .map(this::toLowerCase)
                .filter(this::removeStopwords)
                .map(this::reduceToMaxLength)
                .sorted()
                .collect(Collectors.joining(""));

        if(processedString.isEmpty()) {
            return "";
        }
        return toMD5(processedString); // Convert the processed string to MD5 hash
    }
}
