package org.siemac.metamac.statistical.resources.core.publication.utils.structure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.MetamacExceptionItemBuilder;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.io.FileUtils;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The processor parses a TSV file with the structure of a publication. A publication structure can be defined in a TSV file in the following format:
 * 
 * <pre>
 * TYPE             RELATED_RESOURCE            
 * PUBLICATION                                  Publication title        
 * CHAPTER                                      Chapter level 01 - a        
 * CHAPTER                                          Chapter level 02 - a.a  
 * CUBE             C00031A_000001.QUERY                Table level 03 - a.a-a
 * CUBE             C00031A_000002.DATASET              Table level 03 - a.a-b
 * CUBE             C00031A_000003.QUERY                Table level 03 - a.a-c
 * CHAPTER                                          Chapter level 02 - a.b  
 * CUBE             C00031A_000004.DATASET              Table level 03 - a.b-a
 * CHAPTER                                          Chapter level 02 - a.c  
 * CUBE             C00031A_000005.DATASET              Table level 03 - a.c-a
 * CHAPTER                                      Chapter level 01 - b        
 * CHAPTER                                          Chapter level 02 - b.a  
 * CHAPTER                                          Chapter level 02 - b.b  
 * CHAPTER                                          Chapter level 02 - b.c  
 * CHAPTER                                      Chapter level 01 - c        
 * CUBE             C00031A_000006.QUERY            Table level 2    
 * CUBE             C00031A_000007.QUERY        Table level 1
 * </pre>
 */
@Component
public class PublicationStructureTSVProcessor {

    private static final Logger logger                      = LoggerFactory.getLogger(PublicationStructureTSVProcessor.class);

    private static final char   TSV_SEPARATOR               = '\t';
    private static final String SEPARATOR                   = ".";
    private static final String RELATED_RESOURCE_SEPARATOR  = "\\" + SEPARATOR;
    private static final String HEADER_TYPE                 = "TYPE";
    private static final String HEADER_RELATED_RESOURCE     = "RELATED_RESOURCE";
    private static final int    INDEX_TYPE                  = 0;
    private static final int    INDEX_RELATED_RESOURCE      = 1;
    private static final int    INDEX_PUBLICATION_NAME      = 2;
    private static final int    INDEX_INITIAL_ELEMENT_NAMES = 2;

    public PublicationStructure parse(File file) throws MetamacException {

        PublicationStructure publicationStructure = new PublicationStructure();
        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();
        BufferedReader lines = readFile(file);
        int lineNumber = 1;

        try {

            String line = lines.readLine();

            checkFileNotEmpty(lineNumber, line);
            checkHeader(lineNumber, line);
            lineNumber++;

            line = lines.readLine();
            readPublication(publicationStructure, lineNumber, line, exceptions);
            lineNumber++;

            while ((line = lines.readLine()) != null) {
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                readChaptersAndCubes(publicationStructure, lineNumber, line, exceptions);
                lineNumber++;
            }

        } catch (IOException e) {
            addFormatNotValidException(exceptions, lineNumber);
        }

        ExceptionUtils.throwIfException(exceptions);
        return publicationStructure;
    }

    private BufferedReader readFile(File file) throws MetamacException {
        try {
            InputStream inputStream = new FileInputStream(file);
            String charset = FileUtils.guessCharset(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
            return new BufferedReader(inputStreamReader);
        } catch (Exception e) {
            logger.error("An error has occurred reading file " + file.getName(), e);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_ERROR)
                    .withMessageParameters(file != null ? file.getName() : StringUtils.EMPTY).build();
        }
    }

