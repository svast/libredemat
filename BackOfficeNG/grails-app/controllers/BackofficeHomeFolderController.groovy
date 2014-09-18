import com.google.gson.JsonObject
import grails.converters.JSON
import java.io.IOException
import java.util.ArrayList
import java.util.Collections

import org.libredemat.schema.ximport.HomeFolderImportDocument
import org.libredemat.service.users.IHomeFolderDocumentService
import org.libredemat.service.users.IUserService
import org.libredemat.service.users.IUserSearchService
import org.libredemat.service.users.IUserWorkflowService
import org.libredemat.service.users.IUserSecurityService
import org.libredemat.service.users.IUserDeduplicationService
import org.libredemat.util.Critere
import org.libredemat.business.users.*
import org.libredemat.business.QoS
import org.libredemat.security.SecurityContext
import org.libredemat.service.request.IRequestSearchService
import org.libredemat.service.payment.IPaymentService
import org.libredemat.service.users.IMeansOfContactService
import org.libredemat.service.users.external.IExternalHomeFolderService
import org.libredemat.service.request.IRequestTypeService
import org.libredemat.business.payment.Payment
import org.libredemat.service.document.IDocumentTypeService
import org.libredemat.security.PermissionException
import org.libredemat.security.annotation.ContextPrivilege

import org.libredemat.exception.CvqModelException
import org.libredemat.exception.CvqValidationException

import org.libredemat.business.request.Request
import org.libredemat.business.request.RequestState
import org.libredemat.util.Critere

import org.apache.xmlbeans.XmlError
import org.apache.xmlbeans.XmlException
import org.apache.xmlbeans.XmlOptions

import org.libredemat.service.request.ICategoryService

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

class BackofficeHomeFolderController {

    IExternalHomeFolderService externalHomeFolderService
    IUserService userService
    IUserSearchService userSearchService
    IUserWorkflowService userWorkflowService
    IUserDeduplicationService userDeduplicationService
    IRequestSearchService requestSearchService
    IPaymentService paymentService
    IMeansOfContactService meansOfContactService
    IUserSecurityService userSecurityService
    IDocumentTypeService documentTypeService
    IHomeFolderDocumentService homeFolderDocumentService
    IRequestTypeService requestTypeService
    ICategoryService categoryService

    def translationService
    def homeFolderAdaptorService
    def requestAdaptorService
    def requestTypeAdaptorService
    def individualAdaptorService

    def defaultAction = 'search'
    def defaultMax = 15
    def subMenuEntries

    def beforeInterceptor = {
        session["currentMenu"] = "users"
        if (SecurityContext.currentCredentialBean.hasSiteAdminRole()) {
            subMenuEntries = ["userAdmin.index", "userSecurity.index", "homeFolder.meansOfContact", "homeFolder.importHomeFolders"]
        } else {
            if (userSecurityService.can(SecurityContext.getCurrentAgent(), ContextPrivilege.MANAGE))
                subMenuEntries = ["homeFolder.search", "homeFolder.configure", "homeFolder.create"]
            else
                if (userSecurityService.can(SecurityContext.getCurrentAgent(), ContextPrivilege.WRITE))
                    subMenuEntries = ["homeFolder.search", "homeFolder.create"]
                else
                    subMenuEntries = ["homeFolder.search"]
        }
    }

    def help = {}
    
    def search = {
        def state = [:], records = [], count = 0
        if (params.pageState) state = JSON.parse(params.pageState)
        
        if(!request.get) {
            records = this.doSearch(state)
            count = userSearchService.getCount(this.prepareCriterias(state), true)
        }
        
        return ([
            'agentCanWrite': userSecurityService.canWrite(SecurityContext.currentAgent.id),
            'state': state,
            'records': records,
            'count' : count,
            'max': this.defaultMax,
            'homeFolderStates': this.buildHomeFolderStateFilter(),
            'currentSiteName': SecurityContext.currentSite.name,
            'homeFolderStatus' : this.buildHomeFolderStatusFilter(),
            'pageState' : (new JSON(state)).toString().encodeAsHTML(),
            'offset' : params.currentOffset ? params.currentOffset : 0,
            'subMenuEntries': subMenuEntries
        ]);
    }
    
