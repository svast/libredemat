


  
    <fieldset class="required">
    <legend><g:message code="sgr.property.subjectInformations.label" /></legend>
    
      
            <label for="subjectId" class="required"><g:message code="request.property.subject.label" /> *  <span><g:message code="request.property.subject.help" /></span></label>
            <select id="subjectId" name="subjectId" <g:if test="${isEdition}">disabled="disabled"</g:if> class="required validate-not-first autofill-subjectFilling-trigger" title="<g:message code="request.property.subject.validationError" /> ">
              <option value=""><g:message code="message.select.defaultOption" /></option>
              <g:each in="${subjects}">
                <option value="${it.key}" ${it.key == rqt.subjectId ? 'selected="selected"': ''}>${it.value}</option>
              </g:each>
            </select>
            

    
      <label class="required"><g:message code="sgr.property.subjectAddress.label" /> *  <span><g:message code="sgr.property.subjectAddress.help" /></span></label>
            <div class="address-fieldset required autofill-subjectFilling-listener-Adress">
            <label for="subjectAddress.additionalDeliveryInformation"><g:message code="address.property.additionalDeliveryInformation" /></label>
            <input type="text" value="${rqt.subjectAddress?.additionalDeliveryInformation}" maxlength="38" id="subjectAddress.additionalDeliveryInformation" name="subjectAddress.additionalDeliveryInformation" />  
            <label for="subjectAddress.additionalGeographicalInformation"><g:message code="address.property.additionalGeographicalInformation" /></label>
            <input type="text" value="${rqt.subjectAddress?.additionalGeographicalInformation}" maxlength="38" id="subjectAddress.additionalGeographicalInformation" name="subjectAddress.additionalGeographicalInformation" />
            <label for="subjectAddress.streetNumber"><g:message code="address.property.streetNumber" /></label> - 
            <label for="subjectAddress.streetName" class="required"><g:message code="address.property.streetName" /> *</label><br />
            <input type="text" class="line1" value="${rqt.subjectAddress?.streetNumber}" size="5" maxlength="5" id="subjectAddress.streetNumber" name="subjectAddress.streetNumber" />
            <input type="text" class="line2 required" value="${rqt.subjectAddress?.streetName}" maxlength="32" id="subjectAddress.streetName" name="subjectAddress.streetName" title="<g:message code="address.property.streetName.validationError" />" />
            <label for="subjectAddress.placeNameOrService"><g:message code="address.property.placeNameOrService" /></label>
            <input type="text" value="${rqt.subjectAddress?.placeNameOrService}" maxlength="38" id="subjectAddress.placeNameOrService" name="subjectAddress.placeNameOrService" />
            <label for="subjectAddress.postalCode" class="required"><g:message code="address.property.postalCode" /> * </label> - 
            <label for="subjectAddress.city" class="required"><g:message code="address.property.city" /> *</label><br />
            <input type="text" class="line1 required" value="${rqt.subjectAddress?.postalCode}" size="5" maxlength="5" id="subjectAddress.postalCode" name="subjectAddress.postalCode" title="<g:message code="address.property.postalCode.validationError" />" />
            <input type="text" class="line2 required" value="${rqt.subjectAddress?.city}" maxlength="32" id="subjectAddress.city" name="subjectAddress.city" title="<g:message code="address.property.city.validationError" />" />
            <label for="subjectAddress.countryName"><g:message code="address.property.countryName" /></label>
            <input type="text" value="${rqt.subjectAddress?.countryName}" maxlength="38" id="subjectAddress.countryName" name="subjectAddress.countryName" />
            </div>
            

    
      <label for="subjectPhone" class=""><g:message code="sgr.property.subjectPhone.label" />   <span><g:message code="sgr.property.subjectPhone.help" /></span></label>
            <input type="text" id="subjectPhone" name="subjectPhone" value="${rqt.subjectPhone?.toString()}" 
                    class=" autofill-subjectFilling-listener-HomePhone validate-phone" title="<g:message code="sgr.property.subjectPhone.validationError" />"  maxlength="10" />
            

    
      <label for="subjectMobilePhone" class=""><g:message code="sgr.property.subjectMobilePhone.label" />   <span><g:message code="sgr.property.subjectMobilePhone.help" /></span></label>
            <input type="text" id="subjectMobilePhone" name="subjectMobilePhone" value="${rqt.subjectMobilePhone?.toString()}" 
                    class=" autofill-subjectFilling-listener-MobilePhone validate-mobilePhone" title="<g:message code="sgr.property.subjectMobilePhone.validationError" />"  maxlength="10" />
            

    
      <label for="subjectEmail" class=""><g:message code="sgr.property.subjectEmail.label" />   <span><g:message code="sgr.property.subjectEmail.help" /></span></label>
            <input type="text" id="subjectEmail" name="subjectEmail" value="${rqt.subjectEmail?.toString()}" 
                    class=" autofill-subjectFilling-listener-Email validate-email" title="<g:message code="sgr.property.subjectEmail.validationError" />"   />
            

    
      <label for="subjectBirthDate" class="required"><g:message code="sgr.property.subjectBirthDate.label" /> *  <span><g:message code="sgr.property.subjectBirthDate.help" /></span></label>
            <input type="text" id="subjectBirthDate" name="subjectBirthDate" value="${formatDate(formatName:'format.date',date:rqt.subjectBirthDate)}" 
                   class="required autofill-subjectFilling-listener-BirthDate validate-date" title="<g:message code="sgr.property.subjectBirthDate.validationError" />" />
            

    
      <label class="required"><g:message code="sgr.property.subjectFirstRequest.label" /> *  <span><g:message code="sgr.property.subjectFirstRequest.help" /></span></label>
            <ul class="yes-no required">
              <g:each in="${[true,false]}">
              <li>
                <input type="radio" id="subjectFirstRequest_${it ? 'yes' : 'no'}" class="required  validate-one-required boolean" title="" value="${it}" name="subjectFirstRequest" ${it == rqt.subjectFirstRequest ? 'checked="checked"': ''} />
                <label for="subjectFirstRequest_${it ? 'yes' : 'no'}"><g:message code="message.${it ? 'yes' : 'no'}" /></label>
              </li>
              </g:each>
            </ul>
            

    
    </fieldset>
  
