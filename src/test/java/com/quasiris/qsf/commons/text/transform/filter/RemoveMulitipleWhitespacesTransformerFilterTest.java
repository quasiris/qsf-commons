package com.quasiris.qsf.commons.text.transform.filter;

import com.quasiris.qsf.commons.text.transform.TransformerFilter;
import org.junit.Assert;
import org.junit.Test;

public class RemoveMulitipleWhitespacesTransformerFilterTest {


    @Test
    public void transformNull() {
        TransformerFilter transformrFilter = new RemoveMultipleWhitespacesTransformerFilter();
        Assert.assertNull(transformrFilter.transform(null));
    }

    @Test
    public void transform() {
        TransformerFilter transformrFilter = new RemoveMultipleWhitespacesTransformerFilter();
        Assert.assertEquals("foo Bar ", transformrFilter.transform("foo    Bar "));
    }

    @Test
    public void transformMultipleWhitespacesAtTheEnd() {
        TransformerFilter transformrFilter = new RemoveMultipleWhitespacesTransformerFilter();
        Assert.assertEquals("Bar ", transformrFilter.transform("Bar  "));
    }

    @Test
    public void transformSingleWhitespace() {
        TransformerFilter transformrFilter = new RemoveMultipleWhitespacesTransformerFilter();
        Assert.assertEquals(" ", transformrFilter.transform(" "));
    }
}