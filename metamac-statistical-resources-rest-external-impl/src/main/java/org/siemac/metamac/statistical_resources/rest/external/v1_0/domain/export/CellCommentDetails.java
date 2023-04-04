package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export;

import org.apache.commons.lang.StringUtils;

public class CellCommentDetails {

    private static final String NEW_LINE_CHARACTER = "\n";
    private static int          MAX_CELL_ANCHOR    = 15;

    private StringBuilder       valueBuilder       = null;
    private int                 numberOfLines      = 0;
    private int                 maxCharactersWidth = 0;

    public void addCommentLine(String value) {
        if (StringUtils.isNotBlank(value)) {
            if (valueBuilder == null) {
                valueBuilder = new StringBuilder();
            }
            valueBuilder.append(value.trim()).append(NEW_LINE_CHARACTER);
            numberOfLines++;
            if (maxCharactersWidth < value.length()) {
                maxCharactersWidth = value.length() + 1;
            }
        }
    }

    public String getValue() {
        if (valueBuilder != null && valueBuilder.length() > 0) {
            return valueBuilder.toString();
        } else {
            return null;
        }
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public int getMaxCharactersWidth() {
        return maxCharactersWidth;
    }

    public int calculateNumberOfColumnsToAccomodateComment() {
        int numberOfCells = maxCharactersWidth / 7 + 1;
        if (numberOfCells > MAX_CELL_ANCHOR) {
            return MAX_CELL_ANCHOR;
        } else {
            return numberOfCells;
        }
    }

    public int calculateNumberOfRowsToAccomodateComment() {
        if (numberOfLines > MAX_CELL_ANCHOR) {
            return MAX_CELL_ANCHOR;
        } else {
            return numberOfLines;
        }
    }

}
