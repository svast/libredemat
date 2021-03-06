import org.libredemat.business.authority.LocalAuthorityResource
import org.libredemat.business.request.Request
import org.libredemat.business.request.RequestState
import org.libredemat.security.SecurityContext
import org.libredemat.service.authority.ILocalAuthorityRegistry
import org.libredemat.service.request.IRequestSearchService
import org.libredemat.service.request.IRequestServiceRegistry
import org.libredemat.service.request.IRequestWorkflowService
import org.libredemat.service.users.IUserSearchService
import org.libredemat.util.Critere
import java.util.regex.Pattern

class FrontofficeRequestTypeController {

    IUserSearchService userSearchService
    ILocalAuthorityRegistry localAuthorityRegistry
    IRequestSearchService requestSearchService
    IRequestWorkflowService requestWorkflowService
    IRequestServiceRegistry requestServiceRegistry
    def requestAdaptorService
    def requestTypeService
    def requestTypeAdaptorService
    def documentAdaptorService

    def index = {
        def adult = userSearchService.getAdultById(session.currentEcitizenId)
        def groups = []
        use(SplitMap) {
            groups = requestTypeAdaptorService.getDisplayGroups(adult?.homeFolder).split()
        }
        return ['groups':groups]
    }

    def start = {
        def label = params.id != null ? params.id : params.requestTypeLabel
        if (label == null) {
            redirect(uri: '/frontoffice/requestType')
            return false
        }

        if (SecurityContext.currentEcitizen == null) {
            def callbackURI = request.forwardURI+(request.queryString ? "?"+request.queryString : "")
            redirect(controller : "frontofficeRequestType", action : "login", params : ["requestTypeLabel" : label, "callback" : callbackURI] )
            return false
        }
        def requestType = requestTypeService.getRequestTypeByLabel(label)
        def intro = localAuthorityRegistry.getFileContent(
            localAuthorityRegistry.getLocalAuthorityResourceFile(
                LocalAuthorityResource.Type.HTML,
                "request/" + LibredematUtils.requestTypeLabelAsDir(label) + "/introduction",
                false))
        def lastRequests = requestWorkflowService.getRenewableRequests(label)
        def seasons = requestTypeService.isOfRegistrationKind(requestType.id) ?
            requestTypeService.getOpenSeasons(requestType) : []
        def criterias = [] as Set
        def critere = new Critere()
        critere.comparatif = Critere.EQUALS
        critere.attribut = Request.SEARCH_BY_REQUEST_TYPE_LABEL
        critere.value = label
        criterias.add(critere)
        critere = new Critere()
        critere.comparatif = Critere.EQUALS
        critere.attribut = Request.SEARCH_BY_STATE
        critere.value = RequestState.DRAFT
        criterias.add(critere)
        def drafts = requestSearchService.get(criterias, null, null, 0, 0, false)
        params.put('label', label)

        def pat = Pattern.compile("(.*\\[\\d+\\])\\..*")
        def paramsToDel = []
        for( i in params.keySet())
        {
          def matcher = pat.matcher(i)
          if(matcher.find() && !paramsToDel.contains(matcher.group(1)))
            paramsToDel.add(matcher.group(1))
        }
        for(i in paramsToDel)
        {
          params.remove(i)
        }
        if ( request.queryString != null && params.long("requestSeasonId")!=null)
        {
            redirect(controller : "frontofficeRequest", action : "create", params : params)
            return false
        }
        if (intro == null && lastRequests.isEmpty() && seasons.isEmpty() && drafts.isEmpty()) {
            redirect(controller : "frontofficeRequest", action : "create", params : ["label" : label])
            return false
        }
        return [
            "requestTypeLabel" : label,
            "documentTypes" : documentAdaptorService.getDocumentTypes(requestType.id),
            "acronym" : requestTypeAdaptorService.generateAcronym(label),
            "intro" : intro,
            "lastRequests" : lastRequests,
            "seasons" : seasons,
            "noSeason" : params.noSeason != null ? params.noSeason : false,
            "drafts" : requestAdaptorService.prepareRecords(["all" : drafts, "count" : drafts.size(), "records" : []]).records
        ]
    }

    def login = {
        return ["temporary" : requestServiceRegistry.getRequestService(params.requestTypeLabel)
            .supportUnregisteredCreation()]
    }
}
