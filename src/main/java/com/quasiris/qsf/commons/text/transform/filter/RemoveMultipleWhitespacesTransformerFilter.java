package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;

public class RemoveMultipleWhitespacesTransformerFilter implements TransformerFilter {


    @Override
    public String transform(String text) {
        if(text == null) {
            return null;
        }
        int textLength = text.length();
        char[] newText = new char[textLength];
        int count = 0;

        boolean isLastCharacterWhitespace = false;
        for(int i = 0; i < textLength; ++i) {
            char c = text.charAt(i);
            if (Character.isWhitespace(c)) {
                if(!isLastCharacterWhitespace) {
                    isLastCharacterWhitespace = true;
                    newText[count++] = text.charAt(i);
                } else {

                }

            } else {
                isLastCharacterWhitespace = false;
                newText[count++] = text.charAt(i);
            }
        }

        if (count == textLength) {
            return text;
        } else {
            return new String(newText, 0, count);
        }
    }
}
