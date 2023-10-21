package com.quasiris.qsf.commons.http.java.helper;

import com.quasiris.qsf.commons.http.java.model.HttpMetadata;
import com.quasiris.qsf.commons.http.java.model.multipart.MultipartUploadItem;
import com.quasiris.qsf.commons.http.java.model.multipart.MultipartUploadRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.http.HttpRequest;

public class PublisherHelper {

    public static HttpRequest.BodyPublisher getMultipartUploadPublisher(MultipartUploadRequest request, HttpMetadata metadata) {
        String boundary = metadata.getRequest().getBoundary();
        byte[] endBoundary = ("--" + boundary + "--\r\n").getBytes();
        InputStream finalStream = new ByteArrayInputStream(new byte[0]);
        for (MultipartUploadItem item : request.getItems()) {
            InputStream filePartStream = prepareFilePart(item, boundary);
            finalStream = new SequenceInputStream(finalStream, filePartStream);
        }
        finalStream = new SequenceInputStream(finalStream, new ByteArrayInputStream(endBoundary));

        return getBodyPublisher(finalStream);
    }

    private static InputStream prepareFilePart(MultipartUploadItem item, String boundary) {
        byte[] start = ("--" + boundary + "\r\n" +
                        "Content-Disposition: form-data; name=\"" + item.getFileName() + "\"; filename=\"" + item.getFileName() + "\"\r\n" +
                        "Content-Type: " + item.getContentType() + "\r\n\r\n").getBytes();
        byte[] end = "\r\n".getBytes();

        InputStream preFileIS = new ByteArrayInputStream(start);
        InputStream is = item.getConentInputStream();
        InputStream postFileIS = new ByteArrayInputStream(end);
        return new SequenceInputStream(preFileIS, new SequenceInputStream(is, postFileIS));
    }

    private static HttpRequest.BodyPublisher getBodyPublisher(InputStream finalStream) {
        return HttpRequest.BodyPublishers.ofInputStream(() -> finalStream);
    }

}
