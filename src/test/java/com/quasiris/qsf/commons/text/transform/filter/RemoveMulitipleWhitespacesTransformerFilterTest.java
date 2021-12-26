package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RemoveMulitipleWhitespacesTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new RemoveMultipleWhitespacesTransformerFilter();
        assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new RemoveMultipleWhitespacesTransformerFilter();
        assertEquals("foo Bar ", transformrFilter.transform("foo    Bar "));
    }

    @Test
    public void transformMultipleWhitespacesAtTheEnd() {
        TransformerFilter transformrFilter = new RemoveMultipleWhitespacesTransformerFilter();
        assertEquals("Bar ", transformrFilter.transform("Bar  "));
    }

    @Test
    public void transformSingleWhitespace() {
        TransformerFilter transformrFilter = new RemoveMultipleWhitespacesTransformerFilter();
        assertEquals(" ", transformrFilter.transform(" "));
    }
}