    def details = {
        def homeFolder = userSearchService.getHomeFolderById(Long.parseLong(params.id))
        if (homeFolder.temporary)
            render(text: "", status: 403)
        def adults, children
        if (params.viewArchived != null) {
            adults = userSearchService.getAdults(homeFolder.id, UserState.allUserStates)
            children = userSearchService.getChildren(homeFolder.id, UserState.allUserStates)
        } else {
            adults = userSearchService.getAdults(homeFolder.id)
            children = userSearchService.getChildren(homeFolder.id)
        }

        def unarchivableIndividuals = []
            Critere critere = new Critere(Request.SEARCH_BY_HOME_FOLDER_ID, homeFolder.id, Critere.EQUALS)
            Set<Critere> criteria = new HashSet<Critere>();
            criteria.add(critere)
            List<Request> homeFolderRequests = requestSearchService.get(criteria, null, null, -1, 0, false)
            for (Request request : homeFolderRequests) {
                if (request.state != RequestState.ARCHIVED && request.subjectId != null)
                    unarchivableIndividuals.add(request.subjectId)
            }

        def result = [:]
        result.homeFolder = homeFolder
        result.homeFolderResponsible = userSearchService.getHomeFolderResponsible(homeFolder.id)
        if (result.homeFolderResponsible.duplicateAlert) {
            result.homeFolderDuplicates = [:]
            def duplicates = JSON.parse(result.homeFolderResponsible.duplicateData)
            duplicates.each { homeFolderId, values ->
                result.homeFolderDuplicates[homeFolderId] = [:]
                values.each { 
                    if (it.key == 'rank')
                        result.homeFolderDuplicates[homeFolderId][it.key] = Long.valueOf(it.value)
                    else
                        result.homeFolderDuplicates[homeFolderId][it.key] = it.value
                }
                result.homeFolderDuplicates[homeFolderId]['otherDuplicates'] = 
                    userDeduplicationService.getHomeFolderDuplicates(homeFolder.id, Long.valueOf(homeFolderId))
                result.homeFolderDuplicates[homeFolderId]['otherDuplicates'].remove(result.homeFolderResponsible.fullName)
                result.homeFolderDuplicates[homeFolderId]['duplicateResponsibleData'] = userSearchService.getHomeFolderResponsible(Long.valueOf(homeFolderId))
            }
        }
        result.adults = adults.findAll{it.id != result.homeFolderResponsible.id}
        result.children = children
        result.homeFolderState = homeFolder.state.toString().toLowerCase()
        result.homeFolderStatus = homeFolder.enabled ? 'enable' : 'disable'
        def isValidable = false
        def agentCanWrite = userSecurityService.can(SecurityContext.getCurrentAgent(), ContextPrivilege.WRITE)
        if ((homeFolder.state.equals(UserState.NEW) || homeFolder.state.equals(UserState.MODIFIED)) &&
            (agentCanWrite)) {
            isValidable=true
        }
        result.isValidable=isValidable

        result.responsibles = [:]
        for(Child child : result.children)
            result.responsibles.put(child.id, userSearchService.listBySubjectRoles(child.id, RoleType.childRoleTypes))

        result.homeMappings = externalHomeFolderService.getHomeFolderMappings(Long.valueOf(params.id))

        result.agentCanWrite = agentCanWrite
        result.informationSheetDisplayed = SecurityContext.getCurrentConfigurationBean().isInformationSheetDisplayed()

        result.groups = requestTypeAdaptorService.getActiveRequestTypeByDisplayGroup(homeFolder)

        if(!SecurityContext.getCurrentConfigurationBean().getExternalApplicationProperty("booker.url").isEmpty()
          && categoryService.hasWriteProfile(SecurityContext.getCurrentAgent())
          && userSearchService.hasExternalLibredematId(result.homeFolderResponsible.id)) {
            result.groups["Other"] = ['label': "Autre",requests:[['label':"Planning",'id':'booking']]]
        }

        result.unarchivableIndividuals = unarchivableIndividuals

        result.canResetPassword = isAllowedToResetPassword(homeFolder, result.homeFolderResponsible)

        return result
    }

