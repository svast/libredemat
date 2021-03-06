

  <g:set var="listSize" value="${rqt.otherFolders.size()}" />
  <h3>
    <a class="addListItem" id="add_otherFolders[${listSize}]"></a>
    <span><g:message code="hcar.property.otherFolders.label" /></span>
  </h3>
  <g:each var="it" in="${rqt.otherFolders.reverse()}" status="index">
  <div class="collection-action">
    <a class="deleteListItem" id="delete_otherFolders[${listSize - 1 - index}]"></a>
  </div>
  <dl class="condition-isOtherFolders-filled">
    
      <dt class="required"><g:message code="hcar.property.otherFolderName.label" /> * : </dt>
      <dd id="otherFolders[${listSize - 1 - index}].otherFolderName" class="action-editField validate- required-true i18n-hcar.property.otherFolderName maxLength-60" >
        <span>${it?.otherFolderName}</span>
      </dd>
    
      <dt class=""><g:message code="hcar.property.otherFolderNumber.label" />  : </dt>
      <dd id="otherFolders[${listSize - 1 - index}].otherFolderNumber" class="action-editField validate- i18n-hcar.property.otherFolderNumber maxLength-30" >
        <span>${it?.otherFolderNumber}</span>
      </dd>
    
      <dt class=""><g:message code="hcar.property.otherFolderDepartment.label" />  : </dt>
      <dd id="otherFolders[${listSize - 1 - index}].otherFolderDepartment" class="action-editField validate-departmentCode i18n-hcar.property.otherFolderDepartment maxLength-2" >
        <span>${it?.otherFolderDepartment}</span>
      </dd>
    
  </dl>
  </g:each>
