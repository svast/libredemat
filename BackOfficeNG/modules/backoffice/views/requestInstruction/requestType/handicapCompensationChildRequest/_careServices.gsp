

  <g:set var="listSize" value="${request.careServices.size()}" />
  <h3>
    <a class="addListItem" id="add_careServices[${listSize}]"></a>
    <span><g:message code="hccr.property.careServices.label" /></span>
  </h3>
  <g:each var="it" in="${request.careServices.reverse()}" status="index">
  <div class="collection-action">
    <a class="deleteListItem" id="delete_careServices[${listSize - 1 - index}]"></a>
  </div>
  <dl class="required condition-isCareServices-filled">
    
      <dt class="required"><g:message code="hccr.property.careServiceKind.label" /> * : </dt>
      <dd id="careServices[${listSize - 1 - index}].careServiceKind" class="action-editField validate-string required-true i18n-hccr.property.careServiceKind" >
        <span>${it?.careServiceKind}</span>
      </dd>
    
      <dt class="required condition-isCareServiceEmployer-trigger"><g:message code="hccr.property.careServiceCareServiceEmployer.label" /> * : </dt>
      <dd id="careServices[${listSize - 1 - index}].careServiceCareServiceEmployer" class="action-editField validate-boolean required-true i18n-hccr.property.careServiceCareServiceEmployer" >
        <span class="value-${it?.careServiceCareServiceEmployer}"><g:message code="message.${it?.careServiceCareServiceEmployer ? 'yes' : 'no'}" /></span>
      </dd>
    
      <dt class="required condition-isCareServiceEmployer-unfilled"><g:message code="hccr.property.careServiceProviderName.label" /> * : </dt>
      <dd id="careServices[${listSize - 1 - index}].careServiceProviderName" class="action-editField validate-lastName required-true i18n-hccr.property.careServiceProviderName" >
        <span>${it?.careServiceProviderName}</span>
      </dd>
    
      <dt class="condition-isCareServiceEmployer-unfilled"><g:message code="hccr.property.careServiceProviderAddress.label" />  : </dt>
      <dd id="careServices[${listSize - 1 - index}].careServiceProviderAddress" class="action-editField validate-address i18n-hccr.property.careServiceProviderAddress" >
        <div><p class="additionalDeliveryInformation">${it?.careServiceProviderAddress?.additionalDeliveryInformation}</p><p class="additionalGeographicalInformation">${it?.careServiceProviderAddress?.additionalGeographicalInformation}</p><span class="streetNumber">${it?.careServiceProviderAddress?.streetNumber}</span> <span class="streetName">${it?.careServiceProviderAddress?.streetName}</span><p class="placeNameOrService">${it?.careServiceProviderAddress?.placeNameOrService}</p><span class="postalCode">${it?.careServiceProviderAddress?.postalCode}</span> <span class="city">${it?.careServiceProviderAddress?.city}</span><p class="countryName">${it?.careServiceProviderAddress?.countryName}</p></div>
      </dd>
    
  </dl>
  </g:each>
