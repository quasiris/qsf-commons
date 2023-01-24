package com.quasiris.qsf.commons.repo;

import com.quasiris.qsf.commons.repo.config.*;
import com.quasiris.qsf.commons.aws.http.AwsCredentialsHelper;
import com.quasiris.qsf.commons.aws.http.AwsRequestSigner;
import com.quasiris.qsf.commons.util.HttpUtil;
import com.quasiris.qsf.dto.http.aws.AwsCredentialsValue;
import com.quasiris.qsf.commons.util.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Download and save models from a url to a local path.
 * If the model exists the local model is used.
 */
public class ModelRepositoryManager {
    private static final Logger logger = LoggerFactory.getLogger(ModelRepositoryManager.class);

    private String groupId;
    private String artifactId;
    private String version;

    private ModelRepositoryConfig config;

    protected String getModelName() {
        return artifactId + "-" + version;
    }

    protected String getUrlZipFile() {
        return getUrlPath() + getModelName() + ".zip";
    }

    public static String resolvePath(String modelname, String path) {
        if(StringUtils.isNotBlank(modelname) && StringUtils.isNotBlank(path)) {
            String relativeModelPath = resolvePath(modelname);
            path = path.replace(modelname, relativeModelPath);
        }
        return path;
    }

    public static String resolvePath(String modelname) {
        String path = null;
        if(StringUtils.isNotBlank(modelname)) {
            String[] parts = modelname.split("\\|");
            if(parts.length == 3) {
                parts[0] = parts[0].replaceAll("\\.", File.separator); // TODO this is not working under windows
                String artifact = parts[1]+"-"+parts[2];
                path = String.join(File.separator, parts);
                path = String.join(File.separator, path, artifact);
            } else {
                throw new RuntimeException("Invalid Modelname: "+modelname);
            }
        }

        return path;
    }

    protected String getUrlPath() {
        StringBuilder groupIdPath = new StringBuilder(groupId.replaceAll(Pattern.quote("."), "/"));
        groupIdPath.append("/");
        groupIdPath.append(artifactId);
        groupIdPath.append("/");
        groupIdPath.append(version);
        groupIdPath.append("/");
        return groupIdPath.toString();
    }


    /**
     * Zip the source directory and save it to the specified path.
     *
     * @param sourceDir the source directory to zip.
     * @throws IOException if the zip file can not be created or saved.
     */
    public void save(String sourceDir) throws IOException {
        String absoluteModelPath =  getAbsoluteModelPath();
        IOUtils.createDirectoryIfNotExists(absoluteModelPath);
        String zipFileName = getZipFile();
        IOUtils.zip(sourceDir, getModelName(), zipFileName);
    }

    public void saveAndUpload(String sourceDir) throws IOException {
        save(sourceDir);
        upload(getZipFile());
    }

    /**
     * Save the input stream as a zip file in the correct repository structure.
     * The zip file must be created in a correct way. See TODO
     *
     * @param zipFileInputStream input stream of the zip file
     * @throws IOException if the file can not be saved
     */
    public void save(InputStream zipFileInputStream) throws IOException {
        String absoluteModelPath =  getAbsoluteModelPath();
        IOUtils.createDirectoryIfNotExists(absoluteModelPath);
        String zipFile = getZipFile();
        IOUtils.copyInputStreamToFile(zipFileInputStream, new File(zipFile));
    }


    /**
     * Load a specific file from the zipped model directory.
     * If the model does not exists, it is installed.
     *
     * @param fileName the file name to be loaded.
     * @return the inputstream of the file.
     * @throws FileNotFoundException if the file was not found.
     */
    public InputStream load(String fileName) throws FileNotFoundException {
        install();
        String absoluteFile = getAbsoluteModelFile(fileName);
        return new FileInputStream(new File(absoluteFile));
    }

    protected boolean isInstalled() {
        Path path = Paths.get(getAbsoluteModelFile());
        return Files.exists(path)
                && Files.isReadable(path)
                && Files.isWritable(path);
    }

    public void install() {
        if(!isInstalled()) {
            logger.warn("Model {} is not installed locally!", getModelName());
            download();
            unzip();
        } else {
            logger.info("Found installed model {}", getModelName());
        }
    }

    protected String getAbsoluteModelPath() {
        return getConfig().getModelBasePath() + getUrlPath();
    }

    public String getAbsoluteModelFile() {
        return getConfig().getModelBasePath() + getUrlPath()  + getModelName() + "/";
    }

    public String getAbsoluteModelFile(String fileName) {
        return getAbsoluteModelFile() + fileName;
    }

    protected String getUploadUrl() {

        StringBuilder uploadUrl = new StringBuilder(getConfig().getUploadBaseUrl());
        uploadUrl.append(groupId);
        uploadUrl.append("/");
        uploadUrl.append(artifactId);
        uploadUrl.append("/");
        uploadUrl.append(version);

        return uploadUrl.toString();
    }

    public void upload(String fileName) throws IOException {
        String url = getUploadUrl();

        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            File file = new File(fileName);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, fileName);
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            CloseableHttpResponse response = httpclient.execute(post);

