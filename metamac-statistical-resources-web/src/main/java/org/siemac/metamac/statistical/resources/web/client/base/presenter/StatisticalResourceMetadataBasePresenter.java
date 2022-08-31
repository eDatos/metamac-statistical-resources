package org.siemac.metamac.statistical.resources.web.client.base.presenter;

import java.util.List;

import org.siemac.metamac.statistical.resources.web.client.base.utils.SiemacMetadataExternalField;
import org.siemac.metamac.statistical.resources.web.client.base.view.handlers.StatisticalResourceUiHandlers;
import org.siemac.metamac.statistical.resources.web.shared.external.GetCommonMetadataConfigurationsListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetCommonMetadataConfigurationsListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDataProviderPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDataProviderPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDataProviderSchemesPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDataProviderSchemesPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetLanguagesCodesAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetLanguagesCodesResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetOrganisationUnitSchemesPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetOrganisationUnitSchemesPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetOrganisationUnitsPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetOrganisationUnitsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationInstancesPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationInstancesPaginatedListResult;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;
import org.siemac.metamac.web.common.shared.criteria.CommonConfigurationRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;

public abstract class StatisticalResourceMetadataBasePresenter<V extends StatisticalResourceMetadataBasePresenter.StatisticalResourceMetadataBaseView, P extends Proxy<?>> extends Presenter<V, P>
        implements
            StatisticalResourceUiHandlers {

    protected DispatchAsync dispatcher;
    protected PlaceManager  placeManager;

    public StatisticalResourceMetadataBasePresenter(EventBus eventBus, V listView, P proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, listView, proxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
    }

    public interface StatisticalResourceMetadataBaseView extends View {

        void setCommonConfigurations(GetCommonMetadataConfigurationsListResult result);

        void setLanguagesCodes(GetLanguagesCodesResult result);

        void setStatisticalOperationInstances(GetStatisticalOperationInstancesPaginatedListResult result);

        void setOrganisationUnitSchemesForField(GetOrganisationUnitSchemesPaginatedListResult result, SiemacMetadataExternalField field);
        void setOrganisationUnitsForField(GetOrganisationUnitsPaginatedListResult result, SiemacMetadataExternalField field);
        
        // data provider
        void setDataProviderSchemesForField(GetDataProviderSchemesPaginatedListResult result, SiemacMetadataExternalField field);
        void setDataProviderForField(GetDataProviderPaginatedListResult result, SiemacMetadataExternalField field);
    }

    @Override
    public void retrieveOrganisationUnitSchemes(int firstResult, int maxResults, MetamacWebCriteria webCriteria, final SiemacMetadataExternalField field) {
        dispatcher.execute(new GetOrganisationUnitSchemesPaginatedListAction(firstResult, maxResults, webCriteria),
                new WaitingAsyncCallbackHandlingError<GetOrganisationUnitSchemesPaginatedListResult>(this) {

                    @Override
                    public void onWaitSuccess(GetOrganisationUnitSchemesPaginatedListResult result) {
                        getView().setOrganisationUnitSchemesForField(result, field);
                    }
                });
    }

    @Override
    public void retrieveOrganisationUnits(int firstResult, int maxResults, SrmItemRestCriteria webCriteria, final SiemacMetadataExternalField field) {
        dispatcher.execute(new GetOrganisationUnitsPaginatedListAction(firstResult, maxResults, webCriteria), new WaitingAsyncCallbackHandlingError<GetOrganisationUnitsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetOrganisationUnitsPaginatedListResult result) {
                getView().setOrganisationUnitsForField(result, field);
            }
        });
    }

    @Override
    public void retrieveDataProviderSchemes(int firstResult, int maxResults, MetamacWebCriteria webCriteria, final SiemacMetadataExternalField field) {
        dispatcher.execute(new GetDataProviderSchemesPaginatedListAction(firstResult, maxResults, webCriteria),
                new WaitingAsyncCallbackHandlingError<GetDataProviderSchemesPaginatedListResult>(this) {

                    @Override
                    public void onWaitSuccess(GetDataProviderSchemesPaginatedListResult result) {
                        getView().setDataProviderSchemesForField(result, field);
                    }
                });
    }

    @Override
    public void retrieveDataProviderUnits(int firstResult, int maxResults, SrmItemRestCriteria webCriteria, final SiemacMetadataExternalField field) {
        dispatcher.execute(new GetDataProviderPaginatedListAction(firstResult, maxResults, webCriteria), new WaitingAsyncCallbackHandlingError<GetDataProviderPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetDataProviderPaginatedListResult result) {
                getView().setDataProviderForField(result, field);
            }
        });
    }

    
    @Override
    public void retrieveCommonConfigurations(CommonConfigurationRestCriteria criteria) {
        dispatcher.execute(new GetCommonMetadataConfigurationsListAction(criteria), new WaitingAsyncCallbackHandlingError<GetCommonMetadataConfigurationsListResult>(this) {

            @Override
            public void onWaitSuccess(GetCommonMetadataConfigurationsListResult result) {
                getView().setCommonConfigurations(result);
            }
        });
    }

    @Override
    public void retrieveStatisticalOperationInstances(String statisticalOperationCode, int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
        dispatcher.execute(new GetStatisticalOperationInstancesPaginatedListAction(statisticalOperationCode, firstResult, maxResults, webCriteria),
                new WaitingAsyncCallbackHandlingError<GetStatisticalOperationInstancesPaginatedListResult>(this) {

                    @Override
                    public void onWaitSuccess(GetStatisticalOperationInstancesPaginatedListResult result) {
                        getView().setStatisticalOperationInstances(result);
                    }
                });
    }

    @Override
    public void retrieveLanguagesCodes(int firstResult, int maxResults, MetamacWebCriteria criteria) {
        dispatcher.execute(new GetLanguagesCodesAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetLanguagesCodesResult>(this) {

            @Override
            public void onWaitSuccess(GetLanguagesCodesResult result) {
                getView().setLanguagesCodes(result);
            }
        });

    }

    @Override
    public void goTo(List<PlaceRequest> location) {
        if (location != null && !location.isEmpty()) {
            placeManager.revealPlaceHierarchy(location);
        }
    }
}