    private void checkFileNotEmpty(int lineNumber, String line) throws MetamacException {
        if (StringUtils.isBlank(line)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_FORMAT_NOT_VALID).withMessageParameters(lineNumber).build();
        }
    }

    private void checkHeader(int lineNumber, String line) throws MetamacException {
        String[] headerColumns = splitLine(line);
        if (headerColumns.length < 2 || !HEADER_TYPE.equals(headerColumns[INDEX_TYPE]) || !HEADER_RELATED_RESOURCE.equals(headerColumns[INDEX_RELATED_RESOURCE])) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_HEADER_NOT_VALID).withMessageParameters(lineNumber).build();
        }
    }

    private void readPublication(PublicationStructure publicationStructure, int lineNumber, String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (StringUtils.isBlank(line)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_FORMAT_NOT_VALID).withMessageParameters(lineNumber).build();
        }
        try {
            String[] elements = splitLine(line);
            if (!isPublication(elements)) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_FORMAT_NOT_VALID).withMessageParameters(lineNumber).build();
            }
            publicationStructure.setPublicationTitle(elements[INDEX_PUBLICATION_NAME]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_FORMAT_NOT_VALID).withMessageParameters(lineNumber).build();
        }
    }

    private void readChaptersAndCubes(PublicationStructure publicationStructure, int lineNumber, String line, List<MetamacExceptionItem> exceptions) {
        String[] splitLine = splitLine(line);

        try {

            TypeRelatedResourceEnum type = getElementType(splitLine);
            if (type == null) {
                addFormatNotValidException(exceptions, lineNumber);
                return;
            }

            Element element = new Element();
            element.setType(type);
            element.setLineNumber(lineNumber);

            int position = readElementName(splitLine, element, lineNumber, exceptions);

            String relatedResource = splitLine[INDEX_RELATED_RESOURCE];
            if (isCube(type)) {
                parseRelatedResource(element, lineNumber, relatedResource, exceptions);
            } else if (StringUtils.isNotBlank(relatedResource)) {
                // A related resource code have been specified in a chapter. Only cubes have related resources.
                addException(exceptions, ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CHAPTER_WITH_RELATED_RESOURCE, lineNumber);
                return;
            }

            addElementoToPublication(publicationStructure, element, position, lineNumber, exceptions);
        } catch (ArrayIndexOutOfBoundsException e) {
            addFormatNotValidException(exceptions, lineNumber);
        }
    }

    private void addElementoToPublication(PublicationStructure publicationStructure, Element element, int position, int lineNumber, List<MetamacExceptionItem> exceptions) {
        if (position < INDEX_INITIAL_ELEMENT_NAMES) {
            return;
        }

        if (position == INDEX_INITIAL_ELEMENT_NAMES) {
            publicationStructure.addElement(element);
            return;
        }

        int depthLevel = position - INDEX_INITIAL_ELEMENT_NAMES;
        int parentDepthLevel = depthLevel - 1;

        LinkedList<Element> elementsInLevel = publicationStructure.getElements();

        Element elementParent = null;
        for (int i = 0; i < depthLevel; i++) {

            if (elementsInLevel.isEmpty()) {
                // If there is no elements in this level, the hierarchy of elements is not correctly specified in the file
                addFormatNotValidException(exceptions, lineNumber);
                return;
            }

            if (i == parentDepthLevel) {
                elementParent = elementsInLevel.getLast();
                break;
            }
            elementsInLevel = elementsInLevel.getLast().getElements();
        }
        if (isCube(elementParent.getType())) {
            addException(exceptions, ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CUBE_WITH_SUBELEMENTS, lineNumber);
            return;
        }
        elementParent.addElement(element);
    }

    private int readElementName(String[] splitLine, Element element, int lineNumber, List<MetamacExceptionItem> exceptions) {
        for (int i = INDEX_INITIAL_ELEMENT_NAMES; i < splitLine.length; i++) {
            if (StringUtils.isNotBlank(splitLine[i])) {
                element.setTitle(splitLine[i]);
                return i;
            }
        }
        addException(exceptions, ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_ELEMENT_WITH_EMTPY_NAME, lineNumber);
        return -1;
    }

    private void parseRelatedResource(Element element, int lineNumber, String relatedResource, List<MetamacExceptionItem> exceptions) {
        if (StringUtils.isBlank(relatedResource)) {
            exceptions.add(MetamacExceptionItemBuilder.metamacExceptionItem()
                    .withCommonServiceExceptionType(ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CUBE_WITHOUT_RELATED_RESOURCE).withMessageParameters(lineNumber).build());
            return;
        }
        String[] relatedResourceElements = relatedResource.split(RELATED_RESOURCE_SEPARATOR);

        if (isCubeUrlType(relatedResourceElements)) {
            addUrlRelatedResourceType(element, relatedResource);
            return;
        } else if (relatedResourceElements.length == 2) {
            element.setRelatedResourCode(relatedResourceElements[0]);
            StatisticalResourceTypeEnum type = getRelatedResourceType(relatedResourceElements[1]);
            if (type != null) {
                element.setRelatedResourceType(type);
                return;
            }
        }
        exceptions.add(MetamacExceptionItemBuilder.metamacExceptionItem()
                .withCommonServiceExceptionType(ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CUBE_WITH_WRONG_RELATED_RESOURCE).withMessageParameters(lineNumber).build());
    }

    private boolean isCubeUrlType(String[] relatedResourceElements) {
        if (relatedResourceElements.length >= 2) {
            StatisticalResourceTypeEnum type = getRelatedResourceType(relatedResourceElements[relatedResourceElements.length - 1]);
            return type != null && (StatisticalResourceTypeEnum.URL == type);
        }
        return false;
    }
    
    private void addUrlRelatedResourceType(Element element, String relatedResource) {
        element.setRelatedResourCode(readUrl(relatedResource));
        element.setRelatedResourceType(StatisticalResourceTypeEnum.URL);
    }

    private String readUrl(String relatedResource) {
        int lastSeparator = relatedResource.lastIndexOf(SEPARATOR);
        if (lastSeparator > 0) {
            return relatedResource.substring(0, lastSeparator);
        }
        return null;
    }
    
    private StatisticalResourceTypeEnum getRelatedResourceType(String type) {
        StatisticalResourceTypeEnum relatedResourceType = null;

        if (StatisticalResourceTypeEnum.DATASET.toString().equals(type)) {
            relatedResourceType = StatisticalResourceTypeEnum.DATASET;
        } else if (StatisticalResourceTypeEnum.QUERY.toString().equals(type)) {
            relatedResourceType = StatisticalResourceTypeEnum.QUERY;
        } else if (StatisticalResourceTypeEnum.MULTIDATASET.toString().equals(type)) {
            relatedResourceType = StatisticalResourceTypeEnum.MULTIDATASET;
        } else if (StatisticalResourceTypeEnum.URL.toString().equals(type)) {
            relatedResourceType = StatisticalResourceTypeEnum.URL;
        }

        return relatedResourceType;
    }

    private boolean isPublication(String[] elements) {
        return TypeRelatedResourceEnum.PUBLICATION.toString().equals(getLineType(elements));
    }

    private TypeRelatedResourceEnum getElementType(String[] elements) {
        if (TypeRelatedResourceEnum.CHAPTER.toString().equals(getLineType(elements))) {
            return TypeRelatedResourceEnum.CHAPTER;
        } else if (TypeRelatedResourceEnum.CUBE.toString().equals(getLineType(elements))) {
            return TypeRelatedResourceEnum.CUBE;
        }
        return null;
    }

    private boolean isCube(TypeRelatedResourceEnum type) {
        return TypeRelatedResourceEnum.CUBE.equals(type);
    }

    private String getLineType(String[] elements) {
        return elements[INDEX_TYPE];
    }

    private String[] splitLine(String line) {
        return line.split(Character.toString(TSV_SEPARATOR));
    }

    private void addException(List<MetamacExceptionItem> exceptions, CommonServiceExceptionType exceptionType, int lineNumber) {
        exceptions.add(MetamacExceptionItemBuilder.metamacExceptionItem().withCommonServiceExceptionType(exceptionType).withMessageParameters(lineNumber).build());
    }

    private void addFormatNotValidException(List<MetamacExceptionItem> exceptions, int lineNumber) {
        addException(exceptions, ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_FORMAT_NOT_VALID, lineNumber);
    }
}
