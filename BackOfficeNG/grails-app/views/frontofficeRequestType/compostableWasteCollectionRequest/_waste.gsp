


  
    <label class="required"><g:message code="cwcr.property.compostableWasteType.label" /> *  <span><g:message code="cwcr.property.compostableWasteType.help" /></span></label>
            <g:set var="compostableWasteTypeIndex" value="${0}" scope="flash" />
            <g:render template="/frontofficeRequestType/widget/localReferentialData" 
                      model="['javaName':'compostableWasteType', 'i18nPrefixCode':'cwcr.property.compostableWasteType', 'htmlClass':'required  ', 
                              'lrEntries': lrTypes.compostableWasteType.entries, 'depth':0]" />
            

  

  
    <label for="otherWaste" class=""><g:message code="cwcr.property.otherWaste.label" />   <span><g:message code="cwcr.property.otherWaste.help" /></span></label>
            <input type="text" id="otherWaste" name="otherWaste" value="${rqt.otherWaste?.toString()}" 
                    class="  validate-string ${stepStates != null && stepStates['waste']?.invalidFields.contains('otherWaste') ? 'validation-failed' : ''}" title="<g:message code="cwcr.property.otherWaste.validationError" />"   />
            

  

  
    <label class=""><g:message code="cwcr.property.collectionAddress.label" />   <span><g:message code="cwcr.property.collectionAddress.help" /></span></label>
            <div id="collectionAddress" class="address-fieldset   ${stepStates != null && stepStates['waste']?.invalidFields.contains('collectionAddress') ? 'validation-failed' : ''}">
            <label for="collectionAddress.additionalDeliveryInformation"><g:message code="address.property.additionalDeliveryInformation" /></label>
            <input type="text" class="validate-addressLine38 ${stepStates != null && stepStates['waste']?.invalidFields.contains('collectionAddress.additionalDeliveryInformation') ? 'validation-failed' : ''}" value="${rqt.collectionAddress?.additionalDeliveryInformation}" maxlength="38" id="collectionAddress.additionalDeliveryInformation" name="collectionAddress.additionalDeliveryInformation" />  
            <label for="collectionAddress.additionalGeographicalInformation"><g:message code="address.property.additionalGeographicalInformation" /></label>
            <input type="text" class="validate-addressLine38 ${stepStates != null && stepStates['waste']?.invalidFields.contains('collectionAddress.additionalGeographicalInformation') ? 'validation-failed' : ''}" value="${rqt.collectionAddress?.additionalGeographicalInformation}" maxlength="38" id="collectionAddress.additionalGeographicalInformation" name="collectionAddress.additionalGeographicalInformation" />
            <label for="collectionAddress_streetNumber"><g:message code="address.property.streetNumber" /></label> - 
            <label for="collectionAddress_streetName" class="required"><g:message code="address.property.streetName" /> *</label><br />
            <input type="text" class="line1 validate-streetNumber ${stepStates != null && stepStates['waste']?.invalidFields.contains('collectionAddress.streetNumber') ? 'validation-failed' : ''}" value="${rqt.collectionAddress?.streetNumber}" size="5" maxlength="5" id="collectionAddress_streetNumber" name="collectionAddress.streetNumber" />
            <input type="text" class="line2 required validate-streetName ${stepStates != null && stepStates['waste']?.invalidFields.contains('collectionAddress.streetName') ? 'validation-failed' : ''}" value="${rqt.collectionAddress?.streetName}" maxlength="32" id="collectionAddress_streetName" name="collectionAddress.streetName" title="<g:message code="address.property.streetName.validationError" />" />
            <input type="hidden" value="${rqt.collectionAddress?.streetMatriculation}" id="collectionAddress_streetMatriculation" name="collectionAddress.streetMatriculation" />
            <input type="hidden" value="${rqt.collectionAddress?.streetRivoliCode}" id="collectionAddress_streetRivoliCode" name="collectionAddress.streetRivoliCode" />
            <label for="collectionAddress.placeNameOrService"><g:message code="address.property.placeNameOrService" /></label>
            <input type="text" class="validate-addressLine38 ${stepStates != null && stepStates['waste']?.invalidFields.contains('collectionAddress.placeNameOrService') ? 'validation-failed' : ''}" value="${rqt.collectionAddress?.placeNameOrService}" maxlength="38" id="collectionAddress.placeNameOrService" name="collectionAddress.placeNameOrService" />
            <label for="collectionAddress_postalCode" class="required"><g:message code="address.property.postalCode" /> * </label> - 
            <label for="collectionAddress_city" class="required"><g:message code="address.property.city" /> *</label><br />
            <input type="text" class="line1 required validate-postalCode ${stepStates != null && stepStates['waste']?.invalidFields.contains('collectionAddress.postalCode') ? 'validation-failed' : ''}" value="${rqt.collectionAddress?.postalCode}" size="5" maxlength="5" id="collectionAddress_postalCode" name="collectionAddress.postalCode" title="<g:message code="address.property.postalCode.validationError" />" />
            <input type="text" class="line2 required validate-city ${stepStates != null && stepStates['waste']?.invalidFields.contains('collectionAddress.city') ? 'validation-failed' : ''}" value="${rqt.collectionAddress?.city}" maxlength="32" id="collectionAddress_city" name="collectionAddress.city" title="<g:message code="address.property.city.validationError" />" />
            <input type="hidden" value="${rqt.collectionAddress?.cityInseeCode}" id="collectionAddress_cityInseeCode" name="collectionAddress.cityInseeCode" />
            <label for="collectionAddress.countryName"><g:message code="address.property.countryName" /></label>
            <input type="text" class="validate-addressLine38 ${stepStates != null && stepStates['waste']?.invalidFields.contains('collectionAddress.countryName') ? 'validation-failed' : ''}" value="${rqt.collectionAddress?.countryName}" maxlength="38" id="collectionAddress.countryName" name="collectionAddress.countryName" />
            </div>
            

  

