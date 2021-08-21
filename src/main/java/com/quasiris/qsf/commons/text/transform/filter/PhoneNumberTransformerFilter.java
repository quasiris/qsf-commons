package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.phone.PhoneNumberCountryCodes;
import com.quasiris.qsf.commons.text.SpecialChars;
import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PhoneNumberTransformerFilter implements TransformerFilter {



    @Override
    public String transform(String text) {
        if(text == null) {
            return null;
        }

        KeepCharTransformerFilter keepCharTransformerFilter = new KeepCharTransformerFilter(SpecialChars.NUMBERS + "+");
        String normalized = keepCharTransformerFilter.transform(text);
        if(normalized.startsWith("+")) {
            normalized = normalized.replaceFirst(Pattern.quote("+"), "00");
        }

        if(normalized.startsWith("00")) {
            int len = normalized.length();
            for(String countryCode : PhoneNumberCountryCodes.COUNTRY_CODES) {
                normalized = normalized.replaceFirst(countryCode, "0");
                if(len != normalized.length()) {
                    break;
                }
            }
        }

        return normalized;

    }
}
