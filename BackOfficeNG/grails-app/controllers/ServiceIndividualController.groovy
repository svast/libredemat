import fr.cg95.cvq.business.users.external.IndividualMapping;
import fr.cg95.cvq.security.SecurityContext;
import fr.cg95.cvq.service.users.IUserSearchService;
import fr.cg95.cvq.service.users.external.IExternalHomeFolderService;
import fr.cg95.cvq.oauth2.InsufficientScopeException;
import fr.cg95.cvq.exception.CvqObjectNotFoundException
import fr.cg95.cvq.service.authority.IAgentService

import grails.converters.JSON

class ServiceIndividualController {

    IUserSearchService userSearchService
    IExternalHomeFolderService externalHomeFolderService
    IAgentService agentService

    def beforeInterceptor = {
        def token = request.getAttribute("accessToken")
        if (!token?.sufficientScope("individual")) {
            forward(controller: 'OAuth2', action: 'invalidScope')
            return false;
        }
    }

    def defaultAction = 'userInfo'

    def userInfo = {
        def token = request.getAttribute("accessToken")
        def user

        try {
          if(params.eCitizenId != null && params.eCitizenId != "") {
            def agent = agentService.getByLogin(token.resourceOwnerName)
            if(agent) {
                user = userSearchService.getById(params.eCitizenId as Long)
            } else {
                render(status: 403)
            }
          } else {
            user = userSearchService.getByLogin(token.resourceOwnerName)
          }
        } catch (CvqObjectNotFoundException ex) {
          render(status: 404)
        }

        def individualMapping = externalHomeFolderService.
                getIndividualMapping(user, SecurityContext.getCurrentExternalService())
        if (individualMapping != null) {
            user.setExternalCapDematId(individualMapping.getExternalCapDematId())
            user.setExternalId(individualMapping.getExternalId())
        }

        def result = [externalCapdematId:user.externalCapDematId, externalId:user.externalId,
               email: user.email, firstname: user.firstName, lastname: user.lastName]

        if(token.scope.contains("homefolderId")) {
          result += [homefolderId: user.getHomeFolder().getId()]
        }

        render (result as JSON)
    }

}
