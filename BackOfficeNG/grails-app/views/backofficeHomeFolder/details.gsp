<html>
  <head>
    <title>${message(code:'homeFolder.header.details', args:[params.id])}</title>
    <meta name="layout" content="main" />
    <g:if test="${flash.addressesReferentialEnabled}">
      <link rel="stylesheet" type="text/css" href="${resource(dir:'css/common', file:'autocomplete.css')}" />
      <script type="text/javascript">
        zenexity.libredemat.contextPath = "${request.contextPath}";
      </script>
      <script type="text/javascript" src="${resource(dir:'js/common',file:'addressAutocomplete.js')}"></script>
      <script type="text/javascript" src="${resource(dir:'js/common',file:'autocomplete.js')}"></script>
    </g:if>
    <script type="text/javascript" src="${resource(dir:'js/common',file:'calendar.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js/backoffice',file:'contact.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js/backoffice',file:'homeFolderDetails.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js/backoffice',file:'userDocumentInstruction.js')}"></script>
    <link rel="stylesheet" href="${resource(dir:'css/backoffice/common/yui-skin/',file:'container.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/backoffice',file:'homeFolder.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/backoffice',file:'document.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/backoffice',file:'contact.css')}" />
    <script type="text/javascript">
      zenexity.libredemat.bong.homeFolder.Details.homeFolderId = ${params.id};
      zenexity.libredemat.bong.contactPanelUrl = "${createLink(controller : 'backofficeContact', action : 'panel')}";
      zenexity.libredemat.bong.homeFolder.Details.agentCanWrite = ${agentCanWrite};
    </script>
  </head>
  <body>
    <g:if test="${flash.errorMessage}">
      <div class="error-box"><p>${flash.errorMessage}</p></div>
    </g:if>
    <g:if test="${flash.successMessage}">
      <div class="success-box"><p>${flash.successMessage}</p></div>
    </g:if>

    <div id="syncMessage" class="invisible">
        <div class="information-box"><p><g:message code="homeFolder.synchronisation.posted" /></p></div>
    </div>

    <div id="yui-main">
      <div class="yui-b">
        <div class="head">
          <g:if test="${homeFolderResponsible?.state?.toString() != 'Archived'}">
            <div id="contactContainer" class="txt-right">
              <a id="contactLink">
                <g:message code="contact.header.contactEcitizen" />
              </a>
              <div id="contactPanel">
                <div class="hd"></div>
                <div class="bd"></div>
              </div>
            </div>
          </g:if>
          <h1>${message(code:'homeFolder.header.details', args:[params.id])}</h1>
        </div>

        <g:if test="${homeFolderResponsible.duplicateAlert}">
          <div id="duplicates" class="mainbox">
            <h2><g:message code="homeFolder.message.duplicateAlert" /></h2>

            <h3><g:message code="homeFolder.message.duplicate.actions" /></h3>
            <ul>
                <li><g:message code="action.merge" /> : <g:message code="homeFolder.message.duplicate.merge" /></li>
                <li><g:message code="action.ignore" /> : <g:message code="homeFolder.message.duplicate.ignore" /></li>
            </ul>
            <g:each in="${homeFolderDuplicates}" var="duplicate">
              <div class="yui-g duplicate">
                <div class="yui-u first">
                  <h4>
                    <g:message code="homeFolder.message.duplicate.homeFolderId" /> ${duplicate.key}
                    <span>(<a href="${createLink(controller: 'backofficeHomeFolder',action:'details', id : duplicate.key)}"><g:message code="homeFolder.header.goToAccount" /></a>)</span>
                  </h4>
                  <dl>
                    <dd class="duplicateData">${duplicate.value['duplicateResponsibleData'].firstName} ${duplicate.value['duplicateResponsibleData'].lastName}</dd>
                    <dd ${duplicate.value['streetName'] != null ? 'class=\"duplicateData\"' : ''}>${duplicate.value['duplicateResponsibleData'].address.streetName}</dd>
                    <dd ${duplicate.value['email'] != null ? 'class=\"duplicateData\"' : ''}>${duplicate.value['duplicateResponsibleData'].email}</dd>
                  </dl>

                  </h4><g:message code="homeFolder.message.otherDuplicates" /> : ${duplicate.value['otherDuplicates'].size()}</h4>
                  <ul>
                    <g:each in="${duplicate.value['otherDuplicates']}" var="otherDuplicate">
                      <li>${otherDuplicate}</li>
                    </g:each>
                  </ul>
                </div>

                <div class="yui-u txt-right">
                  <g:while test="${duplicate.value['rank'] > 0}">
                    <img src="${createLinkTo(dir:'images/icons',file:'16-star.png')}" alt="${message(code:'homeFolder.label.rank')}" />
                    <%duplicate.value['rank']--%>
                  </g:while>
                  <br />
                  <form id="ignoreDuplicate" method="post" action="${g.createLink(action:'processDuplicate')}" style="float: right">
                    <input type="submit" name="ignore" value="${message(code:'action.ignore')}" ${agentCanWrite ? '' : 'disabled="disabled"'}/>
                    <input type="submit" name="merge" value="${message(code:'action.merge')}" ${agentCanWrite ? '' : 'disabled="disabled"'}/>
                    <input type="hidden" name="targetHomeFolderId" value="${duplicate.key}" />
                    <input type="hidden" name="homeFolderId" value="${homeFolder.id}" />
                  </form>
                </div>
              </div>
            </g:each>
          </div>
        </g:if>

        <g:if test="${!homeFolderResponsible.duplicateAlert && !individualDuplicates?.isEmpty()}">
          <div id="duplicates" class="mainbox">
            <h2><g:message code="individual.message.duplicateAlert" /></h2>

            <h3><g:message code="individual.message.duplicate.actions" /></h3>
            <ul>
                <li><g:message code="action.ignore" /> : <g:message code="individual.message.duplicate.ignore" /></li>
            </ul>
            <g:each in="${individualDuplicates}" var="duplicate">
              <div class="yui-g duplicate">
                  <h4>
                    <g:message code="individual.message.duplicate.homeFolderId" /> ${duplicate.key}
                    <g:if test="${Long.valueOf(duplicate.key) != homeFolder?.id}">
                    <span>(<a href="${createLink(controller: 'backofficeHomeFolder',action:'details', id : duplicate.key)}"><g:message code="homeFolder.header.goToAccount" /></a>)</span>
                    </g:if>
                  </h4>
                <span><g:message code="individual.message.duplicate.similars" /></span><br />
                <g:each in="${duplicate.value}" var="duplicateIndividual">
                  <div>
                  <div class="yui-u first">
                    <span><g:message code="individual.message.duplicate.individualId" /> ${duplicateIndividual.key}</span>
                      <dl>
                        <dd ${duplicateIndividual.value['birthDate'] != null ? 'class=\"duplicateData\"' : ''}><g:formatDate format="dd/MM/yyyy" date="${duplicateIndividual.value['birthDate']}"/></dd>
                        <dd ${duplicateIndividual.value['lastName'] != null ? 'class=\"duplicateData\"' : ''}>${duplicateIndividual.value['lastName']}</dd>
                        <dd ${duplicateIndividual.value['firstName'] != null ? 'class=\"duplicateData\"' : ''}>${duplicateIndividual.value['firstName']}</dd>
                        <dd ${duplicateIndividual.value['streetName'] != null ? 'class=\"duplicateData\"' : ''}>${duplicateIndividual.value['streetName']}</dd>
                        <dd ${duplicateIndividual.value['email'] != null ? 'class=\"duplicateData\"' : ''}>${duplicateIndividual.value['email']}</dd>
                      </dl>
                  </div>
                  <div class="yui-u txt-right">
                  <g:while test="${duplicateIndividual.value['rank'] > 0}">
                    <img src="${createLinkTo(dir:'images/icons',file:'16-star.png')}" alt="${message(code:'homeFolder.label.rank')}" />
                    <%duplicateIndividual.value['rank']--%>
                  </g:while>
                  <br />
                  <form id="ignoreDuplicate" method="post" action="${g.createLink(action:'processDuplicate')}" style="float: right">
                    <input type="submit" name="ignore" value="${message(code:'action.ignore')}" ${agentCanWrite ? '' : 'disabled="disabled"'}/>
                    <input type="hidden" name="targetHomeFolderId" value="${duplicate.key}" />
                    <input type="hidden" name="homeFolderId" value="${homeFolder.id}" />
                  </form>
                </div>
                  </div>
                </g:each>
                </div>
            </g:each>
          </div>
        </g:if>

        <div id="homeFolder" class="mainbox mainbox-yellow">
          <h2>${message(code:'homeFolder.search.isHomeFolderResponsible')}</h2>

          <div id="adult_${homeFolderResponsible.id}" class="individual collapse">
            <a class="toggle">${message(code:'action.expand')} / ${message(code:'action.collapse')}</a>
            <div class="yui-g">
              <div class="yui-u first">
                <dl class="required">
                  <g:render template="static/id" model="['user':homeFolderResponsible]" />
                </dl>
                <dl class="${homeFolderResponsible?.state?.toString() != 'Archived' ? 'edit' : ''} individual-state required collapse">
                  <g:render template="static/state" model="['user':homeFolderResponsible]" />
                </dl>
                <h3>${message(code:'homeFolder.individual.header.identity')}</h3>
                <dl class="${homeFolderResponsible?.state?.toString() != 'Archived' ? 'edit' : ''} individual-identity required collapse">
                  <g:render template="static/adultIdentity" model="['individual':homeFolderResponsible]" />
                </dl>
                <h3>${message(code:'homeFolder.individual.header.connexion')}</h3>
                <dl class="required collapse">
                  <g:render template="static/connexion" model="['adult':homeFolderResponsible]" />
                </dl>
                <g:if test="${adultsRoles != null && adultsRoles.get(homeFolderResponsible.id) != null &&
                !adultsRoles.get(homeFolderResponsible.id).isEmpty()}">
                <h3>${message(code:'homeFolder.individual.header.responsibles.adult')}</h3>
                <dl class="${homeFolderResponsible?.state?.toString() != 'Archived' ? 'edit' : ''} individual-adultResponsibles collapse">
                  <g:render template="static/adultResponsibles" model="['adult':homeFolderResponsible, 'roles': adultsRoles.get(homeFolderResponsible.id)]" />
                </dl>
                </g:if>


              </div>
              <div class="yui-u">
                <h3>${message(code:'homeFolder.individual.header.address')}</h3>
                <dl class="${homeFolderResponsible?.state?.toString() != 'Archived' ? 'edit' : ''} adult-address required collapse">
                  <g:render template="static/address" model="['user' : homeFolderResponsible]" />
                </dl>
                <h3>${message(code:'homeFolder.individual.header.contact')}</h3>
                <dl class="${homeFolderResponsible?.state?.toString() != 'Archived' ? 'edit' : ''} adult-contact required collapse">
                  <g:render template="static/contact" model="['adult':homeFolderResponsible, 'isResponsible':true]" />
                </dl>
                <g:each var="homeMapping" in="${homeMappings}"> 
                   <g:each var="indivMap" in="${homeMapping.individualsMappings}">
                   		<g:if test="${indivMap.individualId == homeFolderResponsible.id}">
		                   <h3>${homeMapping.externalServiceLabel}</h3>
		                   <dl class="${homeFolderResponsible?.state?.toString() != 'Archived' ? 'edit' : ''} individual-${homeMapping.externalServiceLabel.replace(" ", "#")}-mapping required collapse">
		                      <g:render template="static/mapping" model="['mapping':indivMap]" />
		                   </dl>
	                   </g:if>
                	</g:each>
                   <dl class="${homeFolderResponsible?.state?.toString() != 'Archived' ? 'edit' : ''} individual-${homeMapping.externalServiceLabel.replace(" ", "#")}-homeFolderMapping required collapse">
                    <!-- to do inexine get home folder external id -->
                      <g:render template="static/homeFolderMapping" model="['mapping':homeMapping]" />
                   </dl>
                </g:each>
              </div>
            </div>
          </div>

          <div class="yui-g">
            <div class="yui-u first">
              <h2>
                ${message(code:'homeFolder.property.adults')}
                <g:if test="${homeFolderResponsible?.state?.toString() != 'Archived'}">
                  <a class="add adult" style="font-size:.7em;">${message(code:'action.add')}</a>
                </g:if>
              </h2>
              <div class="new"></div>
              <g:each var="adult" in="${adults}">
                <g:render template="static/adult" model="['adult':adult,'unarchivable': unarchivableIndividuals.contains(adult.id)]" />
              </g:each>
            </div>
            <div class="yui-u">
              <h2>
                ${message(code:'homeFolder.property.children')}
                <g:if test="${homeFolderResponsible?.state?.toString() != 'Archived'}">
                  <a class="add child" style="font-size:.7em;">${message(code:'action.add')}</a>
                </g:if>
              </h2>
              <div class="new"></div>
              <g:each var="child" in="${children}">
                <g:render template="static/child" model="['child':child, 'roleOwners': responsibles[child.id], 'roles': childsRoles, 'homeMappings' : homeMappings,'unarchivable': unarchivableIndividuals.contains(child.id), 'isAccessSanitaire': isAccessSanitaire]" />
              </g:each>
            </div>
          </div>

        </div>

        <!-- Request TabView -->
        <div id="homeFolderInformation"></div>

        <!-- document managment panel [default display = none] -->
        <div id="documentPanel">
          <div class="hd"></div>
          <div class="bd"></div>
        </div>

      </div>
    </div>
    <div id="narrow" class="yui-b">
      <g:if test="${homeFolderState != 'archived'}">
        <div class="nobox taskstate">
          <h3>${message(code:'header.subMenus')}</h3>
          <div class="body">
            <a href="${createLink(controller: 'backofficeHomeFolder',action:'findDuplicates', id : homeFolderResponsible.id)}">
              ${message(code:'homeFolder.header.findDuplicates')}
            </a>
          </div>
        </div>
      </g:if>

    <g:if test="${homeFolderState != 'archived'}">
        <div class="nobox taskstate">
          <h3>${message(code:'header.synchronize')}</h3>
          <div class="body">
            <g:if test="${homeFolderState == 'valid'}">
              <form method="post" id="synchronisation" action="${createLink(action : "synchronise")}">
                <div class="form-group">
                  <label>Veuillez sélectionner un ou plusieurs services :</label>
                  <g:select name="services" multiple="yes" from="${externalProviders}" require="required"/>
                </div>
                <input type="hidden" name="id" value="${homeFolderResponsible.id}" />
                <input id="initialisation" type="submit" value="${message(code:'action.sync')}" disabled />
              </form>
            </g:if>
            <g:else>
              <span style="text-decoration: underline;">${message(code:'homeFolder.header.synchronise')}</span> (${message(code:'homeFolder.header.synchronise.help')})
            </g:else>
          </div>
        </div>
      </g:if>

      <g:if test="${homeFolderResponsible?.state?.toString() != 'Archived'}">
        <div class="nobox taskstate">
          <h3>${message(code:'home.header.realizeRequest')}</h3>
          <div class="body">
            <form method="post" action="${g.createLink(action:'realizeRequest', id:homeFolderResponsible.id)}">
              <label for="requestTypeId">${message(code:'property.requestType')} :</label>
              <select id="requestTypeId" name="requestTypeId">
                <option value=""><g:message code="search.filter.defaultValue"/></option>
                <g:each var="group" in="${groups}">
                    <optgroup label="${group.value.get('label')}">
                        <g:each in="${group.value.get('requests')}" var="requestType">
                          <option value="${requestType.id}">
                            <g:translateRequestTypeLabel label="${requestType.label}"/>
                          </option>
                        </g:each>
                    </optgroup>
                </g:each>
              </select>
              <input type="submit" value="${message(code:'action.begin')}" />
            </form>
          </div>
        </div>
      </g:if>

      <!-- home folder state -->
      <div class="nobox taskstate">
        <h3>${message(code:'property.homeFolderState')}</h3>
        <div class="body">
          <g:if test="${isValidable}">
        <form method="post" action="${g.createLink(action:'validateHomeFolder',id:homeFolder.id)}">
        <button id="validateHomeFolder">Valider le compte</button>
        
        </g:if>
          <span id="homeFolderState" class="tag-${homeFolderState}" style="float: right; font-size:1.1em;">
            ${message(code:'user.state.' + homeFolderState)}
          </span>
        </form>
        </div>
      </div>

      <!-- request document state -->
      <g:if test="${homeFolderResponsible?.state?.toString() != 'Archived'}">
        <div class="nobox taskstate">
          <h3><g:message code="property.documents" /></h3>
          <div class="body">
            <ul class="document-list" id="fullDocumentList">
            </ul>
          </div>
        </div>
      </g:if>

      <div id="documentStateOverlay" class="state-overlay">
        <div class="hd"> </div>
        <div class="bd"> </div>
      </div>

      <div id="documentCalendarTip">
        <div class="hd"> </div>
        <div class="bd">
          <div id="documentCalendar"> </div>
        </div>
      </div>

      <g:if test="${homeFolderResponsible?.state?.toString() != 'Archived'}">
      <!--  Reset password box -->
      <div id="reset-pwd-outer">
        <g:render template="currentResetPasswordBox" model="['canResetPassword': canResetPassword]"/>
      </div>
      </g:if>

    </div>

  </body>
</html>