    def create = {
        if (request.post) {
            def adult = new Adult()
            DataBindingUtils.initBind(adult, params)
            bind(adult)
            def invalidFields = userService.validate(adult, false)
            if (!invalidFields.isEmpty()) {
                session.doRollback = true
                render (['invalidFields': invalidFields] as JSON)
                return false
            }
            userWorkflowService.create(adult, false, null)
            render (['id' : adult.homeFolder.id] as JSON)
            return false
        }
        return (['subMenuEntries': subMenuEntries,
                 'defaultEmail': SecurityContext.getCurrentConfigurationBean().getDefaultEmail()])
    }

    def adult = { 
        def adult, template
        def mode = params.mode 
        if (!params.id) {
            adult =  new Adult()
            template = 'adult'
            flash.homeFolderId = params.homeFolderId
        } else {
            adult = userSearchService.getAdultById(Long.valueOf(params.id))
            template = params.template ? params.template : 'adult'
        }

        if (request.post && !adult.id) {
            mode = 'static'
            DataBindingUtils.initBind(adult, params)
            bind(adult)
            def homefolder = userSearchService.getHomeFolderById(Long.valueOf(params.homeFolderId))
            adult.address = homefolder.address
            def invalidFields = userService.validate(adult)
            if (!invalidFields.isEmpty()) {
                session.doRollback = true
                render (['invalidFields': invalidFields] as JSON)
                return false
            }
            userWorkflowService.add(homefolder, adult, false)
            render (['status': 'success', 'type':'adult', 'id': adult.id] as JSON)
            return false
        }
        render(template: mode + '/' + template, model:['adult': adult])
    }

    def child = {
        def child, template, homeFolderId
        def mode = params.mode
        if (!params.id) {
            child =  new Child()
            template = 'child'
            homeFolderId = Long.valueOf(params.homeFolderId)
            flash.homeFolderId = params.homeFolderId
        } else {
            child = userSearchService.getChildById(Long.valueOf(params.id))
            template = params.template ? params.template : 'child'
            homeFolderId = child.homeFolder.id
        }
        if (request.post && !child.id) {
            mode = 'static'
            bind(child)
            userWorkflowService.add(userSearchService.getHomeFolderById(homeFolderId), child)
            params.roles.each {
                if (it.value instanceof GrailsParameterMap && it.value.owner != '' && it.value.type != '') {
                    userWorkflowService.link(
                        userSearchService.getById(Long.valueOf(it.value.owner)),
                        child, [RoleType.valueOf(it.value.type)])
                }
            }
            def invalidFields = userService.validate(child)
            if (!invalidFields.isEmpty()) {
                session.doRollback = true
                if (invalidFields.contains('legalResponsibles')) {
                    userSearchService.getAdults(child.homeFolder.id).eachWithIndex { adult, index ->
                        invalidFields.add("roles.${index}.type")
                    }
                }
                render (['invalidFields': invalidFields] as JSON)
                return false
            }
            render (['status': 'success', 'type':'child', 'id': child.id] as JSON)
            return false
        }
        def models = ['child': child]
        models['adults'] = userSearchService.getAdults(homeFolderId).findAll{ it.state != UserState.ARCHIVED }
        if (child.id) {
            models['roleOwners'] = userSearchService.listBySubjectRoles(child.id, RoleType.childRoleTypes)
        }
        render(template: mode + '/' + template, model: models)
    }

    def removeIndividual = {
        def user = userSearchService.getById(params.long("id"))
        try {
            userWorkflowService.changeState(user, UserState.ARCHIVED)
            render(['status':'success', 'message':message(code:'homeFolder.message.individualRemoveSuccess')] as JSON)
        } catch (CvqModelException cme) {
            render(['status':'error', 'message':cme.message] as JSON)
        }
    }

    def resetPassword = {
        def adult = userSearchService.getHomeFolderResponsible(params.long("id"))
        userWorkflowService.launchResetPasswordProcess(adult)
        render(['status': 'success', 'message': message(code:'homeFolder.action.resetPwd.btnFeedback')] as JSON)
    }

    def state = {
        def user
        try {
            user = userSearchService.getById(params.long("id"))
        } catch (CvqObjectNotFoundException) {
            user = userSearchService.getHomeFolderById(params.long("id"))
        }
        def mode = request.get ? params.mode : "static"
        if (request.post) {
            userWorkflowService.changeState(user, UserState.forString(params.state))
        }
        render(template : mode + "/state", model : [
            "user" : user, "states" : userWorkflowService.getPossibleTransitions(user)])
    }
    
