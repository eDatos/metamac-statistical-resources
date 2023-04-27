package org.siemac.metamac.statistical.resources.web.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public interface GlobalResources extends ClientBundleWithLookup {

    public static final GlobalResources RESOURCE = GWT.create(GlobalResources.class);

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/info.png")
    ImageResource info();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/annotations.png")
    ImageResource annotations();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/add_annotation.png")
    ImageResource addAnnotation();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetnext.png")
    ImageResource resultSetNext();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetfirst.png")
    ImageResource resultSetFirst();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetlast.png")
    ImageResource resultSetLast();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetprevious.png")
    ImageResource resultSetPrevious();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetnext_Disabled.png")
    ImageResource resultSetNextDisabled();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetfirst_Disabled.png")
    ImageResource resultSetFirstDisabled();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetlast_Disabled.png")
    ImageResource resultSetLastDisabled();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetprevious_Disabled.png")
    ImageResource resultSetPreviousDisabled();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/validate_production.png")
    ImageResource validateProduction();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/validate_diffusion.png")
    ImageResource validateDiffusion();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/publish.png")
    ImageResource publish();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/reject.png")
    ImageResource reject();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/reload.png")
    ImageResource reload();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/version.png")
    ImageResource version();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/archive.png")
    ImageResource archive();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/pending_publication.png")
    ImageResource pendingPublication();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/tree_folder.png")
    ImageResource treeFolder();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/tree_file.png")
    ImageResource treeFile();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/preview.png")
    ImageResource preview();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/copy.png")
    ImageResource copy();
}
