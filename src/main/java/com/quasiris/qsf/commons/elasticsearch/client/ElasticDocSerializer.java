package com.quasiris.qsf.commons.elasticsearch.client;

import com.quasiris.qsf.commons.util.JsonUtil;
import com.quasiris.qsf.dto.elasticsearch.IndexAction;
import org.apache.commons.lang3.StringUtils;

/**
 * Elasticsearch document serializer
 */
public class ElasticDocSerializer {
    public static String serializeBulk(IndexAction action) {
        String type = action.getType();
        if(StringUtils.isEmpty(type) || "_doc".equals(type)) {
            type = null;
        }


        StringBuilder bulkHeaderBuilder = new StringBuilder("{");
        bulkHeaderBuilder.append("\""+action.getOpType().getLowercase()+"\" : {");
        bulkHeaderBuilder.append("\"_index\" : \""+action.getIndex()+"\"");
        if(type != null) {
            bulkHeaderBuilder.append(", \"_type\" : \"" + type + "\"");
        }
        if(StringUtils.isNotEmpty(action.getDoc().getId())) {
            bulkHeaderBuilder.append(", \"_id\" : \"" + action.getDoc().getId() + "\"");
        }
        bulkHeaderBuilder.append("} }");
        String bulkHeader = bulkHeaderBuilder.toString();
        StringBuilder bulkLine = new StringBuilder();
        bulkLine.append(bulkHeader).append(" \n");
        if(action.getDoc() != null &&
            action.getDoc().getFields() != null &&
            action.getDoc().getFields().size() > 0) {
            String json = JsonUtil.toJson(action.getDoc().getFields());
            bulkLine.append(json);
        }
        return bulkLine.toString();
    }

    public static String serializeElasticdump(IndexAction action) {
        String type = action.getType();
        if(StringUtils.isEmpty(type)) {
            type = "_doc";
        }

        StringBuilder bulkLine = new StringBuilder("{");
        bulkLine.append("\"_index\" : \""+action.getIndex()+"\"");
        bulkLine.append(", \"_type\" : \"" + type + "\"");
        if(StringUtils.isNotEmpty(action.getDoc().getId())) {
            bulkLine.append(", \"_id\" : \"" + action.getDoc().getId() + "\"");
        }
        if(action.getDoc() != null &&
                action.getDoc().getFields() != null &&
                action.getDoc().getFields().size() > 0) {
            String json = JsonUtil.toJson(action.getDoc().getFields());
            bulkLine.append("\"_source\": "+json);
        }
        bulkLine.append("}");
        return bulkLine.toString();
    }
}