    def validateHomeFolder = {
            def homeFolder = userSearchService.getHomeFolderById(Long.parseLong(params.id));
            userWorkflowService.validateHomeFolder(homeFolder);
            redirect(action: 'details',id: params.id)
    }

    def processDuplicate = {
        if (params.ignore) {
            userDeduplicationService.removeDuplicate(params.long('homeFolderId'), params.long('targetHomeFolderId'))
        } else if (params.merge) {
            userDeduplicationService.mergeDuplicate(params.long('homeFolderId'), params.long('targetHomeFolderId'))
        }
        redirect(action:'details', params:['id': params.homeFolderId])
    }

    def realizeRequest = {
      def requestTypeLabel = params.requestTypeId != null && !params.requestTypeId.isEmpty() ?
                               params.requestTypeId.equals("booking") ?
                                 "booking" : requestTypeService.getRequestTypeById(Long.valueOf(params.requestTypeId)).label : null

      session.startAgentSpoofEcitizenProcess = true
      redirect(controller: 'frontofficeHome', action:'loginAgent', params:['id' : params.id, 'requestTypeLabel' : requestTypeLabel])
    }

    def address = {
        def adult = userSearchService.getAdultById(params.long("id"))
        def mode = request.get ? params.mode : "static"
        if (request.post) {
            try {
                def temp = new Address()
                bind(temp)
                individualAdaptorService.historize(
                    adult, adult.address, temp, "address",
                    ["additionalDeliveryInformation", "additionalGeographicalInformation", "city",
                        "cityInseeCode", "countryName", "placeNameOrService", "postalCode",
                        "streetMatriculation", "streetName", "streetNumber", "streetRivoliCode"])
            } catch (CvqValidationException e) {
                session.doRollback = true
                def invalidFields = []
                e.invalidFields.each{ invalidFields.add(it = it.replace('address.',''))}
                render (['invalidFields': invalidFields] as JSON)
                return false
            }
        }
        render(template : mode + "/address", model : ["user" : adult])
    }

    def contact = {
        def adult = userSearchService.getAdultById(params.long("id"))
        def mode = request.get ? params.mode : "static"
        if (request.post) {
            try {
                def temp = new Adult()
                bind(temp)
                individualAdaptorService.historize(
                    adult, adult, temp, "contact", ["email", "homePhone", "mobilePhone", "officePhone"])
            } catch (CvqValidationException e) {
                session.doRollback = true
                render (['invalidFields': e.invalidFields] as JSON)
                return false
            }
        }
        render(template : mode + "/contact", model : ["adult" : adult])
    }

    def identity = {
        def individual = userSearchService.getById(params.long("id"))
        def mode = request.get ? params.mode : "static"
        if (request.post) {
            try {
                def temp = individual instanceof Adult ? new Adult() : new Child()
                bind(temp)
                individualAdaptorService.historize(
                    individual, individual, temp, "identity",
                    individual instanceof Adult ?
                        ["title", "familyStatus", "lastName", "maidenName", "firstName", "firstName2", "firstName3", "profession", "cfbn"] :
                        ["born", "lastName", "firstName", "firstName2", "firstName3", "sex", "birthDate", "birthPostalCode", "birthCity", "birthCountry"])
            } catch (CvqValidationException e) {
                session.doRollback = true 
                render (['invalidFields': e.invalidFields] as JSON)
                return false
            }
        }
        render(template : mode + "/" + individual.class.simpleName.toLowerCase() + "Identity",
            model : ["individual" : individual])
    }
    
    
    def mapping = {
    	if (request.post) {
    	  externalHomeFolderService.setExternalId(params.externalServiceLabel, Long.valueOf(params.homeFolderId), Long.valueOf(params.id), params.externalId)
    	}
        def individual = userSearchService.getById(params.long("id"))
        def mapping = externalHomeFolderService.getIndividualMapping(individual, params.externalServiceLabel)
        def mode = request.get ? params.mode : "static"
        render(template : mode + "/mapping", model : ["mapping" : mapping])
    }

