package org.siemac.metamac.statistical.resources.web.client.publication.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.NameableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.CubeDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.publication.model.ds.ElementLevelDS;
import org.siemac.metamac.statistical.resources.web.client.publication.utils.PublicationClientSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.publication.view.handlers.PublicationStructureTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.search.SearchSingleStatisticalRelatedResourcePaginatedWindow;
import org.siemac.metamac.statistical.resources.web.shared.criteria.StatisticalResourceWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetsResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.GetMultidatasetsResult;
import org.siemac.metamac.statistical.resources.web.shared.publication.GetPublicationsResult;
import org.siemac.metamac.statistical.resources.web.shared.query.GetQueriesResult;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomLinkItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageRichTextEditorItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchCustomLinkItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.core.client.Scheduler;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

public class PublicationStructureElementPanel extends VLayout {

    private InternationalMainFormLayout                           mainFormLayout;
    private GroupDynamicForm                                      form;
    private GroupDynamicForm                                      editionForm;

    private SearchSingleStatisticalRelatedResourcePaginatedWindow searchDatasetsWindow;
    private SearchSingleStatisticalRelatedResourcePaginatedWindow searchQueriesWindow;
    private SearchSingleStatisticalRelatedResourcePaginatedWindow searchMultidatasetWindow;
    private SearchSingleStatisticalRelatedResourcePaginatedWindow searchCollectionWindow;

    private PublicationVersionBaseDto                             publicationVersion;
    private NameableStatisticalResourceDto                        element;

    private PublicationStructureTabUiHandlers                     uiHandlers;

    public PublicationStructureElementPanel() {

        mainFormLayout = new InternationalMainFormLayout();
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        addMember(mainFormLayout);
    }

