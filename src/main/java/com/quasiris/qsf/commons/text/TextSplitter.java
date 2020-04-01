package com.quasiris.qsf.commons.text;

import java.util.List;

/**
 * Split text for multiple purposes
 */
public interface TextSplitter {
    List<String> split(String text);
}
