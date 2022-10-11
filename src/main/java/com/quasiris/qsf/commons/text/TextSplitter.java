package com.quasiris.qsf.commons.text;

import java.io.Serializable;
import java.util.List;

/**
 * Split text for multiple purposes
 */
public interface TextSplitter extends Serializable {
    List<String> split(String text);
}
