


  
    <fieldset class="required">
    <legend><g:message code="hcar.property.socialSecurity.label" /></legend>
    
      <label for="socialSecurityMemberShipKind" class="required"><g:message code="hcar.property.socialSecurityMemberShipKind.label" /> *  <span><g:message code="hcar.property.socialSecurityMemberShipKind.help" /></span></label>
            <select id="socialSecurityMemberShipKind" name="socialSecurityMemberShipKind" class="required condition-isSocialSecurityMemberShip-trigger  validate-not-first ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('socialSecurityMemberShipKind') ? 'validation-failed' : ''}" title="<g:message code="hcar.property.socialSecurityMemberShipKind.validationError" />">
              <option value=""><g:message code="message.select.defaultOption" /></option>
              <g:each in="${['INSURED','CLAIMANT','NO_MEMBER_SHIP']}">
                <option value="${it}" ${it == rqt.socialSecurityMemberShipKind?.toString() ? 'selected="selected"': ''}><g:libredematEnumToText var="${it}" i18nKeyPrefix="hcar.property.socialSecurityMemberShipKind" /></option>
              </g:each>
            </select>
            

    
      <label for="socialSecurityNumber" class="required condition-isSocialSecurityMemberShip-filled"><g:message code="hcar.property.socialSecurityNumber.label" /> *  <span><g:message code="hcar.property.socialSecurityNumber.help" /></span></label>
            <input type="text" id="socialSecurityNumber" name="socialSecurityNumber" value="${rqt.socialSecurityNumber?.toString()}" 
                    class="required condition-isSocialSecurityMemberShip-filled   ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('socialSecurityNumber') ? 'validation-failed' : ''}" title="<g:message code="hcar.property.socialSecurityNumber.validationError" />"   />
            

    
      <label for="socialSecurityAgencyName" class="required condition-isSocialSecurityMemberShip-filled"><g:message code="hcar.property.socialSecurityAgencyName.label" /> *  <span><g:message code="hcar.property.socialSecurityAgencyName.help" /></span></label>
            <input type="text" id="socialSecurityAgencyName" name="socialSecurityAgencyName" value="${rqt.socialSecurityAgencyName?.toString()}" 
                    class="required condition-isSocialSecurityMemberShip-filled   ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('socialSecurityAgencyName') ? 'validation-failed' : ''}" title="<g:message code="hcar.property.socialSecurityAgencyName.validationError" />"  maxlength="50" />
            

    
      <label class="required condition-isSocialSecurityMemberShip-filled"><g:message code="hcar.property.socialSecurityAgencyAddress.label" /> *  <span><g:message code="hcar.property.socialSecurityAgencyAddress.help" /></span></label>
            <div id="socialSecurityAgencyAddress" class="address required condition-isSocialSecurityMemberShip-filled  ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('socialSecurityAgencyAddress') ? 'validation-failed' : ''}">
            <label for="socialSecurityAgencyAddress.additionalDeliveryInformation"><g:message code="address.property.additionalDeliveryInformation" /></label>
            <input type="text" class="validate-addressLine38 ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('socialSecurityAgencyAddress.additionalDeliveryInformation') ? 'validation-failed' : ''}" value="${rqt.socialSecurityAgencyAddress?.additionalDeliveryInformation}" maxlength="38" id="socialSecurityAgencyAddress.additionalDeliveryInformation" name="socialSecurityAgencyAddress.additionalDeliveryInformation" />  
            <label for="socialSecurityAgencyAddress.additionalGeographicalInformation"><g:message code="address.property.additionalGeographicalInformation" /></label>
            <input type="text" class="validate-addressLine38 ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('socialSecurityAgencyAddress.additionalGeographicalInformation') ? 'validation-failed' : ''}" value="${rqt.socialSecurityAgencyAddress?.additionalGeographicalInformation}" maxlength="38" id="socialSecurityAgencyAddress.additionalGeographicalInformation" name="socialSecurityAgencyAddress.additionalGeographicalInformation" />
            <label for="socialSecurityAgencyAddress_streetNumber"><g:message code="address.property.streetNumber" /></label> - 
            <label for="socialSecurityAgencyAddress_streetName" class="required"><g:message code="address.property.streetName" /> *</label><br />
            <input type="text" class="line1 validate-streetNumber ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('socialSecurityAgencyAddress.streetNumber') ? 'validation-failed' : ''}" value="${rqt.socialSecurityAgencyAddress?.streetNumber}" size="5" maxlength="5" id="socialSecurityAgencyAddress_streetNumber" name="socialSecurityAgencyAddress.streetNumber" />
            <input type="text" class="line2 required validate-streetName ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('socialSecurityAgencyAddress.streetName') ? 'validation-failed' : ''}" value="${rqt.socialSecurityAgencyAddress?.streetName}" maxlength="32" id="socialSecurityAgencyAddress_streetName" name="socialSecurityAgencyAddress.streetName" title="<g:message code="address.property.streetName.validationError" />" />
            <input type="hidden" value="${rqt.socialSecurityAgencyAddress?.streetMatriculation}" id="socialSecurityAgencyAddress_streetMatriculation" name="socialSecurityAgencyAddress.streetMatriculation" />
            <input type="hidden" value="${rqt.socialSecurityAgencyAddress?.streetRivoliCode}" id="socialSecurityAgencyAddress_streetRivoliCode" name="socialSecurityAgencyAddress.streetRivoliCode" />
            <label for="socialSecurityAgencyAddress.placeNameOrService"><g:message code="address.property.placeNameOrService" /></label>
            <input type="text" class="validate-addressLine38 ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('socialSecurityAgencyAddress.placeNameOrService') ? 'validation-failed' : ''}" value="${rqt.socialSecurityAgencyAddress?.placeNameOrService}" maxlength="38" id="socialSecurityAgencyAddress.placeNameOrService" name="socialSecurityAgencyAddress.placeNameOrService" />
            <label for="socialSecurityAgencyAddress_postalCode" class="required"><g:message code="address.property.postalCode" /> * </label> - 
            <label for="socialSecurityAgencyAddress_city" class="required"><g:message code="address.property.city" /> *</label><br />
            <input type="text" class="line1 required validate-postalCode ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('socialSecurityAgencyAddress.postalCode') ? 'validation-failed' : ''}" value="${rqt.socialSecurityAgencyAddress?.postalCode}" size="5" maxlength="5" id="socialSecurityAgencyAddress_postalCode" name="socialSecurityAgencyAddress.postalCode" title="<g:message code="address.property.postalCode.validationError" />" />
            <input type="text" class="line2 required validate-city ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('socialSecurityAgencyAddress.city') ? 'validation-failed' : ''}" value="${rqt.socialSecurityAgencyAddress?.city}" maxlength="32" id="socialSecurityAgencyAddress_city" name="socialSecurityAgencyAddress.city" title="<g:message code="address.property.city.validationError" />" />
            <input type="hidden" value="${rqt.socialSecurityAgencyAddress?.cityInseeCode}" id="socialSecurityAgencyAddress_cityInseeCode" name="socialSecurityAgencyAddress.cityInseeCode" />
            <label for="socialSecurityAgencyAddress.countryName"><g:message code="address.property.countryName" /></label>
            <input type="text" class="validate-addressLine38 ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('socialSecurityAgencyAddress.countryName') ? 'validation-failed' : ''}" value="${rqt.socialSecurityAgencyAddress?.countryName}" maxlength="38" id="socialSecurityAgencyAddress.countryName" name="socialSecurityAgencyAddress.countryName" />
            </div>
            

    
    </fieldset>
  

  
    <fieldset class="required">
    <legend><g:message code="hcar.property.paymentAgency.label" /></legend>
    
      <label for="paymentAgencyBeneficiary" class="required"><g:message code="hcar.property.paymentAgencyBeneficiary.label" /> *  <span><g:message code="hcar.property.paymentAgencyBeneficiary.help" /></span></label>
            <select id="paymentAgencyBeneficiary" name="paymentAgencyBeneficiary" class="required condition-isPaymentAgencyBeneficiary-trigger  validate-not-first ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('paymentAgencyBeneficiary') ? 'validation-failed' : ''}" title="<g:message code="hcar.property.paymentAgencyBeneficiary.validationError" />">
              <option value=""><g:message code="message.select.defaultOption" /></option>
              <g:each in="${['C_A_F','M_S_A','OTHER','NO_MEMBER_SHIP']}">
                <option value="${it}" ${it == rqt.paymentAgencyBeneficiary?.toString() ? 'selected="selected"': ''}><g:libredematEnumToText var="${it}" i18nKeyPrefix="hcar.property.paymentAgencyBeneficiary" /></option>
              </g:each>
            </select>
            

    
      <label for="paymentAgencyBeneficiaryNumber" class="required condition-isPaymentAgencyBeneficiary-filled"><g:message code="hcar.property.paymentAgencyBeneficiaryNumber.label" /> *  <span><g:message code="hcar.property.paymentAgencyBeneficiaryNumber.help" /></span></label>
            <input type="text" id="paymentAgencyBeneficiaryNumber" name="paymentAgencyBeneficiaryNumber" value="${rqt.paymentAgencyBeneficiaryNumber?.toString()}" 
                    class="required condition-isPaymentAgencyBeneficiary-filled   ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('paymentAgencyBeneficiaryNumber') ? 'validation-failed' : ''}" title="<g:message code="hcar.property.paymentAgencyBeneficiaryNumber.validationError" />"  maxlength="20" />
            

    
      <label for="paymentAgencyName" class="required condition-isPaymentAgencyBeneficiary-filled"><g:message code="hcar.property.paymentAgencyName.label" /> *  <span><g:message code="hcar.property.paymentAgencyName.help" /></span></label>
            <input type="text" id="paymentAgencyName" name="paymentAgencyName" value="${rqt.paymentAgencyName?.toString()}" 
                    class="required condition-isPaymentAgencyBeneficiary-filled   ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('paymentAgencyName') ? 'validation-failed' : ''}" title="<g:message code="hcar.property.paymentAgencyName.validationError" />"  maxlength="50" />
            

    
      <label class="required condition-isPaymentAgencyBeneficiary-filled"><g:message code="hcar.property.paymentAgencyAddress.label" /> *  <span><g:message code="hcar.property.paymentAgencyAddress.help" /></span></label>
            <div id="paymentAgencyAddress" class="address required condition-isPaymentAgencyBeneficiary-filled  ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('paymentAgencyAddress') ? 'validation-failed' : ''}">
            <label for="paymentAgencyAddress.additionalDeliveryInformation"><g:message code="address.property.additionalDeliveryInformation" /></label>
            <input type="text" class="validate-addressLine38 ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('paymentAgencyAddress.additionalDeliveryInformation') ? 'validation-failed' : ''}" value="${rqt.paymentAgencyAddress?.additionalDeliveryInformation}" maxlength="38" id="paymentAgencyAddress.additionalDeliveryInformation" name="paymentAgencyAddress.additionalDeliveryInformation" />  
            <label for="paymentAgencyAddress.additionalGeographicalInformation"><g:message code="address.property.additionalGeographicalInformation" /></label>
            <input type="text" class="validate-addressLine38 ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('paymentAgencyAddress.additionalGeographicalInformation') ? 'validation-failed' : ''}" value="${rqt.paymentAgencyAddress?.additionalGeographicalInformation}" maxlength="38" id="paymentAgencyAddress.additionalGeographicalInformation" name="paymentAgencyAddress.additionalGeographicalInformation" />
            <label for="paymentAgencyAddress_streetNumber"><g:message code="address.property.streetNumber" /></label> - 
            <label for="paymentAgencyAddress_streetName" class="required"><g:message code="address.property.streetName" /> *</label><br />
            <input type="text" class="line1 validate-streetNumber ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('paymentAgencyAddress.streetNumber') ? 'validation-failed' : ''}" value="${rqt.paymentAgencyAddress?.streetNumber}" size="5" maxlength="5" id="paymentAgencyAddress_streetNumber" name="paymentAgencyAddress.streetNumber" />
            <input type="text" class="line2 required validate-streetName ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('paymentAgencyAddress.streetName') ? 'validation-failed' : ''}" value="${rqt.paymentAgencyAddress?.streetName}" maxlength="32" id="paymentAgencyAddress_streetName" name="paymentAgencyAddress.streetName" title="<g:message code="address.property.streetName.validationError" />" />
            <input type="hidden" value="${rqt.paymentAgencyAddress?.streetMatriculation}" id="paymentAgencyAddress_streetMatriculation" name="paymentAgencyAddress.streetMatriculation" />
            <input type="hidden" value="${rqt.paymentAgencyAddress?.streetRivoliCode}" id="paymentAgencyAddress_streetRivoliCode" name="paymentAgencyAddress.streetRivoliCode" />
            <label for="paymentAgencyAddress.placeNameOrService"><g:message code="address.property.placeNameOrService" /></label>
            <input type="text" class="validate-addressLine38 ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('paymentAgencyAddress.placeNameOrService') ? 'validation-failed' : ''}" value="${rqt.paymentAgencyAddress?.placeNameOrService}" maxlength="38" id="paymentAgencyAddress.placeNameOrService" name="paymentAgencyAddress.placeNameOrService" />
            <label for="paymentAgencyAddress_postalCode" class="required"><g:message code="address.property.postalCode" /> * </label> - 
            <label for="paymentAgencyAddress_city" class="required"><g:message code="address.property.city" /> *</label><br />
            <input type="text" class="line1 required validate-postalCode ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('paymentAgencyAddress.postalCode') ? 'validation-failed' : ''}" value="${rqt.paymentAgencyAddress?.postalCode}" size="5" maxlength="5" id="paymentAgencyAddress_postalCode" name="paymentAgencyAddress.postalCode" title="<g:message code="address.property.postalCode.validationError" />" />
            <input type="text" class="line2 required validate-city ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('paymentAgencyAddress.city') ? 'validation-failed' : ''}" value="${rqt.paymentAgencyAddress?.city}" maxlength="32" id="paymentAgencyAddress_city" name="paymentAgencyAddress.city" title="<g:message code="address.property.city.validationError" />" />
            <input type="hidden" value="${rqt.paymentAgencyAddress?.cityInseeCode}" id="paymentAgencyAddress_cityInseeCode" name="paymentAgencyAddress.cityInseeCode" />
            <label for="paymentAgencyAddress.countryName"><g:message code="address.property.countryName" /></label>
            <input type="text" class="validate-addressLine38 ${rqt.stepStates['socialSecurityAndPaymentAgency'].invalidFields.contains('paymentAgencyAddress.countryName') ? 'validation-failed' : ''}" value="${rqt.paymentAgencyAddress?.countryName}" maxlength="38" id="paymentAgencyAddress.countryName" name="paymentAgencyAddress.countryName" />
            </div>
            

    
    </fieldset>
  