            if(response.getCode() >= 300) {
                String responseBody = null;
                try {
                    responseBody = EntityUtils.toString(response.getEntity());
                } catch (ParseException e) {
                    responseBody = "COULD NOT BE PARSED!";
                }
                throw new RuntimeException("Could not upload file " + fileName +
                        " to url: " + url +
                        " http code: " + response.getCode() +
                        " message: " +  responseBody);
            }
            httpclient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void download() {

        if(getConfig() == null || getConfig().getDownloadConfig() == null) {
            throw new RuntimeException("No config found to download the data.");
        }


        String path = getAbsoluteModelPath();

        IOUtils.createDirectoryIfNotExists(path);
        String modelLocation = "undefined";

        try {

            DownloadConfig downloadConfig = getConfig().getDownloadConfig();

            if (DownloadConfig.AWS_TYPE.equals(downloadConfig.getType()) && downloadConfig.getAws() != null) {
                String modelUrl =  downloadConfig.getAws().getBaseUrl() + getUrlZipFile();
                modelLocation = modelUrl;
                logger.info("Downloading model {} from url: {} to path: {} ", getModelName(), modelUrl, getZipFile());

                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(modelUrl);
                AwsDownloadConfig aws = downloadConfig.getAws();
                AwsCredentialsValue credentials = AwsCredentialsHelper.getCredentials(aws.getCredentials());
                Map<String, String> headers = AwsRequestSigner.signRequest(httpGet.getUri(),
                        httpGet.getMethod(),
                        credentials,
                        aws.getService(),
                        aws.getRegion());
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
                }
                executeGetModels(httpGet);
            } else if (DownloadConfig.HTTP_TYPE.equals(downloadConfig.getType()) && downloadConfig.getHttp() != null) {
                HttpDownloadConfig httpDownloadConfig = downloadConfig.getHttp();
                String modelUrl =  httpDownloadConfig.getBaseUrl() + getUrlZipFile();
                modelLocation = modelUrl;
                logger.info("Downloading model {} from url: {} to path: {} ", getModelName(), modelUrl, getZipFile());
                HttpGet httpGet = new HttpGet(modelUrl);
                BasicAuth basic = httpDownloadConfig.getBasic();
                if (basic != null) {
                    httpGet.addHeader("Authorization", HttpUtil.createBasicAuthHeaderValue(basic.getUsername(),
                            basic.getPassword()));
                }
                executeGetModels(httpGet);
            }


            logger.info("Unzipping finished!");
        } catch (Exception e) {
            throw new RuntimeException("Something gone wrong while downloading model file from " + modelLocation, e);
        }
    }

    private void executeGetModels(HttpGet httpGet) throws IOException, ParseException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(httpGet);
        if (response.getCode() < 300) {
            try (InputStream inputStream = response.getEntity().getContent()) {
                Files.copy(inputStream, Paths.get(getZipFile()), StandardCopyOption.REPLACE_EXISTING);
            }
            httpclient.close();
        } else {
            String responseBody = EntityUtils.toString(response.getEntity());
            int statusCode = response.getCode();
            httpclient.close();
            throw new HttpResponseException(statusCode, responseBody);
        }
    }

    protected String getZipFile() {
        return getConfig().getModelBasePath() + getUrlZipFile();
    }



    protected void unzip() {
        String zipFile = getZipFile();
        try {
            logger.info("Unzipping downloaded file {}", zipFile);
            IOUtils.unzip(zipFile);
            logger.info("Finished Unzipping file {}" , zipFile);
        } catch (IOException e) {
            throw new RuntimeException("Could not unzip file: " + zipFile, e);
        }
    }

    public static final class Builder {
        private String groupId;
        private String artifactId;
        private String version;

        private ModelRepositoryConfig config;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder shortId(String shortId) {
            String[] splitted = shortId.split(Pattern.quote("|"));
            if(splitted.length < 3) {
                throw new IllegalArgumentException("The short id: " + shortId + " is invalid.");
            }
            this.groupId = splitted[0];
            this.artifactId = splitted[1];
            this.version = splitted[2];
            return this;
        }

        public Builder config(ModelRepositoryConfig config) {
            this.config =  config;
            return this;
        }
        public Builder useDefaultConfig() {
            config =  ModelRepositoryConfigHolder.getModelRepositoryConfig();
            return this;
        }

        public Builder groupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder artifactId(String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public ModelRepositoryManager build() {
            ModelRepositoryManager modelRepositoryManager =  new ModelRepositoryManager();
            modelRepositoryManager.setGroupId(groupId);
            modelRepositoryManager.setArtifactId(artifactId);
            modelRepositoryManager.setVersion(version);
            modelRepositoryManager.setConfig(config);
            return modelRepositoryManager;
        }
    }

    /**
     * Getter for property 'groupId'.
     *
     * @return Value for property 'groupId'.
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Setter for property 'groupId'.
     *
     * @param groupId Value to set for property 'groupId'.
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * Getter for property 'artifactId'.
     *
     * @return Value for property 'artifactId'.
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Setter for property 'artifactId'.
     *
     * @param artifactId Value to set for property 'artifactId'.
     */
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * Getter for property 'version'.
     *
     * @return Value for property 'version'.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Setter for property 'version'.
     *
     * @param version Value to set for property 'version'.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public ModelRepositoryConfig getConfig() {
        return config;
    }

    public void setConfig(ModelRepositoryConfig config) {
        this.config = config;
    }
}