    def responsibles = {
        def child = userSearchService.getChildById(Long.valueOf(params.id))
        def mode = request.get ? params.mode : "static"
        if (request.post) {
            params.roles.each {
                if (it.value instanceof GrailsParameterMap && it.value.owner != '') {
                    def owner = userSearchService.getById(Long.valueOf(it.value.owner))
                    if (it.value.type == '')
                        userWorkflowService.unlink(owner,child)
                    else
                        userWorkflowService.link(owner,child, [RoleType.forString(it.value.type)])
                }
            }
            if (!userService.validate(child).isEmpty())  {
                session.doRollback = true
                def errors = []
                userSearchService.getAdults(child.homeFolder.id).eachWithIndex { adult, index ->
                    errors.add("roles.${index}.type")
                }
                render (['invalidFields': errors] as JSON)
                return false
            }
        }
        def model = [
            "child" : child,
            "adults" : userSearchService.getAdults(child.homeFolder.id),
            "roleOwners" : mode == 'static' ? 
                userSearchService.listBySubjectRoles(child.id, RoleType.childRoleTypes)
                : homeFolderAdaptorService.roleOwners(child.id)
        ]
        render(template : mode + "/responsibles", model : model)
    }

    def actions = {
        def list = new ArrayList(userSearchService.getHomeFolderById(Long.valueOf(params.id)).actions)
        Collections.reverse(list)
        return ["actions" : homeFolderAdaptorService.prepareActions(list),
                "archived" : params.boolean("archived")]
    }
    
    def currentHomeFolderState = {
        def result = [:];
        def homeFolder = userSearchService.getHomeFolderById(Long.parseLong(params.id));
        result.homeFolderState = homeFolder.state.toString().toLowerCase();
        def isValidable=false;
        if(homeFolder.state.equals(UserState.NEW) || homeFolder.state.equals(UserState.MODIFIED)) {
            isValidable=true;
        }
        result.isValidable=isValidable;
        result.homeFolder=homeFolder;
        return result;
    }

    def currentResetPasswordBox = {
        def homeFolder = userSearchService.getHomeFolderById(Long.parseLong(params.id))
        def responsible = userSearchService.getHomeFolderResponsible(homeFolder.id)
        def model = [
            canResetPassword : isAllowedToResetPassword(homeFolder, responsible)
        ]
        render(template: "currentResetPasswordBox", model: model)
    }

    // TODO : move in request module
    def requests = {
        def result = [requests:[]]
        def homeFolderRequests =
            requestSearchService.getByHomeFolderId(Long.valueOf(params.id), false);

        homeFolderRequests.each {
          def record = requestAdaptorService.prepareRecordForSummaryView(it)
          result.requests.add(record)
        }
        return result
    }
    
    // TODO : move in payment module
    def payments = {
        def result = [payments:[]]
        
        for (Payment payment : this.paymentService.getByHomeFolder(Long.parseLong(params.id))) {
            result.payments.add([
                'id' : payment.id,
                'initializationDate' : payment.initializationDate,
                'state' : payment.state.toString(),
                'bankReference' : payment.bankReference,
                'amount' : payment.amount,
                'paymentMode' : message(code:"payment.mode."+payment.paymentMode.toString().toLowerCase())
            ])
        }
        
        return result
    }

    def meansOfContact = {
        return ["subMenuEntries" : subMenuEntries]
    }

    def moCs = {
        render(template : "moCs",
               model : ["moCs" : meansOfContactService.availableMeansOfContact])
    }

    def moC = {
        if (request.post) {
            def moc = meansOfContactService.getById(Long.valueOf(params.id))
            if (params.enabled == 'true') meansOfContactService.disableMeansOfContact(moc)
            else if (params.enabled == 'false') meansOfContactService.enableMeansOfContact(moc)
            render ([status : "success", success_msg : message(code : "message.updateDone")] as JSON)
        }
    }

