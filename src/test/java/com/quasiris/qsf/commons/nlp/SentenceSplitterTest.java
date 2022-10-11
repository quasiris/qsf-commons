package com.quasiris.qsf.commons.nlp;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class SentenceSplitterTest {

    @Test
    void split() {
        // given
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum interdum lorem mauris, id tempus leo condimentum molestie. Sed sit amet pellentesque velit, viverra scelerisque risus. Aenean vitae ornare nibh. Integer ut pretium ligula. Fusce laoreet, ligula id sollicitudin ultricies, ante enim faucibus ligula, in semper metus ipsum non odio. Nam lectus neque, commodo non volutpat ut, vehicula eget magna. Cras porttitor placerat purus quis condimentum. Sed tristique ut nunc eget interdum. Morbi tincidunt pulvinar turpis sit amet vulputate. Donec iaculis molestie semper. Praesent eu porttitor tortor. Nam dapibus scelerisque nisi, sit amet pulvinar sem elementum ut. Fusce sodales eros id purus aliquam fringilla. Integer consequat iaculis enim sit amet sodales.\n" +
                "Donec sollicitudin ligula vitae dolor sollicitudin, ac aliquam purus convallis. Nam finibus in sapien vel lobortis. Integer condimentum nibh at dui consectetur pellentesque. Praesent erat purus, pulvinar at leo ut, interdum tristique sapien. Phasellus eget erat urna. Praesent finibus non nunc vel blandit. Integer consectetur bibendum placerat. Sed eu risus non odio pretium hendrerit. Nullam in orci interdum, porta magna sed, auctor velit. In hac habitasse platea dictumst. Suspendisse tempus feugiat hendrerit. In tempor nunc nulla, suscipit fringilla tellus aliquam a. Ut blandit massa quis augue aliquet porttitor. Donec bibendum libero a leo cursus egestas. Quisque in blandit dolor.\n" +
                "Vestibulum imperdiet a purus quis ullamcorper. Cras at ante arcu. Sed feugiat finibus est eget convallis. Etiam varius diam eu mattis tempor. Etiam et purus magna. Phasellus egestas egestas erat, sed ultrices nibh auctor id. Integer sodales ligula quis augue aliquet suscipit. Aenean vel urna sit amet dui rutrum efficitur. Mauris fringilla elit a justo luctus bibendum. Donec efficitur, orci in consectetur semper, libero lorem faucibus mauris, quis rhoncus felis lacus quis turpis.\n" +
                "Phasellus sagittis scelerisque mi, nec lacinia dolor lacinia eu. Curabitur eget vestibulum nisi, eu interdum ante. Mauris aliquam varius lacus eu ultricies. Vivamus bibendum mauris quis enim interdum, in finibus sem dapibus. Cras vestibulum sed mauris sed imperdiet. Fusce massa elit, ornare ac sapien nec, sagittis porttitor arcu. Donec eget nisi gravida, porttitor dolor id, bibendum urna. Morbi venenatis justo in dictum rutrum. Aenean scelerisque sem justo, non suscipit magna fringilla pellentesque. Morbi lectus lorem, volutpat eu nunc eu, malesuada ornare dui.\n" +
                "Maecenas suscipit est ut rutrum dignissim. Proin venenatis lectus in magna elementum bibendum. Etiam feugiat imperdiet metus vitae consectetur. Morbi vel metus a nulla tincidunt sollicitudin quis nec elit. Sed accumsan tempus quam. Proin tempus felis ut ornare volutpat. Aenean elementum auctor molestie. Sed consequat nunc porta felis lacinia, in congue lectus semper. Morbi ut arcu velit. Mauris pellentesque, ex a varius iaculis, nunc ligula accumsan tellus, quis posuere lacus dui eget dui. Maecenas blandit pulvinar sapien, nec rutrum dolor sodales nec. Vestibulum gravida justo sed est sagittis elementum. Nulla mollis urna vel risus placerat sodales. Proin tincidunt scelerisque tristique. Aliquam sagittis faucibus nibh. Sed est neque, aliquet id diam ut, rhoncus iaculis sapien. ";

        // when
        SentenceSplitter splitter = new SentenceSplitter();
        List<String> textList = splitter.split(text);

        // then
        assertNotNull(textList);
        assertEquals(27, textList.size());
    }

    @Test
    void splitText_checkMinLength() {
        // given
        String text = "Lorem ipsum. dolor sit. amet, consectetur adipiscing elit. Vestibulum interdum lorem mauris, id tempus leo condimentum molestie. Sed sit amet pellentesque velit, viverra scelerisque risus. Aenean vitae ornare nibh. Integer ut pretium ligula. Fusce laoreet, ligula id sollicitudin ultricies, ante enim faucibus ligula, in semper metus ipsum non odio. Nam lectus neque, commodo non volutpat ut, vehicula eget magna. Cras porttitor placerat purus quis condimentum. Sed tristique ut nunc eget interdum. Morbi tincidunt pulvinar turpis sit amet vulputate. Donec iaculis molestie semper. Praesent eu porttitor tortor. Nam dapibus scelerisque nisi, sit amet pulvinar sem elementum ut. Fusce sodales eros id purus aliquam fringilla. Integer consequat iaculis enim sit amet sodales.\n";
        Integer minLength = 200;

        // when
        SentenceSplitter splitter = new SentenceSplitter(Locale.GERMAN, minLength);
        List<String> textList = splitter.split(text);

        // then
        assertNotNull(textList);
        assertEquals(4, textList.size());
    }

    @Test
    void splitText_checkLast() {
        // given
        String text = "Lorem ipsum. dolor sit. amet, consectetur adipiscing elit. Vestibulum interdum lorem mauris, id tempus leo condimentum molestie. Sed sit amet pellentesque velit, viverra scelerisque risus. Aenean vitae ornare nibh. Integer ut pretium ligula. Fusce laoreet, ligula id sollicitudin ultricies, ante enim faucibus ligula, in semper metus ipsum non odio. Nam lectus neque, commodo non volutpat ut, vehicula eget magna. Cras porttitor placerat purus quis condimentum. Sed tristique ut nunc eget interdum. Morbi tincidunt pulvinar turpis sit amet vulputate. Donec iaculis molestie semper. Praesent eu porttitor tortor. Nam dapibus scelerisque nisi, sit amet pulvinar sem elementum ut. Fusce sodales eros id purus aliquam fringilla. Integer consequat iaculis enim sit amet sodales.\n";
        Integer minLength = 100000;

        // when
        SentenceSplitter splitter = new SentenceSplitter(Locale.GERMAN, minLength);
        List<String> textList = splitter.split(text);

        // then
        assertNotNull(textList);
        assertEquals(1, textList.size());
    }
}