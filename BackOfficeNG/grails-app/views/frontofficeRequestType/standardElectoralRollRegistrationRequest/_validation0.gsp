


  

    <h3>${message(code:'serrr.step.homeFolder.label')}</h3>

    
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
          


  


  
    <h3><g:message code="serrr.step.inscription.label" /></h3>
    
      
      <dl>
        <dt><g:message code="serrr.property.subject.label" /></dt>
          <dd>${subjects.get(rqt.subjectId)}</dd>

      </dl>
      
    
      
      <dl>
        <dt><g:message code="serrr.property.nomNaissance.label" /></dt>
          <dd>${rqt.nomNaissance?.toString()}</dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="serrr.property.prenom.label" /></dt>
          <dd>${rqt.prenom?.toString()}</dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="serrr.property.deuxiemePrenom.label" /></dt>
          <dd>${rqt.deuxiemePrenom?.toString()}</dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="serrr.property.troisiemePrenom.label" /></dt>
          <dd>${rqt.troisiemePrenom?.toString()}</dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="serrr.property.sexe.label" /></dt>
          <dd>
            <g:if test="${rqt.sexe}">
              <g:libredematEnumToField var="${rqt.sexe}" i18nKeyPrefix="serrr.property.sexe" />
            </g:if>
          </dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="serrr.property.nomMarital.label" /></dt>
          <dd>${rqt.nomMarital?.toString()}</dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="serrr.property.dateNaissance.label" /></dt>
          <dd><g:formatDate formatName="format.date" date="${rqt.dateNaissance}"/></dd>
          

      </dl>
      
    
      
      <h4><g:message code="serrr.property.lieuNaissance.label" /></h4>
      <dl>
        
          <dt><g:message code="serrr.property.villeNaissanceCodePostal.label" /></dt>
          <dd>${rqt.villeNaissanceCodePostal?.toString()}</dd>
          

        
          <dt><g:message code="serrr.property.lieuNaissanceDepartement.label" /></dt>
          <dd>
            <g:if test="${rqt.lieuNaissanceDepartement}">
              <g:libredematEnumToField var="${rqt.lieuNaissanceDepartement}" i18nKeyPrefix="serrr.property.lieuNaissanceDepartement" />
            </g:if>
          </dd>
          

        
          <dt><g:message code="serrr.property.lieuNaissancePays.label" /></dt>
          <dd>
            <g:if test="${rqt.lieuNaissancePays}">
              <g:libredematEnumToField var="${rqt.lieuNaissancePays}" i18nKeyPrefix="serrr.property.lieuNaissancePays" />
            </g:if>
          </dd>
          

        
      </dl>
      
    
      
      <dl>
        <dt><g:message code="serrr.property.nationalite.label" /></dt>
          <dd>
            <g:if test="${rqt.nationalite}">
              <g:libredematEnumToField var="${rqt.nationalite}" i18nKeyPrefix="serrr.property.nationalite" />
            </g:if>
          </dd>
          

      </dl>
      
    
      
      <h4><g:message code="serrr.property.fieldsetEstUnionEuropeenne.label" /></h4>
      <dl>
        
          <dt><g:message code="serrr.property.precisionNationalite.label" /></dt>
          <dd>
            <g:if test="${rqt.precisionNationalite}">
              <g:libredematEnumToField var="${rqt.precisionNationalite}" i18nKeyPrefix="serrr.property.precisionNationalite" />
            </g:if>
          </dd>
          

        
          <dt><g:message code="serrr.property.typeElection.label" /></dt>
          <dd>
            <g:if test="${rqt.typeElection}">
              <g:libredematEnumToField var="${rqt.typeElection}" i18nKeyPrefix="serrr.property.typeElection" />
            </g:if>
          </dd>
          

        
          <dt><g:message code="serrr.property.paysPrecedent.label" /></dt>
          <dd>
            <g:if test="${rqt.paysPrecedent}">
              <g:libredematEnumToField var="${rqt.paysPrecedent}" i18nKeyPrefix="serrr.property.paysPrecedent" />
            </g:if>
          </dd>
          

        
          <dt><g:message code="serrr.property.subdivisionAdministrativePrecedente.label" /></dt>
          <dd>${rqt.subdivisionAdministrativePrecedente?.toString()}</dd>
          

        
          <dt><g:message code="serrr.property.communeOuLocalitePrecedente.label" /></dt>
          <dd>${rqt.communeOuLocalitePrecedente?.toString()}</dd>
          

        
      </dl>
      
    
  


  
    <h3><g:message code="serrr.step.situation.label" /></h3>
    
      
      <dl>
        <dt><g:message code="serrr.property.situation.label" /></dt>
          <dd>
            <g:if test="${rqt.situation}">
              <g:libredematEnumToField var="${rqt.situation}" i18nKeyPrefix="serrr.property.situation" />
            </g:if>
          </dd>
          

      </dl>
      
    
      
      <h4><g:message code="serrr.property.precedentLieuInscription.label" /></h4>
      <dl>
        
          <dt><g:message code="serrr.property.ancienneCommune.label" /></dt>
          <dd>${rqt.ancienneCommune?.toString()}</dd>
          

        
          <dt><g:message code="serrr.property.departementAncienneCommune.label" /></dt>
          <dd>
            <g:if test="${rqt.departementAncienneCommune}">
              <g:libredematEnumToField var="${rqt.departementAncienneCommune}" i18nKeyPrefix="serrr.property.departementAncienneCommune" />
            </g:if>
          </dd>
          

        
      </dl>
      
    
  


  
    <h3><g:message code="serrr.step.radiation.label" /></h3>
    
      
      <dl>
        <dt><g:message code="serrr.property.ambassadeOuPosteConsulaire.label" /></dt>
          <dd>${rqt.ambassadeOuPosteConsulaire?.toString()}</dd>
          

      </dl>
      
    
      
      <dl>
        <dt><g:message code="serrr.property.paysRadiation.label" /></dt>
          <dd>
            <g:if test="${rqt.paysRadiation}">
              <g:libredematEnumToField var="${rqt.paysRadiation}" i18nKeyPrefix="serrr.property.paysRadiation" />
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
  


  