    private void bindMainFormLayoutEvents() {
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();

                form.setTranslationsShowed(translationsShowed);
                editionForm.setTranslationsShowed(translationsShowed);
            }
        });

        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (editionForm.validate(false)) {
                    // See: METAMAC-2516
                    // Two invokes to getXXXDto() is needed for Chrome, please don't remove this two call fix.
                    getSelectedElement();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                        @Override
                        public void execute() {
                            getUiHandlers().saveElement(publicationVersion.getUrn(), getSelectedElement());
                        }
                    });

                }
            }
        });

        mainFormLayout.getCancelToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // Hide panel if the creation of a new element is canceled
                if (element != null && element.getId() == null) {
                    hide();
                }
            }
        });
    }

    private void createViewForm() {
        form = new GroupDynamicForm(getConstants().publicationStructureElement());
        form.setNumCols(2);
        form.setColWidths("30%", "70%");

        ViewMultiLanguageTextItem title = new ViewMultiLanguageTextItem(ElementLevelDS.TITLE, getConstants().publicationStructureElementTitle());

        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(ElementLevelDS.DESCRIPTION, getConstants().publicationStructureElementDescription());

        ViewTextItem urn = new ViewTextItem(ElementLevelDS.URN, getConstants().publicationStructureElementURN());

        CustomLinkItem dataset = new CustomLinkItem(ElementLevelDS.DATASET, getConstants().dataset());
        dataset.setShowIfCondition(getIsNotEmptyFormItemIfFunction());
        dataset.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                String datasetUrn = editionForm.getValueAsString(ElementLevelDS.DATASET);
                getUiHandlers().goToLastVersion(datasetUrn);
            }
        });

        CustomLinkItem query = new CustomLinkItem(ElementLevelDS.QUERY, getConstants().query());
        query.setShowIfCondition(getIsNotEmptyFormItemIfFunction());
        query.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                String queryUrn = editionForm.getValueAsString(ElementLevelDS.QUERY);
                getUiHandlers().goToLastVersion(queryUrn);
            }
        });

        CustomLinkItem multidataset = new CustomLinkItem(ElementLevelDS.MULTIDATASET, getConstants().multidataset());
        multidataset.setShowIfCondition(getIsNotEmptyFormItemIfFunction());
        multidataset.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                String multidatasetUrn = editionForm.getValueAsString(ElementLevelDS.MULTIDATASET);
                getUiHandlers().goToLastVersion(multidatasetUrn);
            }
        });
        CustomLinkItem collection = createCollectionCustomLink();
        ViewTextItem url = new ViewTextItem(ElementLevelDS.URL, getConstants().url());
        url.setShowIfCondition(getIsNotEmptyFormItemIfFunction());
        
        form.setFields(title, description, urn, dataset, query, multidataset, collection, url);
        mainFormLayout.addViewCanvas(form);
    }

    private CustomLinkItem createCollectionCustomLink() {
        CustomLinkItem collection = new CustomLinkItem(ElementLevelDS.COLLECTION, getConstants().publication());
        collection.setShowIfCondition(getIsNotEmptyFormItemIfFunction());
        collection.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                String collectionUrn = editionForm.getValueAsString(ElementLevelDS.COLLECTION);
                getUiHandlers().goToLastVersion(collectionUrn);
            }
        });
        return collection;
    }
    private void createEditionForm() {
        editionForm = new GroupDynamicForm(getConstants().publicationStructureElement());
        editionForm.setNumCols(2);
        editionForm.setColWidths("30%", "70%");

        MultiLanguageTextItem title = new MultiLanguageTextItem(ElementLevelDS.TITLE, getConstants().publicationStructureElementTitle());
        title.setRequired(true);

        MultiLanguageRichTextEditorItem description = new MultiLanguageRichTextEditorItem(ElementLevelDS.DESCRIPTION, getConstants().publicationStructureElementDescription());

        ViewTextItem urn = new ViewTextItem(ElementLevelDS.URN, getConstants().publicationStructureElementURN());

        CustomSelectItem resourceTypeToLink = new CustomSelectItem(ElementLevelDS.RESOURCE_TYPE_TO_LINK, getConstants().publicationStructureElementResourceTypeToLink());
        resourceTypeToLink.setValueMap(CommonUtils.getStatisticalResourceTypeThatCanBeAddIntoACubeInPublicationHashMap());
        resourceTypeToLink.setRequired(true);
        resourceTypeToLink.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                editionForm.markForRedraw();
            }
        });
        resourceTypeToLink.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // Only show the the form containts a cube
                return element != null && element instanceof CubeDto;
            }
        });

        // Dataset

        SearchCustomLinkItem dataset = createSearchDatasetItem(ElementLevelDS.DATASET, getConstants().dataset());
        dataset.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                String datasetUrn = editionForm.getValueAsString(ElementLevelDS.DATASET);
                getUiHandlers().goToLastVersion(datasetUrn);
            }
        });
        dataset.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return StringUtils.equals(StatisticalResourceTypeEnum.DATASET.name(), editionForm.getValueAsString(ElementLevelDS.RESOURCE_TYPE_TO_LINK));
            }
        });

        // Query

        SearchCustomLinkItem query = createSearchQueryItem(ElementLevelDS.QUERY, getConstants().query());
        query.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                String queryUrn = editionForm.getValueAsString(ElementLevelDS.QUERY);
                getUiHandlers().goToLastVersion(queryUrn);
            }
        });
        query.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return StringUtils.equals(StatisticalResourceTypeEnum.QUERY.name(), editionForm.getValueAsString(ElementLevelDS.RESOURCE_TYPE_TO_LINK));
            }
        });

        // Multidataset

        SearchCustomLinkItem multidataset = createSearchMultidatasetItem(ElementLevelDS.MULTIDATASET, getConstants().multidataset());
        multidataset.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                String multidatasetUrn = editionForm.getValueAsString(ElementLevelDS.MULTIDATASET);
                getUiHandlers().goToLastVersion(multidatasetUrn);
            }
        });
        multidataset.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return StringUtils.equals(StatisticalResourceTypeEnum.MULTIDATASET.name(), editionForm.getValueAsString(ElementLevelDS.RESOURCE_TYPE_TO_LINK));
            }
        });

        //Collections
        SearchCustomLinkItem collection = setSearchCollectionItem();

        CustomTextItem url = new CustomTextItem(ElementLevelDS.URL, getConstants().url());
        url.setRequired(true);
        url.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return StringUtils.equals(StatisticalResourceTypeEnum.URL.name(), editionForm.getValueAsString(ElementLevelDS.RESOURCE_TYPE_TO_LINK));
            }
        });
        
        editionForm.setFields(title, description, urn, resourceTypeToLink, dataset, query, multidataset, collection, url);
        mainFormLayout.getEditionFormLayout().setHeight(300);
        mainFormLayout.addEditionCanvas(editionForm);
    }

    private SearchCustomLinkItem setSearchCollectionItem() {
        SearchCustomLinkItem collection = createSearchCollectionItem(ElementLevelDS.COLLECTION, getConstants().publication());
        collection.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                String collectionUrn = editionForm.getValueAsString(ElementLevelDS.COLLECTION);
                getUiHandlers().goToLastVersion(collectionUrn);
            }
        });
        collection.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return StringUtils.equals(StatisticalResourceTypeEnum.COLLECTION.name(), editionForm.getValueAsString(ElementLevelDS.RESOURCE_TYPE_TO_LINK));
            }
        });
        return collection;
    }

    public void setElement(NameableStatisticalResourceDto element) {
        this.element = element;

        mainFormLayout.setTitleLabelContents(InternationalStringUtils.getLocalisedString(element.getTitle()));
        mainFormLayout.setViewMode();

        setElementViewMode(element);

        if (element instanceof CubeDto) {
            mainFormLayout.setCanEdit(PublicationClientSecurityUtils.canUpdateCube(publicationVersion.getProcStatus()));
            setElementEditionMode(element);
        } else {
            mainFormLayout.setCanEdit(PublicationClientSecurityUtils.canUpdateChapter(publicationVersion.getProcStatus()));
            setElementEditionMode(element);
        }
    }

    private void setElementViewMode(NameableStatisticalResourceDto element) {

        form.setValue(ElementLevelDS.TITLE, element.getTitle());
        form.setValue(ElementLevelDS.DESCRIPTION, element.getDescription());
        form.setValue(ElementLevelDS.URN, element.getUrn());
        
        if (element instanceof CubeDto) {
            CubeDto cubeDto = (CubeDto) element;
            form.setValue(ElementLevelDS.DATASET, cubeDto.getDatasetUrn());
            form.setValue(ElementLevelDS.QUERY, cubeDto.getQueryUrn());
            form.setValue(ElementLevelDS.MULTIDATASET, cubeDto.getMultidatasetUrn());
            form.setValue(ElementLevelDS.COLLECTION, cubeDto.getCollectionUrn());
            form.setValue(ElementLevelDS.URL, cubeDto.getUrl());
        } else {
            form.setValue(ElementLevelDS.DATASET, StringUtils.EMPTY);
            form.setValue(ElementLevelDS.QUERY, StringUtils.EMPTY);
            form.setValue(ElementLevelDS.MULTIDATASET, StringUtils.EMPTY);
            form.setValue(ElementLevelDS.COLLECTION, StringUtils.EMPTY);
            form.setValue(ElementLevelDS.URL, StringUtils.EMPTY);
        }

        form.markForRedraw();
    }

    private void setElementEditionMode(NameableStatisticalResourceDto element) {

        editionForm.setValue(ElementLevelDS.TITLE, element.getTitle());
        editionForm.setValue(ElementLevelDS.DESCRIPTION, element.getDescription());
        editionForm.setValue(ElementLevelDS.URN, element.getUrn());
       
        if (element instanceof CubeDto) {
            CubeDto cubeDto = (CubeDto) element;

            String resourceTypeToLink = StringUtils.EMPTY;
            if (!StringUtils.isBlank(cubeDto.getDatasetUrn())) {
                resourceTypeToLink = StatisticalResourceTypeEnum.DATASET.name();
            } else if (!StringUtils.isBlank(cubeDto.getQueryUrn())) {
                resourceTypeToLink = StatisticalResourceTypeEnum.QUERY.name();
            } else if (!StringUtils.isBlank(cubeDto.getMultidatasetUrn())) {
                resourceTypeToLink = StatisticalResourceTypeEnum.MULTIDATASET.name(); 
            }else if (!StringUtils.isBlank(cubeDto.getCollectionUrn())) {
                resourceTypeToLink = StatisticalResourceTypeEnum.COLLECTION.name();
            } else if (!StringUtils.isBlank(cubeDto.getUrl())) {
                resourceTypeToLink = StatisticalResourceTypeEnum.URL.name();
            }
            editionForm.setValue(ElementLevelDS.RESOURCE_TYPE_TO_LINK, resourceTypeToLink);
            setDatasetInEditionForm(cubeDto.getDatasetUrn());
            setQueryInEditionForm(cubeDto.getQueryUrn());
            setMultidatasetInEditionForm(cubeDto.getMultidatasetUrn());
            setCollectionInEditionForm(cubeDto.getCollectionUrn());
            setUrlInEditionForm(cubeDto.getUrl());
        } else {
            editionForm.setValue(ElementLevelDS.RESOURCE_TYPE_TO_LINK, StringUtils.EMPTY);
            ((CustomLinkItem) editionForm.getItem(ElementLevelDS.DATASET)).clearValue();
            ((CustomLinkItem) editionForm.getItem(ElementLevelDS.QUERY)).clearValue();
            ((CustomLinkItem) editionForm.getItem(ElementLevelDS.MULTIDATASET)).clearValue();
            ((CustomLinkItem) editionForm.getItem(ElementLevelDS.COLLECTION)).clearValue();
            ((CustomTextItem) editionForm.getItem(ElementLevelDS.URL)).clearValue();
        }

        editionForm.markForRedraw();

        if (element.getId() == null) {
            editionForm.clearErrors(true);
        } else {
            editionForm.validate(false);
        }
    }

    private void setDatasetInEditionForm(String datasetUrn) {
        ((CustomLinkItem) editionForm.getItem(ElementLevelDS.DATASET)).setValue(datasetUrn, null);
    }

    private void setCollectionInEditionForm(String datasetUrn) {
        ((CustomLinkItem) editionForm.getItem(ElementLevelDS.COLLECTION)).setValue(datasetUrn, null);
    }

    private void setQueryInEditionForm(String queryUrn) {
        ((CustomLinkItem) editionForm.getItem(ElementLevelDS.QUERY)).setValue(queryUrn, null);
    }

    private void setMultidatasetInEditionForm(String queryUrn) {
        ((CustomLinkItem) editionForm.getItem(ElementLevelDS.MULTIDATASET)).setValue(queryUrn, null);
    }

    private void setUrlInEditionForm(String url) {
        ((CustomTextItem) editionForm.getItem(ElementLevelDS.URL)).setValue(url);
    }
    
    public NameableStatisticalResourceDto getSelectedElement() {

        element.setTitle(editionForm.getValueAsInternationalStringDto(ElementLevelDS.TITLE));
        element.setDescription(editionForm.getValueAsInternationalStringDto(ElementLevelDS.DESCRIPTION));

        if (element instanceof CubeDto) {
            if (StatisticalResourceTypeEnum.DATASET.name().equals(editionForm.getValueAsString(ElementLevelDS.RESOURCE_TYPE_TO_LINK))) {
                ((CubeDto) element).setDatasetUrn(editionForm.getValueAsString(ElementLevelDS.DATASET));
                ((CubeDto) element).setQueryUrn(null);
                ((CubeDto) element).setMultidatasetUrn(null);
                ((CubeDto) element).setCollectionUrn(null);
                ((CubeDto) element).setUrl(null);
            } else if (StatisticalResourceTypeEnum.QUERY.name().equals(editionForm.getValueAsString(ElementLevelDS.RESOURCE_TYPE_TO_LINK))) {
                ((CubeDto) element).setQueryUrn(editionForm.getValueAsString(ElementLevelDS.QUERY));
                ((CubeDto) element).setDatasetUrn(null);
                ((CubeDto) element).setMultidatasetUrn(null);
                ((CubeDto) element).setCollectionUrn(null);
                ((CubeDto) element).setUrl(null);
            } else if (StatisticalResourceTypeEnum.MULTIDATASET.name().equals(editionForm.getValueAsString(ElementLevelDS.RESOURCE_TYPE_TO_LINK))) {
                ((CubeDto) element).setMultidatasetUrn(editionForm.getValueAsString(ElementLevelDS.MULTIDATASET));
                ((CubeDto) element).setDatasetUrn(null);
                ((CubeDto) element).setQueryUrn(null);
                ((CubeDto) element).setCollectionUrn(null);
                ((CubeDto) element).setUrl(null); 
            }else if (StatisticalResourceTypeEnum.COLLECTION.name().equals(editionForm.getValueAsString(ElementLevelDS.RESOURCE_TYPE_TO_LINK))) {
                ((CubeDto) element).setCollectionUrn(editionForm.getValueAsString(ElementLevelDS.COLLECTION));
                ((CubeDto) element).setMultidatasetUrn(null);
                ((CubeDto) element).setDatasetUrn(null);
                ((CubeDto) element).setQueryUrn(null);
                ((CubeDto) element).setUrl(null);
            } else if (StatisticalResourceTypeEnum.URL.name().equals(editionForm.getValueAsString(ElementLevelDS.RESOURCE_TYPE_TO_LINK))) {
                ((CubeDto) element).setUrl(editionForm.getValueAsString(ElementLevelDS.URL));
                ((CubeDto) element).setMultidatasetUrn(null);
                ((CubeDto) element).setDatasetUrn(null);
                ((CubeDto) element).setCollectionUrn(null);
                ((CubeDto) element).setQueryUrn(null);   
            }
        }

        return element;
    }

    public void setPublicationVersion(PublicationVersionBaseDto publicationVersion) {
        this.publicationVersion = publicationVersion;
    }

    public PublicationStructureTabUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void setUiHandlers(PublicationStructureTabUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    private FormItemIfFunction getIsNotEmptyFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return value != null && !StringUtils.isBlank(value.toString());
            }
        };
    }

    public void setMainFormLayoutEditionMode() {
        mainFormLayout.setEditionMode();
    }

    //
    // FORM ITEMS
    //

    private SearchCustomLinkItem createSearchDatasetItem(String name, String title) {

        final SearchCustomLinkItem datasetItem = new SearchCustomLinkItem(name, title);
        datasetItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchDatasetsWindow = new SearchSingleStatisticalRelatedResourcePaginatedWindow(getConstants().resourceSelection(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS,
                        new SearchPaginatedAction<StatisticalResourceWebCriteria>() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults, StatisticalResourceWebCriteria criteria) {
                                getUiHandlers().retrieveDatasetsForCubes(firstResult, maxResults, criteria);
                            }
                        });

                // Load statistical operations to filter datasets
                getUiHandlers().retrieveStatisticalOperationsForDatasetSelection();

                searchDatasetsWindow.setSaveAction(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        RelatedResourceDto selectedResource = searchDatasetsWindow.getSelectedResource();
                        searchDatasetsWindow.markForDestroy();
                        // Set selected resource in form
                        setDatasetInEditionForm(selectedResource != null ? selectedResource.getUrn() : StringUtils.EMPTY);
                        editionForm.validate(false);
                    }
                });
            }
        });
        datasetItem.setRequired(true);
        return datasetItem;
    }

    private SearchCustomLinkItem createSearchQueryItem(String name, String title) {

        final SearchCustomLinkItem queryItem = new SearchCustomLinkItem(name, title);
        queryItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchQueriesWindow = new SearchSingleStatisticalRelatedResourcePaginatedWindow(getConstants().resourceSelection(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS,
                        new SearchPaginatedAction<StatisticalResourceWebCriteria>() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults, StatisticalResourceWebCriteria criteria) {
                                getUiHandlers().retrieveQueriesForCubes(firstResult, maxResults, criteria);
                            }
                        });

                // Load statistical operations to filter queries
                getUiHandlers().retrieveStatisticalOperationsForQuerySelection();

                searchQueriesWindow.setSaveAction(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        RelatedResourceDto selectedResource = searchQueriesWindow.getSelectedResource();
                        searchQueriesWindow.markForDestroy();
                        // Set selected resource in form
                        setQueryInEditionForm(selectedResource != null ? selectedResource.getUrn() : StringUtils.EMPTY);
                        editionForm.validate(false);
                    }
                });
            }
        });
        queryItem.setRequired(true);
        return queryItem;
    }

    private SearchCustomLinkItem createSearchCollectionItem(String name, String title) {

        final SearchCustomLinkItem collectionItem = new SearchCustomLinkItem(name, title);
        collectionItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchCollectionWindow = new SearchSingleStatisticalRelatedResourcePaginatedWindow(getConstants().resourceSelection(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS,
                        new SearchPaginatedAction<StatisticalResourceWebCriteria>() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults, StatisticalResourceWebCriteria criteria) {
                                getUiHandlers().retrieveCollectionsForCubes(firstResult, maxResults, criteria);
                            }
                        });

                // Load statistical operations to filter queries
                getUiHandlers().retrieveStatisticalOperationsForCollectionSelection();

                searchCollectionWindow.setSaveAction(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        RelatedResourceDto selectedResource = searchCollectionWindow.getSelectedResource();
                        searchCollectionWindow.markForDestroy();
                        // Set selected resource in form
                        setCollectionInEditionForm(selectedResource != null ? selectedResource.getUrn() : StringUtils.EMPTY);
                        editionForm.validate(false);
                    }
                });
            }
        });
        collectionItem.setRequired(true);
        return collectionItem;
    }

    private SearchCustomLinkItem createSearchMultidatasetItem(String name, String title) {

        final SearchCustomLinkItem multidatasetItem = new SearchCustomLinkItem(name, title);
        multidatasetItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchMultidatasetWindow = new SearchSingleStatisticalRelatedResourcePaginatedWindow(getConstants().resourceSelection(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS,
                        new SearchPaginatedAction<StatisticalResourceWebCriteria>() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults, StatisticalResourceWebCriteria criteria) {
                                getUiHandlers().retrieveMultidatasetsForCubes(firstResult, maxResults, criteria);
                            }
                        });

                // Load statistical operations to filter queries
                getUiHandlers().retrieveStatisticalOperationsForMultidatasetSelection();

                searchMultidatasetWindow.setSaveAction(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        RelatedResourceDto selectedResource = searchMultidatasetWindow.getSelectedResource();
                        searchMultidatasetWindow.markForDestroy();
                        // Set selected resource in form
                        setMultidatasetInEditionForm(selectedResource != null ? selectedResource.getUrn() : StringUtils.EMPTY);
                        editionForm.validate(false);
                    }
                });
            }
        });
        multidatasetItem.setRequired(true);
        return multidatasetItem;
    }

    //
    // RELATED RESOURCES
    //

    public void setStatisticalOperationsForDatasetSelection(GetStatisticalOperationsPaginatedListResult result) {
        if (searchDatasetsWindow != null) {
            searchDatasetsWindow.setStatisticalOperations(result.getOperationsList());
            getUiHandlers().retrieveDatasetsForCubes(0, StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS, searchDatasetsWindow.getSearchCriteria());
        }
    }

    public void setDatasetsForCubes(GetDatasetsResult result) {
        if (searchDatasetsWindow != null) {
            searchDatasetsWindow.setResources(result.getDatasets());
            searchDatasetsWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getDatasets().size(), result.getTotalResults());
        }
    }

    public void setStatisticalOperationsForQuerySelection(GetStatisticalOperationsPaginatedListResult result) {
        if (searchQueriesWindow != null) {
            searchQueriesWindow.setStatisticalOperations(result.getOperationsList());
            getUiHandlers().retrieveQueriesForCubes(0, StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS, searchQueriesWindow.getSearchCriteria());
        }
    }

    public void setStatisticalOperationsForCollectionSelection(GetStatisticalOperationsPaginatedListResult result) {
        if (searchCollectionWindow != null) {
            searchCollectionWindow.setStatisticalOperations(result.getOperationsList());
            getUiHandlers().retrieveCollectionsForCubes(0, StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS, searchCollectionWindow.getSearchCriteria());
        }
    }

    public void setQueriesForCubes(GetQueriesResult result) {
        if (searchQueriesWindow != null) {
            searchQueriesWindow.setResources(result.getQueries());
            searchQueriesWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getQueries().size(), result.getTotalResults());
        }
    }

    public void setStatisticalOperationsForMultidatasetSelection(GetStatisticalOperationsPaginatedListResult result) {
        if (searchMultidatasetWindow != null) {
            searchMultidatasetWindow.setStatisticalOperations(result.getOperationsList());
            getUiHandlers().retrieveMultidatasetsForCubes(0, StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS, searchMultidatasetWindow.getSearchCriteria());
        }
    }

    public void setMultidatasetsForCubes(GetMultidatasetsResult result) {
        if (searchMultidatasetWindow != null) {
            searchMultidatasetWindow.setResources(result.getMultidatasets());
            searchMultidatasetWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getMultidatasets().size(), result.getTotalResults());
        }
    }

    public void setCollectionsForCubes(GetPublicationsResult result) {
        if (searchCollectionWindow != null) {
            searchCollectionWindow.setResources(result.getCollections());
            searchCollectionWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getCollections().size(), result.getTotalResults());
        }
    }
}
