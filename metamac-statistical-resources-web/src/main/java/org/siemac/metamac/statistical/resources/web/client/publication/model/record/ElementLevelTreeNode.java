package org.siemac.metamac.statistical.resources.web.client.publication.model.record;

import org.siemac.metamac.statistical.resources.core.dto.publication.ElementLevelDto;
import org.siemac.metamac.statistical.resources.web.client.publication.model.ds.ElementLevelDS;
import org.siemac.metamac.web.common.client.widgets.NavigableTreeNode;

public class ElementLevelTreeNode extends NavigableTreeNode {

    public ElementLevelTreeNode() {
    }

    public void setUrn(String value) {
        setAttribute(ElementLevelDS.URN, value);
    }

    public String getUrn() {
        return getAttributeAsString(ElementLevelDS.URN);
    }

    @Override
    public void setTitle(String value) {
        setAttribute(ElementLevelDS.TITLE, value + "cristo_");
    }

    public void setDescription(String value) {
        setAttribute(ElementLevelDS.DESCRIPTION, value);
    }

    public void setOrderInLevel(Long value) {
        setAttribute(ElementLevelDS.ORDER_IN_LEVEL, value);
    }

    public void setParentChapterUrn(String value) {
        setAttribute(ElementLevelDS.PARENT_CHAPTER_URN, value);
    }

    public void setElementLevelDto(ElementLevelDto elementLevelDto) {
        setAttribute(ElementLevelDS.DTO, elementLevelDto);
    }

    public ElementLevelDto getElementLevelDto() {
        return (ElementLevelDto) getAttributeAsObject(ElementLevelDS.DTO);
    }
}
