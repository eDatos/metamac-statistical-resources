package org.siemac.metamac.statistical_resources.rest.common.impl.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.enume.utils.IstacTimeGranularityCodeEnum;
import org.siemac.metamac.statistical.resources.core.enume.utils.IstacTimeUtils;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceBasicDto;
import es.gobcan.istac.edatos.dataset.repository.dto.GranularityAttributeInstanceDto;

/**
 * Resolves granularity attribute instances to concrete dimension key combinations.
 *
 * A granularity instance specifies attribute values for all temporal periods of a given
 * granularity (e.g. all annual periods, all quarterly periods) rather than for individual
 * period codes. This class expands those granularity specs into the concrete key combinations
 * used when building attribute data for the REST response.
 */
public class GranularityAttributeResolver {

    static final String KEY_DIMENSIONS_SEPARATOR = "#";

    private GranularityAttributeResolver() {
    }

    /**
     * Expands a list of granularity attribute instances into a map keyed by the
     * dimension-code combination string (dimensions joined by {@code #}).
     *
     * <p>For temporal dimensions the granularity codes in the instance (e.g. {@code ["A"]}
     * for yearly) are matched against the actual effective temporal codes; only the codes
     * whose granularity label matches are included. For non-temporal dimensions the specific
     * codes stored in the instance are used, falling back to all effective codes when none
     * are specified.
     *
     * <p>If no temporal codes match the requested granularity the instance is skipped entirely.
     * When two instances would produce the same key the first one wins (concrete instances
     * added before this call take precedence over granularity instances).
     *
     * @param attributeDimensionsOrdered    ordered list of dimension IDs for the attribute
     * @param dimensionsCodesSelectedEffective  effective codes per dimension in the current query
     * @param granularityInstances          granularity instances to expand
     * @return map from dimension-key to the matching instance (never null)
     * @throws MetamacException if a temporal code cannot be parsed
     */
    public static Map<String, AttributeInstanceBasicDto> buildGranularityAttributesByCodeDimensions(List<String> attributeDimensionsOrdered,
            Map<String, List<String>> dimensionsCodesSelectedEffective, List<GranularityAttributeInstanceDto> granularityInstances) throws MetamacException {
        Map<String, AttributeInstanceBasicDto> result = new HashMap<String, AttributeInstanceBasicDto>();
        for (GranularityAttributeInstanceDto instance : granularityInstances) {
            if (instance.getGranularityCodesByDimension() == null || instance.getGranularityCodesByDimension().isEmpty()) {
                continue;
            }
            List<List<String>> codesPerDimension = new ArrayList<List<String>>();
            boolean hasMatchingCodes = true;
            for (String dimId : attributeDimensionsOrdered) {
                if (instance.getGranularityCodesByDimension().containsKey(dimId)) {
                    // Temporal dimension: find temporal codes matching the specified granularities
                    List<String> granularityCodes = instance.getGranularityCodesByDimension().get(dimId);
                    List<String> allTemporalCodes = dimensionsCodesSelectedEffective.get(dimId);
                    List<String> matchingCodes = new ArrayList<String>();
                    if (allTemporalCodes != null) {
                        for (String temporalCode : allTemporalCodes) {
                            IstacTimeGranularityCodeEnum granularity = IstacTimeUtils.guessTimeGranularity(temporalCode);
                            if (granularity != null && granularityCodes.contains(granularity.getLabel())) {
                                matchingCodes.add(temporalCode);
                            }
                        }
                    }
                    if (matchingCodes.isEmpty()) {
                        hasMatchingCodes = false;
                        break;
                    }
                    codesPerDimension.add(matchingCodes);
                } else {
                    // Non-temporal dimension: use the specific codes stored in the instance
                    List<String> codes = instance.getCodesByDimension() != null ? instance.getCodesByDimension().get(dimId) : null;
                    if (codes == null || codes.isEmpty()) {
                        List<String> allCodes = dimensionsCodesSelectedEffective.get(dimId);
                        codesPerDimension.add(allCodes != null ? allCodes : new ArrayList<String>());
                    } else {
                        codesPerDimension.add(codes);
                    }
                }
            }
            if (!hasMatchingCodes) {
                continue;
            }
            addGranularityKeyCombinations(result, instance, codesPerDimension, 0, new ArrayList<String>());
        }
        return result;
    }

    static void addGranularityKeyCombinations(Map<String, AttributeInstanceBasicDto> result, GranularityAttributeInstanceDto instance,
            List<List<String>> codesPerDimension, int dimIndex, List<String> currentCodes) {
        if (dimIndex == codesPerDimension.size()) {
            String key = StringUtils.join(currentCodes, KEY_DIMENSIONS_SEPARATOR);
            if (!result.containsKey(key)) {
                result.put(key, instance);
            }
            return;
        }
        List<String> codes = codesPerDimension.get(dimIndex);
        for (String code : codes) {
            currentCodes.add(code);
            addGranularityKeyCombinations(result, instance, codesPerDimension, dimIndex + 1, currentCodes);
            currentCodes.remove(currentCodes.size() - 1);
        }
    }
}
