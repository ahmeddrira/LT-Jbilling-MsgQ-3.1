/*
 * JBILLING CONFIDENTIAL
 * _____________________
 *
 * [2003] - [2012] Enterprise jBilling Software Ltd.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Enterprise jBilling Software.
 * The intellectual and technical concepts contained
 * herein are proprietary to Enterprise jBilling Software
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden.
 */

package com.sapienter.jbilling.server.pricing.cache;

/**
 * MatchType specifies the logic used by {@link RateCardFinder} when determining if a pricing
 * field from mediation matches the 'match' column of the rating table when looking for a price.
 *
 * @author Brian Cowdery
 * @since 19-02-2012
 */
public enum MatchType {

    /** Returns prices from the rate table only when the value EXACTLY matches */
    EXACT,

    /** Searches for a price using the search value as a prefix, trying to find the smallest possible prefix match */
    SMALLEST_PREFIX,

    /** Searches for a price using the search value as a prefix, trying to find the largest, most specific, match */
    LARGEST_PREFIX,
}
