/*
 JBILLING CONFIDENTIAL
 _____________________

 [2003] - [2012] Enterprise jBilling Software Ltd.
 All Rights Reserved.

 NOTICE:  All information contained herein is, and remains
 the property of Enterprise jBilling Software.
 The intellectual and technical concepts contained
 herein are proprietary to Enterprise jBilling Software
 and are protected by trade secret or copyright law.
 Dissemination of this information or reproduction of this material
 is strictly forbidden.
 */

package jbilling

import com.sapienter.jbilling.server.pricing.db.RateCardDTO
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.pricing.RateCardBL
import com.sapienter.jbilling.common.SessionInternalError
import com.sapienter.jbilling.client.ViewUtils
import grails.plugins.springsecurity.Secured

@Secured(["MENU_99"])
class RateCardController {

    static pagination = [ max: 10, offset: 0 ]

    ViewUtils viewUtils


    def index = {
        redirect action: list, params: params
    }

    def list = {
        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset

        def cards = RateCardDTO.createCriteria().list(
                max:    params.max,
                offset: params.offset
        ) {
            eq('company', new CompanyDTO(session['company_id']))
            order('id', 'desc')
        }

        def selected = params.id ? RateCardDTO.get(params.int("id")) : null

        if (params.applyFilter || params.partial) {
            render template: 'rateCards', model: [ cards: cards, selected: selected ]
        } else {
            render view: 'list', model: [ cards: cards, selected: selected ]
        }
    }

    def show = {
        def rateCard = RateCardDTO.get(params.int('id'))

        render template: 'show', model: [ selected: rateCard ]
    }

    def delete = {
        if (params.id) {
            new RateCardBL(params.int('id')).delete();

            flash.message = 'rate.card.deleted'
            flash.args = [ params.id ]
            log.debug("Deleted rate card ${params.id}.")
        }

        // re-render the list of rate cards
        redirect action: 'list', params: [ id: null, partial: true]
    }

    def edit = {
        def rateCard = params.id ? RateCardDTO.get(params.int('id')) : null

        if (params.id && rateCard == null) {
            flash.error = 'rate.card.not.found'
            flash.args = [ params.id as String ]

            redirect controller: 'rateCard', action: 'list'
            return
        }

        render template: 'edit', model: [ rateCard: rateCard ]
    }

    def save = {
        def rateCard = new RateCardDTO();
        bindData(rateCard, params)
        rateCard.company = new CompanyDTO(session['company_id'])

        // save uploaded file
        def rates = request.getFile("rates")
        def temp = null

        if (!rates.empty) {
            temp = File.createTempFile(rateCard.tableName, '.csv')
            rates.transferTo(temp)
            log.debug("rate card csv saved to: " + temp?.getAbsolutePath());
        }

        try {
            // save or update
            if (!rateCard.id) {
                rateCard.id = new RateCardBL().create(rateCard, temp)

                flash.message = 'rate.card.created'
                flash.args = [rateCard.id as String]

            } else {
                new RateCardBL(rateCard.id).update(rateCard, temp);

                flash.message = 'rate.card.updated'
                flash.args = [rateCard.id as String]
            }
        } catch (SessionInternalError e) {
            viewUtils.resolveException(flash, session.locale, e)
            chain action: 'list', model: [ selected: rateCard ]
            return

        } finally {
            temp?.delete()
        }

        chain action: 'list', params: [ id: rateCard.id ]
    }
}