    def importHomeFolders = {
        if (request.get) {
            render(view : "import", model : [
                "subMenuEntries" : subMenuEntries,
                "hasAdminEmail" : SecurityContext.currentSite.adminEmail
            ])
            return false
        } else if (request.post) {
            if (!SecurityContext.currentSite.adminEmail) {
                render (new JSON([status : "error",
                    error_msg : message(code : "homeFolder.import.error.noAdminEmail")]).toString())
                return false
            }
            def file = request.getFile("document")
            def doc
            try {
                doc = HomeFolderImportDocument.Factory.parse(file.inputStream)
                // first validate the data
                List<XmlError> errors = new ArrayList<XmlError>()
                XmlOptions options = new XmlOptions()
                options.setErrorListener(errors)
                doc.validate(options)
                if (!errors.isEmpty()) {
                    log.error "Got validation errors for current file"
                    for (XmlError error : errors) {
                        log.error "Message: ${error.getMessage()}"
                        log.error "Location of invalid XML: ${error.getCursorLocation().xmlText()}"
                    }
                    render (new JSON([status : "error",
                    error_msg : message(code : "homeFolder.import.error.invalidFile")]).toString())
                    return false
                }
            } catch (XmlException e) {
                render (new JSON([status : "error",
                    error_msg : message(code : "homeFolder.import.error.invalidFile")]).toString())
                return false
            } catch (IOException e) {
                render (new JSON([status : "error",
                    error_msg : message(code : "homeFolder.import.error.invalidFile")]).toString())
                return false
            }
            userWorkflowService.importHomeFolders(doc)
            render (new JSON([status : "ok", success_msg :
                message(code : "homeFolder.import.message.started",
                    args : [SecurityContext.currentSite.adminEmail])]).toString())
            return false
        }
    }

    protected List doSearch(state) {
        return userSearchService.get(prepareCriterias(state), prepareSort(state), defaultMax,
            params.currentOffset ? Integer.parseInt(params.currentOffset) : 0, true)
    }
    
    protected Set<Critere> prepareCriterias(state) {
        def mapper =[:]
        mapper.lastName = Critere.STARTSWITH
        mapper.firstName = Critere.STARTSWITH
        mapper.homeFolderId = Critere.EQUALS
        mapper.homeFolderState = Critere.EQUALS 
        mapper.isHomeFolderResponsible = Critere.EQUALS
        mapper.isDuplicateAlert = Critere.EQUALS
        mapper.userState = Critere.EQUALS
        Set<Critere> criterias = new LinkedHashSet<Critere>()
        
        for(String key : state.keySet()){
            if(mapper.keySet().contains(key) && state."$key") {
                Critere criteria = new Critere()
                criteria.setAttribut(key)
                criteria.setComparatif(mapper[key].toString())
                if (key.equals('homeFolderId'))
                    criteria.setValue(LongUtils.stringToLong(state[key]))
                else
                    criteria.setValue(state[key])
                criterias.add(criteria)
            }
        }
        Critere criteria = new Critere()
        criteria.setAttribut(Individual.SEARCH_IS_TEMPORARY)
        criteria.setComparatif(Critere.EQUALS)
        criteria.setValue(false)
        criterias.add(criteria)

        if (!UserState.ARCHIVED.toString().equals(state['homeFolderState']) &&
            !state['userState']) {
          Critere notArchived = new Critere()
          notArchived.setAttribut(Individual.SEARCH_BY_USER_STATE)
          notArchived.setComparatif(Critere.NEQUALS)
          notArchived.setValue(UserState.ARCHIVED.toString())
          criterias.add(notArchived)
        }

        return criterias
    }
    
    protected Map<String,String> prepareSort(state) {
        if(!state?.orderBy) state.orderBy = 'creationDate'
        Map<String,String> result = new HashMap<String,String>();
        def orderBy = 'asc'
        if (state.orderBy.contains('Date'))
            orderBy = 'desc'
        result.put("individual." + state.orderBy, orderBy)
        return result
    }
    
    protected List buildHomeFolderStatusFilter() {
        def result = []
        result.add(['name':'true','i18nKey': message(code:'property.active')])
        result.add(['name':'false','i18nKey':message(code:'property.inactive')])
        return result
    }
    
    protected List buildHomeFolderStateFilter() {
        def result = []

        for(UserState state : UserState.filtersStates) {
            result.add([
                'name':state.toString(),
                'i18nKey': message(code:"user.state.${state.toString().toLowerCase()}")
            ])
        }
        return result;
    }

