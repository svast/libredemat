  <h3>
    <span class="tag-state tag-uncomplete"><g:message code="request.step.state.uncomplete"/></span>
    <g:message code="request.step.document.label" />
    <span><g:message code="request.step.document.desc" /></span>
  </h3>

  <g:message code="request.step.document.listHeader" /> :
  <ul>
    <g:each in="${documentTypes}" var="documentType">
      <li>
        <g:message code="${documentType.value}"/>
      </li>
    </g:each>
  </ul>
             
  <div class="error" id="documentRefFormErrors"> </div>
           
  <!-- Input submit-->
  <input type="button" id="submitDocumentRef" name="submitDocumentRef" 
         value="${message(code:'action.save')}" />



             