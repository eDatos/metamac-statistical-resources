package org.siemac.metamac.statistical.resources.web.server.servlet;

import static org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum.CODELIST;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DimensionRepresentationMappingDto;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.server.rest.SrmRestInternalFacadeImpl;
import org.siemac.metamac.statistical.resources.web.shared.ds.DimensionRepresentationMappingDS;
import org.siemac.metamac.statistical.resources.web.shared.dtos.DimensionRepresentationMappingWebDto;
import org.siemac.metamac.statistical.resources.web.shared.utils.StatisticalResourcesSharedTokens;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.google.inject.Singleton;

@Singleton
@SuppressWarnings("serial")
public class DatasourceImportationPreviewServlet extends BaseHttpServlet {

    private static Logger logger = Logger.getLogger(DatasourceImportationPreviewServlet.class.getName());

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

        try {
            
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // Get the temporary directory (this is where files that exceed the threshold will be stored)
            factory.setRepository(tmpDir);

            ServletFileUpload upload = new ServletFileUpload(factory);

            // Parse the request
            List items = upload.parseRequest(request);

            // Process the uploaded items
            Iterator itr = items.iterator();

            while (itr.hasNext()) {
                DiskFileItem item = (DiskFileItem) itr.next();
                if (item.isFormField()) {
                    args.put(item.getFieldName(), item.getString());
                } else {
                    fileName = item.getName();
                }
            }
            
            String message = getDimensionRepresentationMappingDto(fileName, args.get(StatisticalResourcesSharedTokens.UPLOAD_PARAM_DATASET_VERSION_URN));
            
            sendSuccessImportationResponse(response, StringEscapeUtils.escapeJavaScript(message));

        } catch (Exception e) {

            String errorMessage = null;
            if (e instanceof MetamacException) {
                errorMessage = WebExceptionUtils.serializeToJson((MetamacException) e);
            } else {
                errorMessage = e.getMessage();
                errorMessage = StringEscapeUtils.escapeJavaScript(errorMessage);
            }

            logger.log(Level.SEVERE, "Error importing file = " + fileName + ". " + e.getMessage());
            logger.log(Level.SEVERE, e.getMessage());

            sendFailedImportationResponse(response, errorMessage);
        }
    }

    private String getDimensionRepresentationMappingDto(String filename, String datasetVersionUrn) throws MetamacException, MetamacWebException {
        String message = null;
        if (datasetVersionUrn != null && !"".equals(datasetVersionUrn)) {
            StatisticalResourcesServiceFacade statisticalResourcesServiceFacade = (StatisticalResourcesServiceFacade) ApplicationContextProvider.getApplicationContext()
                    .getBean("statisticalResourcesServiceFacade");

            DimensionRepresentationMappingDto mapping = statisticalResourcesServiceFacade.retrieveDimensionRepresentationMappings(ServiceContextHolder.getCurrentServiceContext(), datasetVersionUrn,
                    filename);
            DimensionRepresentationMappingWebDto mappingExternalItem = dimensionRepresentationMappingDto2WebDto(mapping);

            message = serializeResourceJson(mappingExternalItem).toJSONString();
        }
        return message;
    }
    
    private void processQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    //
    // UTILITY METHODS
    //

    @SuppressWarnings("unchecked")
    private JSONObject serializeResourceJson(DimensionRepresentationMappingWebDto dimensionRepresentationMapping) {
        JSONObject obj = new JSONObject();
        if (dimensionRepresentationMapping != null) {
            obj.put(DimensionRepresentationMappingDS.FILENAME, dimensionRepresentationMapping.getDatasourceFilename());
            obj.put(DimensionRepresentationMappingDS.MAPPING, serializeMap(dimensionRepresentationMapping.getMapping()));
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    private JSONArray serializeMap(Map<String, ExternalItemDto> mapping) {
        JSONArray list = new JSONArray();
        for (Entry<String, ExternalItemDto> entry : mapping.entrySet()) {
            JSONObject representation = new JSONObject();
            representation.put(DimensionRepresentationMappingDS.DIMENSION_ID, entry.getKey());
            representation.put(DimensionRepresentationMappingDS.EXTERNAL_ITEM, serializeExternalItemDto(entry.getValue()));
            list.add(representation);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private JSONObject serializeExternalItemDto(ExternalItemDto externalItemDto) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(DimensionRepresentationMappingDS.EXTERNAL_ITEM_CODE, externalItemDto.getCode());
        jsonObject.put(DimensionRepresentationMappingDS.EXTERNAL_ITEM_URN, externalItemDto.getUrn());
        jsonObject.put(DimensionRepresentationMappingDS.EXTERNAL_ITEM_TITLE, serializeInternationalString(externalItemDto.getTitle()));
        return jsonObject;
    }

    @SuppressWarnings("unchecked")
    private JSONObject serializeInternationalString(InternationalStringDto title) {
        JSONObject obj = new JSONObject();
        for (LocalisedStringDto localised : title.getTexts()) {
            String clean = JSONObject.escape(localised.getLabel());
            obj.put(localised.getLocale(), clean);
        }
        return obj;
    }

    private void sendSuccessImportationResponse(HttpServletResponse response, String message) throws IOException {
        String action = "if (parent.uploadComplete) parent.uploadComplete('" + message + "');";
        sendResponse(response, action);
    }

    private void sendFailedImportationResponse(HttpServletResponse response, String errorMessage) throws IOException {
        String action = "if (parent.uploadFailed) parent.uploadFailed('" + errorMessage + "');";
        sendResponse(response, action);
    }

    private DimensionRepresentationMappingWebDto dimensionRepresentationMappingDto2WebDto(DimensionRepresentationMappingDto dto) throws MetamacWebException {
        if (dto == null) {
            return null;
        }
        DimensionRepresentationMappingWebDto webDto = new DimensionRepresentationMappingWebDto();
        webDto.setDatasourceFilename(dto.getDatasourceFilename());
        webDto.setMapping(buildRepresentationMap(dto.getMapping()));
        return webDto;
    }

    private Map<String, ExternalItemDto> buildRepresentationMap(Map<String, String> mappings) throws MetamacWebException {
        Map<String, ExternalItemDto> externalItemMappings = new HashMap<String, ExternalItemDto>();
        if (mappings != null && !mappings.isEmpty()) {
            List<String> urns = new ArrayList<String>(mappings.values());
            Map<String, ExternalItemDto> codelistMap = getCodelists(urns);
            for (String dimension : mappings.keySet()) {
                String codelistRepresentationUrn = mappings.get(dimension);
                externalItemMappings.put(dimension, codelistMap.get(codelistRepresentationUrn));
            }
        }
        return externalItemMappings;
    }

    private Map<String, ExternalItemDto> getCodelists(List<String> urns) throws MetamacWebException {

        SrmRestInternalFacadeImpl srmRestInternalFacade = (SrmRestInternalFacadeImpl) ApplicationContextProvider.getApplicationContext().getBean("srmRestInternalFacadeImpl");

        Map<String, ExternalItemDto> codelistMap = new HashMap<String, ExternalItemDto>(urns.size());

        SrmExternalResourceRestCriteria criteria = new SrmExternalResourceRestCriteria(CODELIST);
        criteria.setUrns(urns);

        ExternalItemsResult result = srmRestInternalFacade.findCodelists(criteria);
        for (ExternalItemDto externalItemDto : result.getExternalItemDtos()) {
            codelistMap.put(externalItemDto.getUrn(), externalItemDto);
        }

        return codelistMap;
    }
}