    def listTasks = {
        def state = [:]

        // TODO deal with pagination
        render(view : 'search', model: [
            'state': state,
            'records' : userSearchService.listTasks(QoS.forString(params.qoS), 0),
            'count' : userSearchService.countTasks(QoS.forString(params.qoS)),
            'max': 100,
            'homeFolderStates': buildHomeFolderStateFilter(),
            'currentSiteName': SecurityContext.currentSite.name,
            'homeFolderStatus' : buildHomeFolderStatusFilter(),
            'pageState' : (new JSON(state)).toString().encodeAsHTML(),
            'offset' : 0,
            'subMenuEntries' : subMenuEntries
        ]);
    }

    def listDuplicates = {
        def state = ['isDuplicateAlert': true]

        // TODO deal with pagination
        render(view : 'search', model: [
            'state': state,
            'records' : userSearchService.listDuplicates(-1),
            'count' : userSearchService.countDuplicates(),
            'max': 100,
            'homeFolderStates': buildHomeFolderStateFilter(),
            'currentSiteName': SecurityContext.currentSite.name,
            'homeFolderStatus' : buildHomeFolderStatusFilter(),
            'pageState' : (new JSON(state)).toString().encodeAsHTML(),
            'offset' : 0,
            'subMenuEntries' : subMenuEntries
        ]);
    }
    /**
     * Home folders configuration:
     * Enable/disable home folder creation without starting a request.
     * Set document types wished at home folder creation time.
     */
    def configure = {
        try {
            def bool = userService.homeFolderIndependentCreationEnabled();
            return (['subMenuEntries': subMenuEntries, 'independentCreationEnabled': bool])
        } catch (PermissionException pe) {
            render(text: message(code: pe.message), status: 403)
        }
    }

    def setIndependentCreation = {
        try {
            if (params.independentCreation == "1")
                userService.enableHomeFolderIndependentCreation()
            else
                userService.disableHomeFolderIndependentCreation()
            render ([status:"success", success_msg:message(code:"message.updateDone")] as JSON)
        } catch (PermissionException pe) {
            render(text: message(code: pe.message), status: 403)
        }
    }

    def documentList = {
        def list = []
        def wishedDocumentTypes = homeFolderDocumentService.wishedDocumentTypes()
        documentTypeService.getAllDocumentTypes().each{ d ->
            list.add([
                'documentId' : d.id,
                'name' : message(code:LibredematUtils.adaptDocumentTypeName(d.name)),
                'bound' : wishedDocumentTypes.contains(d),
                'class' : wishedDocumentTypes.contains(d) ? '' : 'notBelong'
            ])
        }
        list = list.sort{ it.name }
        render(template:"documentList", model:["documents":list])
    }

    def associateDocument = {
        try {
            homeFolderDocumentService.wish(Long.valueOf(params.dtid))
            render([status:"ok", success_msg:message(code:"message.updateDone")] as JSON)
        } catch (PermissionException pe) {
            render(text: message(code: pe.message), status: 403)
        }
    }

    def unassociateDocument = {
        try {
            homeFolderDocumentService.unwish(Long.valueOf(params.dtid))
            render([status:"ok", success_msg:message(code:"message.updateDone")] as JSON)
        } catch (PermissionException pe) {
            render(text: message(code: pe.message), status: 403)
        }
    }


    /*def note = {
        def hf = userSearchService.getHomeFolderById(Long.valueOf(params.id))
        render(template: 'note', model: ['hf': hf])
    }

    def saveNote = {
        def hf = userSearchService.getHomeFolderById(Long.valueOf(params.id))
        if (hf && params.note) {
            def action = new UserAction(UserAction.Type.INTERNAL_NOTE, hf.id)
            action.userId = SecurityContext.getCurrentUserId()
            action.note = params.note
            hf.actions.add(action)
            homeFolderDAO.update(hf)

            render(text:['message':message(code:'homeFolder.note.added')] as JSON)
        } else {
            render(status:403, text:['message':message(code:'homeFolder.note.add.failed')] as JSON)
        }
    }*/

    private isAllowedToResetPassword(homeFolder, homeFolderResponsible) {
        return userService.hasValidEmail(homeFolderResponsible) && homeFolder.state != UserState.ARCHIVED
    }

}
