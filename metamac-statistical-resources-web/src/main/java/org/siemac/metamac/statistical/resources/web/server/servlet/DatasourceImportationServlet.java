package org.siemac.metamac.statistical.resources.web.server.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dto.BasicVersionableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.task.domain.DatasetFileFormatEnum;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.core.task.domain.AlternativeEnumeratedRepresentation;
import org.siemac.metamac.statistical.resources.core.task.domain.FileDescriptor;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoDataset;
import org.siemac.metamac.statistical.resources.web.client.WebMessageExceptionsConstants;
import org.siemac.metamac.statistical.resources.web.shared.utils.ImportableResourceTypeEnum;
import org.siemac.metamac.statistical.resources.web.shared.utils.StatisticalResourcesSharedTokens;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.server.utils.ZipUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.google.inject.Singleton;

@Singleton
@SuppressWarnings("serial")
public class DatasourceImportationServlet extends BaseHttpServlet {

    private static Logger           logger                                      = Logger.getLogger(DatasourceImportationServlet.class.getName());
    protected static final String[] FIELDS_VERSIONABLE_STATISTICAL_RESOURCE_DTO = new String[]{StatisticalResourcesSharedTokens.UPLOAD_VERSION_RATIONALE_TYPES,
            StatisticalResourcesSharedTokens.UPLOAD_NEXT_VERSION, StatisticalResourcesSharedTokens.UPLOAD_DATE_NEXT_UPDATE, StatisticalResourcesSharedTokens.UPLOAD_DATE_NEXT_VERSION,
            StatisticalResourcesSharedTokens.UPLOAD_UPDATE_FREQUENCY, StatisticalResourcesSharedTokens.UPLOAD_HAS_EXTRA_FIELDS, StatisticalResourcesSharedTokens.UPLOAD_PROC_STATUS,
            StatisticalResourcesSharedTokens.UPLOAD_DATA_PROVIDER};

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        logger.info("FileUpload Servlet");
        tmpDir = new File(((File) getServletContext().getAttribute("javax.servlet.context.tempdir")).toString());
        logger.info("tmpDir: " + tmpDir.toString());
    }

    @Override
    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check that we have a file upload request
        if (ServletFileUpload.isMultipartContent(request)) {
            processFiles(request, response);
        } else {
            processQuery(request, response);
        }
    }

    @SuppressWarnings("rawtypes")
    private void processFiles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HashMap<String, String> args = new HashMap<String, String>();

        String fileName = new String();
        InputStream inputStream = null;
        Boolean mustBeZip = false;
        BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto = new BasicVersionableStatisticalResourceDto();
        try {

            DiskFileItemFactory factory = new DiskFileItemFactory();
            // Get the temporary directory (this is where files that exceed the threshold will be stored)
            factory.setRepository(tmpDir);

            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding(StandardCharsets.UTF_8.name());
            // Parse the request
            List items = upload.parseRequest(request);

            // Process the uploaded items
            Iterator itr = items.iterator();

            while (itr.hasNext()) {
                DiskFileItem item = (DiskFileItem) itr.next();
                if (item.isFormField()) {
                    getFormFields(item, basicVersionableStatisticalResourceDto, args);
                } else {
                    fileName = item.getName();
                    inputStream = item.getInputStream();
                }
            }

            String tempZipFilePathName = inputStreamToTempFile(fileName, inputStream);
            File outputFolder = (File) getServletContext().getAttribute("javax.servlet.context.tempdir");
            File uploadedFile = new File(tempZipFilePathName);

            mustBeZip = BooleanUtils.toBoolean(args.get(StatisticalResourcesSharedTokens.UPLOAD_MUST_BE_ZIP_FILE));

            ImportableResourceTypeEnum importableResourceType = getImportableResourceType(args);
            if (ImportableResourceTypeEnum.PUBLICATION_VERSION_STRUCTURE.equals(importableResourceType)) {
                importPublicationVersionStructure(uploadedFile, args);
            } else {
                importElement(args, mustBeZip, basicVersionableStatisticalResourceDto, outputFolder, uploadedFile);
            }

            sendSuccessImportationResponse(response, fileName, mustBeZip, basicVersionableStatisticalResourceDto.getAutomaticLifeCicle());

        } catch (Exception e) {

            String errorMessage = null;
            if (e instanceof MetamacException) {
                errorMessage = WebExceptionUtils.serializeToJson((MetamacException) e);
            } else if (e instanceof MetamacWebException) {
                errorMessage = getMessageFromMetamacWebException((MetamacWebException) e);
                errorMessage = StringEscapeUtils.escapeJavaScript(errorMessage);
            } else {
                errorMessage = e.getMessage();
                errorMessage = StringEscapeUtils.escapeJavaScript(errorMessage);
            }

            logger.log(Level.SEVERE, "Error importing file = " + fileName + ". " + e.getMessage());
            logger.log(Level.SEVERE, e.getMessage());

            sendFailedImportationResponse(response, errorMessage, mustBeZip, basicVersionableStatisticalResourceDto.getAutomaticLifeCicle());
        }
    }

    private void importElement(HashMap<String, String> args, Boolean mustBeZip, BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto, File outputFolder, File uploadedFile)
            throws MetamacWebException, ZipException, IOException, MetamacException {
        if (BooleanUtils.toBoolean(args.get(StatisticalResourcesSharedTokens.LOAD_PARAM_ATTRIBUTES))) {
            importAttributes(uploadedFile, args, basicVersionableStatisticalResourceDto);
        } else {
            importDatasource(mustBeZip, uploadedFile, outputFolder, args, basicVersionableStatisticalResourceDto);
        }
    }

    private void getFormFields(DiskFileItem item, BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto, HashMap<String, String> args) throws ParseException {
        if (Arrays.asList(FIELDS_VERSIONABLE_STATISTICAL_RESOURCE_DTO).contains(item.getFieldName())) {
            fillBasicVersionableStatisticalResourceDto(item, basicVersionableStatisticalResourceDto);
        } else {
            args.put(item.getFieldName(), item.getString());
        }
    }

    private void fillBasicVersionableStatisticalResourceDto(DiskFileItem item, BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto) throws ParseException {

        if (StatisticalResourcesSharedTokens.UPLOAD_DATA_PROVIDER.equals(item.getFieldName()) && !StringUtils.isEmpty(item.getString())) {
            getListDataProvidersUrnFromRequest(item.getString().split(","), basicVersionableStatisticalResourceDto);
        }

        if (StatisticalResourcesSharedTokens.UPLOAD_VERSION_RATIONALE_TYPES.equals(item.getFieldName())) {
            getListVersionRationaleTypeFromRequest(item.getString().split(","), basicVersionableStatisticalResourceDto);
        }

        if (StatisticalResourcesSharedTokens.UPLOAD_DATE_NEXT_VERSION.equals(item.getFieldName())) {
            basicVersionableStatisticalResourceDto.setNextVersionDate(item.getString());
        }

        if (StatisticalResourcesSharedTokens.UPLOAD_NEXT_VERSION.equals(item.getFieldName())) {
            basicVersionableStatisticalResourceDto.setNextVersion(item.getString());
        }

        if (StatisticalResourcesSharedTokens.UPLOAD_DATE_NEXT_UPDATE.equals(item.getFieldName())) {
            basicVersionableStatisticalResourceDto.setNextUpdateDate(item.getString());
        }

        if (StatisticalResourcesSharedTokens.UPLOAD_UPDATE_FREQUENCY.equals(item.getFieldName())) {
            basicVersionableStatisticalResourceDto.setUpdateFrequency(item.getString());
        }

        if (StatisticalResourcesSharedTokens.UPLOAD_PROC_STATUS.equals(item.getFieldName())) {
            basicVersionableStatisticalResourceDto.setNextProcStatus(item.getString());
        }

        if (StatisticalResourcesSharedTokens.UPLOAD_HAS_EXTRA_FIELDS.equals(item.getFieldName())) {
            basicVersionableStatisticalResourceDto.setAutomaticLifeCicle(BooleanUtils.toBoolean(item.getString()));
        }
    }

    private void getListVersionRationaleTypeFromRequest(String[] versionRationaleType, BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto) {

        for (String itemVersionRationaleType : versionRationaleType) {
            basicVersionableStatisticalResourceDto.getVersionRationaleTypes().add((itemVersionRationaleType));
        }
    }

    private void getListDataProvidersUrnFromRequest(String[] dataProvidersUrn, BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto) {
        for (String itemDataProviderUrn : dataProvidersUrn) {
            basicVersionableStatisticalResourceDto.getDataProvidersUrn().add((itemDataProviderUrn));
        }
    }

    private void importDatasource(Boolean mustBeZip, File uploadedFile, File outputFolder, HashMap<String, String> args, BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto)
            throws MetamacWebException, ZipException, IOException, MetamacException {

        StatisticalResourcesServiceFacade statisticalResourcesServiceFacade = (StatisticalResourcesServiceFacade) ApplicationContextProvider.getApplicationContext()
                .getBean(StatisticalResourcesServiceFacade.BEAN_ID);

        List<File> filesToImport = new ArrayList<File>();

        Boolean storeDimensionsMapping = null;
        boolean isFileZip = isZip(uploadedFile);

        if (isFileZip) {
            // If the uploaded file is a zip, the mapping cannot be set by the user. That's why the mappings are not stored in this case.
            storeDimensionsMapping = false;
            filesToImport = ZipUtils.unzipArchive(uploadedFile, outputFolder);
        } else {
            storeDimensionsMapping = true;
            filesToImport.add(uploadedFile);
        }

        List<URL> fileUrls = getURLsFromFiles(filesToImport);

        String statisticalOperationCode = args.get(StatisticalResourcesSharedTokens.UPLOAD_PARAM_OPERATION_CODE);
        String datasetVersionUrn = args.get(StatisticalResourcesSharedTokens.UPLOAD_PARAM_DATASET_VERSION_URN);

        if (BooleanUtils.isTrue(mustBeZip) && !isFileZip) {
            throwMetamacWebException(WebMessageExceptionsConstants.ERROR_IMPORT_IS_NOT_ZIP);
        } else if (BooleanUtils.isFalse(mustBeZip) && isFileZip) {
            throwMetamacWebException(WebMessageExceptionsConstants.ERROR_IMPORT_IS_ZIP);
        }

        MetamacException exceptionsJobPlanifying = null;
        if (StringUtils.isNotBlank(datasetVersionUrn)) {
            Map<String, String> dimensionMapping = buildDimensionsMappings(args);
            DatasetVersionDto datasetVersionDto = statisticalResourcesServiceFacade.retrieveDatasetVersionByUrn(ServiceContextHolder.getCurrentServiceContext(), datasetVersionUrn);
            statisticalResourcesServiceFacade.importDatasourcesInDatasetVersion(ServiceContextHolder.getCurrentServiceContext(), datasetVersionDto, fileUrls, dimensionMapping, storeDimensionsMapping,
                    new BasicVersionableStatisticalResourceDto());
        } else if (StringUtils.isNotBlank(statisticalOperationCode)) {
            exceptionsJobPlanifying = statisticalResourcesServiceFacade.importDatasourcesInStatisticalOperation(ServiceContextHolder.getCurrentServiceContext(), statisticalOperationCode, fileUrls,
                    basicVersionableStatisticalResourceDto);
            if (exceptionsJobPlanifying != null) {
                throw exceptionsJobPlanifying;
            }
        }
    }

    private void importAttributes(File uploadedFile, HashMap<String, String> args, BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto)
            throws MetamacWebException, ZipException, IOException, MetamacException {

        StatisticalResourcesServiceFacade datasetService = (StatisticalResourcesServiceFacade) ApplicationContextProvider.getApplicationContext().getBean(StatisticalResourcesServiceFacade.BEAN_ID);
        List<File> filesToImport = new ArrayList<File>();
        filesToImport.add(uploadedFile);

        List<URL> fileUrls = getURLsFromFiles(filesToImport);

        String datasetVersionUrn = args.get(StatisticalResourcesSharedTokens.UPLOAD_PARAM_DATASET_VERSION_URN);

        DatasetVersionDto datasetVersionDto = datasetService.retrieveDatasetVersionByUrn(ServiceContextHolder.getCurrentServiceContext(), datasetVersionUrn);

        datasetService.importAttributesFromFile(ServiceContextHolder.getCurrentServiceContext(), datasetVersionDto, fileUrls);

    }

    private TaskInfoDataset buildImportationTaskInfo(DatasetVersionDto datasetVersion, List<URL> fileUrls, Map<String, String> dimensionRepresentationMapping,
            boolean storeDimensionRepresentationMapping, BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto) {
        String datasetVersionUrn = datasetVersion.getUrn();

        TaskInfoDataset taskInfo = new TaskInfoDataset();
        taskInfo.setDatasetUrn(datasetVersion.getUrn());
        taskInfo.setDatasetVersionId(datasetVersionUrn);
        taskInfo.setDataStructureUrn(datasetVersion.getRelatedDsd().getUrn());
        taskInfo.setStoreAlternativeRepresentations(storeDimensionRepresentationMapping);
        taskInfo.setStatisticalOperationUrn(datasetVersion.getStatisticalOperation().getUrn());
        taskInfo.setDatasetNextVersion(basicVersionableStatisticalResourceDto.getNextVersion());
        taskInfo.setDatasetNextVersionDate(basicVersionableStatisticalResourceDto.getNextVersionDate());
        taskInfo.setDatasetNextUpdateDate(basicVersionableStatisticalResourceDto.getNextUpdateDate());
        taskInfo.setDatasetUpdateFrequency(basicVersionableStatisticalResourceDto.getUpdateFrequency());
        taskInfo.setDatasetVersionDataProviderUrn(basicVersionableStatisticalResourceDto.getDataProvidersUrn());
        taskInfo.setDatasetVersionRationaleTypes(basicVersionableStatisticalResourceDto.getVersionRationaleTypes());
        taskInfo.setDatasetNextProcStatus(basicVersionableStatisticalResourceDto.getNextProcStatus());
        taskInfo.setDatasetAutomaticLifeCicle(basicVersionableStatisticalResourceDto.getAutomaticLifeCicle());
        for (String dimensionId : dimensionRepresentationMapping.keySet()) {
            AlternativeEnumeratedRepresentation representation = new AlternativeEnumeratedRepresentation();
            representation.setComponentId(dimensionId);
            representation.setUrn(dimensionRepresentationMapping.get(dimensionId));
            taskInfo.getAlternativeRepresentations().add(representation);
        }

        for (URL url : fileUrls) {
            String filename = getFilenameFromPath(url.getPath());
            DatasetFileFormatEnum format = calculateFileFormat(filename);
            FileDescriptor fileDescriptor = new FileDescriptor(new File(url.getPath()), filename, format);
            taskInfo.addFile(fileDescriptor);
        }

        return taskInfo;
    }

    private String getFilenameFromPath(String path) {
        String base = FilenameUtils.getBaseName(path);
        String extension = FilenameUtils.getExtension(path);
        if (StringUtils.isEmpty(extension)) {
            return base;
        }
        return base + "." + extension;
    }

    private DatasetFileFormatEnum calculateFileFormat(String filename) {
        if (filename.endsWith(StatisticalResourcesConstants.PX_EXTENSION)) {
            return DatasetFileFormatEnum.PX;
        } else if (filename.endsWith(StatisticalResourcesConstants.SDMX_EXTENSION)) {
            return DatasetFileFormatEnum.SDMX_2_1;
        } else {
            return DatasetFileFormatEnum.CSV;
        }
    }

    private void importPublicationVersionStructure(File uploadedFile, HashMap<String, String> args) throws MetamacException, MalformedURLException {

        StatisticalResourcesServiceFacade statisticalResourcesServiceFacade = (StatisticalResourcesServiceFacade) ApplicationContextProvider.getApplicationContext()
                .getBean(StatisticalResourcesServiceFacade.BEAN_ID);

        String publicationVersionUrn = args.get(StatisticalResourcesSharedTokens.UPLOAD_PARAM_PUBLICATION_VERSION_URN);
        String language = args.get(StatisticalResourcesSharedTokens.UPLOAD_PARAM_LANGUAGE);

        statisticalResourcesServiceFacade.importPublicationVersionStructure(ServiceContextHolder.getCurrentServiceContext(), publicationVersionUrn, uploadedFile.toURI().toURL(), language);
    }

    private ImportableResourceTypeEnum getImportableResourceType(HashMap<String, String> args) {
        try {
            return ImportableResourceTypeEnum.valueOf(args.get(StatisticalResourcesSharedTokens.UPLOAD_RESOURCE_TYPE));
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, String> buildDimensionsMappings(HashMap<String, String> args) {
        Map<String, String> mapping = new HashMap<String, String>();
        for (String itemId : args.keySet()) {
            if (itemId.startsWith(StatisticalResourcesSharedTokens.UPLOAD_PARAM_DIM_PREFIX)) {
                String dimensionId = itemId.substring(StatisticalResourcesSharedTokens.UPLOAD_PARAM_DIM_PREFIX.length());
                String codelistUrn = args.get(itemId);
                if (!StringUtils.isEmpty(codelistUrn)) {
                    mapping.put(dimensionId, codelistUrn);
                }
            }
        }
        return mapping;
    }

    private void processQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    //
    // UTILITY METHODS
    //

    private void sendSuccessImportationResponse(HttpServletResponse response, String message, boolean zipUpload, Boolean isZipWithAutomaticLifeCicle) throws IOException {
        String functionName = (zipUpload && !Boolean.TRUE.equals(isZipWithAutomaticLifeCicle)) ? "uploadZipComplete" : "uploadComplete";
        sendImportationResponse(response, message, functionName);
    }

    private void sendFailedImportationResponse(HttpServletResponse response, String errorMessage, boolean zipUpload, Boolean isZipWithAutomaticLifeCicle) throws IOException {
        String functionName = (zipUpload && !Boolean.TRUE.equals(isZipWithAutomaticLifeCicle)) ? "uploadZipFailed" : "uploadFailed";
        sendImportationResponse(response, errorMessage, functionName);
    }

    private void sendImportationResponse(HttpServletResponse response, String message, String functionName) throws IOException {
        String action = "if (parent." + functionName + ") parent." + functionName + "('" + message + "');";
        sendResponse(response, action);
    }

    private boolean isZip(File file) {
        if (file != null) {
            // return org.springframework.http.MediaType.APPLICATION_OCTET_STREAM.toString().equals(contentType) || "application/zip".equals(contentType);
            return file.getName().endsWith("zip");
        }
        return false;
    }
}
