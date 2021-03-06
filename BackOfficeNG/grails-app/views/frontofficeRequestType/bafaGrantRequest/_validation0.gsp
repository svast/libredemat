


  

    <h3>${message(code:'bgr.step.homeFolder.label')}</h3>

    
            <dl>
              <dt><g:libredematEnumToFlag var="${requester.title}" i18nKeyPrefix="homeFolder.adult.title" /> ${requester.fullName}</dt>
              <dd>
                <ul>
                  <li>
                    <span class="tag-homefolderresponsible tag-state">${message(code:'homeFolder.role.homeFolderResponsible')}</span>
                  </li>
                  <g:if test="${requester.homePhone}">
                    <li>${requester.homePhone}</li>
                  </g:if>
                  <g:if test="${requester.mobilePhone}">
                    <li>${requester.mobilePhone}</li>
                  </g:if>
                  <g:if test="${requester.email}">
                    <li>${requester.email}</li>
                  </g:if>
                </ul>
              </dd>
            </dl>
          


    
          <g:each in="${requester.getHomeFolder().getIndividuals().findAll{ !(it.getState().name.equals('Archived') || it.getState().name.equals('Invalid')) && (requester.getId() != it.getId()) }}" var="individual">
            <g:if test="${individual.getClass() == org.libredemat.business.users.Adult.class}">
              <dl>
                <dt><g:libredematEnumToFlag var="${individual.title}" i18nKeyPrefix="homeFolder.adult.title" /> ${individual.fullName}</dt>
                <dd>
                  <ul>
                    <g:if test="${individual.homePhone}">
                      <li>${individual.homePhone}</li>
                    </g:if>
                    <g:if test="${individual.mobilePhone}">
                      <li>${individual.mobilePhone}</li>
                    </g:if>
                    <g:if test="${individual.email}">
                      <li>${individual.email}</li>
                    </g:if>
                  </ul>
                </dd>
              </dl>
            </g:if>
          </g:each>
          


    
          <g:each in="${requester.getHomeFolder().getIndividuals().findAll{ !(it.getState().name.equals('Archived') || it.getState().name.equals('Invalid'))}}" var="individual">
            <g:if test="${individual.getClass() == org.libredemat.business.users.Child.class}">
              <dl class="${individual.state}">
                <dt>
                  <g:if test="${individual.born}">${individual.fullName}</g:if>
                  <g:else>${message(code:'request.subject.childNoBorn', args:[individual.fullName])}</g:else>
                <dd>
                  <g:if test="${individual.born}">${message(code:'homeFolder.header.born')}</g:if>
                  <g:else>${message(code:'homeFolder.header.noBorn')}</g:else>
                  <g:if test="${individual.birthDate}">
                    ${message(code:'homeFolder.header.on')}
                    ${formatDate(date:individual.birthDate,formatName:'format.date')}
                  </g:if>
                </dd>
              </dl>
            </g:if>
          </g:each>
          


  


  
    <h3><g:message code="bgr.step.subject.label" /></h3>
    
      
      <dl>
        <dt><g:message code="bgr.property.subject.label" /></dt>
          <dd>${subjects.get(rqt.subjectId)}</dd>

      </dl>
      
    
      
      <dl>
        <dt><g:message code="bgr.property.subjectBirthDate.label" /></dt>
          <dd><g:formatDate formatName="format.date" date="${rqt.subjectBirthDate}"/></dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="bgr.property.subjectBirthCity.label" /></dt>
          <dd>${rqt.subjectBirthCity?.toString()}</dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="bgr.property.subjectAddress.label" /></dt>
          <dd>
          <g:if test="${rqt.subjectAddress}">
              <p>${rqt.subjectAddress?.additionalDeliveryInformation}</p>
              <p>${rqt.subjectAddress?.additionalGeographicalInformation}</p>
              <p>${rqt.subjectAddress?.streetNumber} ${rqt.subjectAddress?.streetName}</p>
              <p>${rqt.subjectAddress?.placeNameOrService}</p>
              <p>${rqt.subjectAddress?.postalCode} ${rqt.subjectAddress?.city}</p>
              <p>${rqt.subjectAddress?.countryName}</p>
          </g:if>
          </dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="bgr.property.subjectPhone.label" /></dt>
          <dd>${rqt.subjectPhone?.toString()}</dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="bgr.property.subjectEmail.label" /></dt>
          <dd>${rqt.subjectEmail?.toString()}</dd>
          

      </dl>
      
    
  


  
    <h3><g:message code="bgr.step.internship.label" /></h3>
    
      
      <dl>
        <dt><g:message code="bgr.property.internshipStartDate.label" /></dt>
          <dd><g:formatDate formatName="format.date" date="${rqt.internshipStartDate}"/></dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="bgr.property.internshipEndDate.label" /></dt>
          <dd><g:formatDate formatName="format.date" date="${rqt.internshipEndDate}"/></dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="bgr.property.internshipInstituteName.label" /></dt>
          <dd>${rqt.internshipInstituteName?.toString()}</dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="bgr.property.internshipInstituteAddress.label" /></dt>
          <dd>
          <g:if test="${rqt.internshipInstituteAddress}">
              <p>${rqt.internshipInstituteAddress?.additionalDeliveryInformation}</p>
              <p>${rqt.internshipInstituteAddress?.additionalGeographicalInformation}</p>
              <p>${rqt.internshipInstituteAddress?.streetNumber} ${rqt.internshipInstituteAddress?.streetName}</p>
              <p>${rqt.internshipInstituteAddress?.placeNameOrService}</p>
              <p>${rqt.internshipInstituteAddress?.postalCode} ${rqt.internshipInstituteAddress?.city}</p>
              <p>${rqt.internshipInstituteAddress?.countryName}</p>
          </g:if>
          </dd>
          

      </dl>
      
    
  


  
    <h3><g:message code="bgr.step.bankReference.label" /></h3>
    
      
      <dl>
        <dt><g:message code="bgr.property.isSubjectAccountHolder.label" /></dt>
          <dd><g:message code="message.${rqt.isSubjectAccountHolder ? 'yes' : 'no'}" /></dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="bgr.property.accountHolderTitle.label" /></dt>
          <dd>
            <g:if test="${rqt.accountHolderTitle}">
              <g:libredematEnumToField var="${rqt.accountHolderTitle}" i18nKeyPrefix="bgr.property.accountHolderTitle" />
            </g:if>
          </dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="bgr.property.accountHolderLastName.label" /></dt>
          <dd>${rqt.accountHolderLastName?.toString()}</dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="bgr.property.accountHolderFirstName.label" /></dt>
          <dd>${rqt.accountHolderFirstName?.toString()}</dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="bgr.property.accountHolderBirthDate.label" /></dt>
          <dd><g:formatDate formatName="format.date" date="${rqt.accountHolderBirthDate}"/></dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="bgr.property.bankAccount.label" /></dt>
          <dd>
          <g:if test="${rqt.bankAccount}">
            <p>
              ${rqt.bankAccount?.BIC}
              ${rqt.bankAccount?.IBAN}
            </p>
          </g:if>
          </dd>
          

      </dl>
      
    
  


  
  <g:if test="${!documentsByTypes.isEmpty()}">
    <h3>${message(code:'request.step.document.label')}</h3>
    <g:each in="${documentsByTypes}" var="documentType">
      <h4>${message(code:documentType.value.name)}</h4>
      <g:if test="${documentType.value.associated}">
      <dl class="document-linked">
        <g:each in="${documentType.value.associated}" var="document">
        <dt>
          <g:if test="${document.ecitizenNote}">${message(code:'document.header.description')} : ${document.ecitizenNote}<br/></g:if>
          <g:if test="${document.endValidityDate}">${message(code:'document.header.expireOn')} ${formatDate(date:document.endValidityDate,formatName:'format.date')}</g:if>
        </dt>
        <dd>
          <g:libredematEnumToFlag var="${document.state}" i18nKeyPrefix="document.state" />
          <a href="${createLink(controller:'frontofficeDocument',action:'details', id:document.id)}" target="blank" title="${message(code:'document.message.preview.longdesc')}">${message(code:'document.message.preview')}</a>
        </dd>
        </g:each>
      </dl>
      </g:if>
      <g:else>
        ${message(code:'document.header.noAttachments')}
      </g:else>
    </g:each>
  </g:if>
  


  


