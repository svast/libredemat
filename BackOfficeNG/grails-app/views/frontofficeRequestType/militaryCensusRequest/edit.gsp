
<html>
  <head>
     <title>${message(code:'mcr.description')}</title>
    <meta name="layout" content="fo_main" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css/frontoffice', file:'request.css')}" />
    <g:if test="${flash.addressesReferentialEnabled}">
        <link rel="stylesheet" type="text/css" href="${resource(dir:'css/common', file:'autocomplete.css')}" />
    </g:if>
    <script type="text/javascript" src="${resource(dir:'js/frontoffice',file:'requestCreation.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js/frontoffice',file:'condition.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js/frontoffice',file:'autofill.js')}"></script>
    <g:if test="${flash.addressesReferentialEnabled}">
        <script type="text/javascript" src="${resource(dir:'js/common',file:'addressAutocomplete.js')}"></script>
        <script type="text/javascript" src="${resource(dir:'js/common',file:'autocomplete.js')}"></script>
    </g:if>
    <g:if test="${customJS}">
      <script type="text/javascript" src="${resource(dir:customJS.dir,file:customJS.file)}"></script>
    </g:if>
    <script type="text/javascript">
        zenexity.capdemat.contextPath = "${request.contextPath}";
    </script>
  </head>
  <body>
    <form action="${createLink(controller:'frontofficeRequestCreation',action:'condition')}"
      method="post" id="conditionsForm">
      <input type="hidden" id="conditionsContainer" name="conditionsContainer" value="" />
      <input type="hidden" name="requestTypeLabel" value="${requestTypeLabel}" />
    </form>
    <form action="${createLink(controller:'frontofficeRequestCreation',action:'autofill')}"
      method="post" id="autofillForm">
      <input type="hidden" id="autofillContainer" name="autofillContainer" value="" />
      <input type="hidden" id="triggerName" name="triggerName" value="" />
      <input type="hidden" id="triggerValue" name="triggerValue" value="" />
    </form>
    <g:if test="${flash.isOutOfAccountRequest}">
      <g:render template="/frontofficeRequestType/loginPanel" />
    </g:if>
    <g:if test="${flash.confirmationMessage}">
      <div class="information-box">
      	<p>${flash.confirmationMessage}</p>
      	<g:if test="${flash.confirmationMessageNotice}">
      	  <strong>${flash.confirmationMessageNotice}</strong>
      	</g:if>
      </div>
    </g:if>
    <g:if test="${flash.errorMessage}">
      <div class="error-box">
        <p>${flash.errorMessage}</p>
        <g:if test="${flash.errorMessageNotice}">
          <strong>${flash.errorMessageNotice}</strong>
        </g:if>
      </div>
    </g:if>
    
    <g:render template="/frontofficeRequestType/cancelPanel" />
    <g:if test="${session.currentEcitizen && !isEdition}">
      <g:render template="/frontofficeRequestType/draftPanel" />
    </g:if>
    
    <g:set var="requestTypeInfo" value="${requestTypeInfo.encodeAsHTML()}" />
    
    <h2 class="request-creation"> <g:message code="mcr.label" /></h2>
    <p><g:message code="mcr.description" /></p> 
    <p><g:message code="request.duration.label" /><strong> : <g:message code="mcr.duration.value" /></strong></p>
    <p>
      <g:message code="request.requiredDocuments.header" /> :
      <g:if test="${!documentTypes.isEmpty()}">
        <g:each in="${documentTypes}" var="documentType" status="index">
          <strong>${documentType.value.name}<g:if test="${index < documentTypes.size() - 1}">,</g:if></strong>
        </g:each>
      </g:if>
      <g:else>
        <g:message code="message.none" />
      </g:else>
    </p>

    <div id="requestTabView" class="yui-navset">
      <ul class="yui-nav">

  
        <li class="${currentStep == 'census' ? 'selected' : ''}">
          <a href="${createLink(controller:'frontofficeRequestCreation', params:['label':requestTypeLabel,'currentStep':'census'])}">
          <em>
          <span class="tag-state ${stepStates!= null ? stepStates.census.cssClass : 'tag-pending'}"><g:message code="${stepStates != null ? stepStates.census.i18nKey : 'request.step.state.uncomplete'}" /></span>
    
          <strong>
            <g:message code="mcr.step.census.label" /> *
          </strong>
            
          </em></a>
        </li>    
  

  
        <li class="${currentStep == 'parentage' ? 'selected' : ''}">
          <a href="${createLink(controller:'frontofficeRequestCreation', params:['label':requestTypeLabel,'currentStep':'parentage'])}">
          <em>
          <span class="tag-state ${stepStates!= null ? stepStates.parentage.cssClass : 'tag-pending'}"><g:message code="${stepStates != null ? stepStates.parentage.i18nKey : 'request.step.state.uncomplete'}" /></span>
    
          <strong>
            <g:message code="mcr.step.parentage.label" /> *
          </strong>
            
          </em></a>
        </li>    
  

  
        <li class="${currentStep == 'situation' ? 'selected' : ''}">
          <a href="${createLink(controller:'frontofficeRequestCreation', params:['label':requestTypeLabel,'currentStep':'situation'])}">
          <em>
          <span class="tag-state ${stepStates!= null ? stepStates.situation.cssClass : 'tag-pending'}"><g:message code="${stepStates != null ? stepStates.situation.i18nKey : 'request.step.state.uncomplete'}" /></span>
    
          <strong>
            <g:message code="mcr.step.situation.label" /> *
          </strong>
            
          </em></a>
        </li>    
  

  
        <li class="${currentStep == 'exemption' ? 'selected' : ''}">
          <a href="${createLink(controller:'frontofficeRequestCreation', params:['label':requestTypeLabel,'currentStep':'exemption'])}">
          <em>
          <span class="tag-state ${stepStates!= null ? stepStates.exemption.cssClass : 'tag-pending'}"><g:message code="${stepStates != null ? stepStates.exemption.i18nKey : 'request.step.state.uncomplete'}" /></span>
    
          <g:message code="mcr.step.exemption.label" />
            
          </em></a>
        </li>    
  

  
        <g:if test="${!documentTypes.isEmpty()}">
  
        <li class="${currentStep == 'document' ? 'selected' : ''}">
          <a href="${createLink(controller:'frontofficeRequestCreation', params:['label':requestTypeLabel,'currentStep':'document'])}">
          <em>
          <span class="tag-state ${stepStates!= null ? stepStates.document.cssClass : 'tag-pending'}"><g:message code="${stepStates != null ? stepStates.document.i18nKey : 'request.step.state.uncomplete'}" /></span>
    
          <g:message code="request.step.document.label" />
            
          </em></a>
        </li>    
  
        </g:if>
  

  
        <li class="${currentStep == 'validation' ? 'selected' : ''}">
          <a href="${createLink(controller:'frontofficeRequestCreation', params:['label':requestTypeLabel,'currentStep':'validation'])}">
          <em>
          <span class="tag-state ${stepStates!= null ? stepStates.validation.cssClass : 'tag-pending'}"><g:message code="${stepStates != null ? stepStates.validation.i18nKey : 'request.step.state.uncomplete'}" /></span>
    
          <strong>
            <g:message code="request.step.validation.label" /> *
          </strong>
            
          </em></a>
        </li>    
  

		 </ul>

     <div class="yui-content">
      <g:set var="requestTypeInfo">
        {"label": "${requestTypeLabel}"
          ,"steps": [  "census-required",  "parentage-required",  "situation-required",  "exemption",  "document",  "validation-required"  ]
        }
      </g:set>
      <g:set var="requestTypeInfo" value="${requestTypeInfo.encodeAsHTML()}" scope="request" />
       <g:set var="requestTypeInfo" value="${requestTypeInfo.encodeAsHTML()}" scope="request" />
       <g:set var="firstStep" value="requester" />
       <g:set var="currentStep" value="${currentStep == 'firstStep' ? firstStep : currentStep}" scope="request"/>
       <g:set var="requestTypeLabel" value="${requestTypeLabel}" />
       <g:set var="requestTypeAcronym" value="mcr" />
       <g:render template="/frontofficeRequestType/step" /> 
     </div><!-- end yui-content -->
    </div><!-- end requestTabView -->

  </body>
</html>
