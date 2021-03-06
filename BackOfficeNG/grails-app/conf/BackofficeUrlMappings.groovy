class BackofficeUrlMappings {
  static mappings = {
    "/backoffice/requestAdmin/$action?/$id?" (controller : "backofficeRequestAdmin" )
    "/backoffice/requestArchives/$action?/$id?" (controller : "backofficeRequestArchives" )
    "/backoffice/referential/$action?/$id?" (controller : "backofficeReferential" )
    "/backoffice/requestType/$action?/$id?" (controller : "backofficeRequestType" )
    "/backoffice/email/states/$state" (controller : "backofficeEmail", action = [GET : "email", POST : "saveEmail"])
    "/backoffice/email/$action?/$id?" (controller : "backofficeEmail")
    "/backoffice/requestType/help/$id/step/$step" (controller : "backofficeRequestType", action = [GET : "helpFile", POST : "saveHelpFile"])
    "/backoffice/payment/$action?/$id?" (controller : "backofficePayment" )
    "/backoffice/category/$action?/$id?" (controller : "backofficeCategory" )
    "/backoffice/request/$action?/$id?" (controller : "backofficeRequest" )
    "/backoffice/requestInstruction/$action?/$id?" (controller : "backofficeRequestInstruction" )
    "/backoffice/localAuthority/$action?/$id?" (controller : "backofficeLocalAuthority" )
    "/backoffice/home/$action?/$id?" (controller : "backofficeHome" )
    "/backoffice/contact/$action?/$id?" (controller : "backofficeContact" )
    "/backoffice/login/$action?/$id?" (controller : "backofficeLogin" )
    "/backoffice/documentInstruction/$action?/$id?" (controller : "backofficeDocumentInstruction" )
    "/backoffice/homeFolder/$id/state" (controller : "backofficeHomeFolder", action : "state")
    "/backoffice/homeFolder/individual/$id/state" (controller : "backofficeHomeFolder", action : "state")
    "/backoffice/homeFolder/individual/$id/identity" (controller : "backofficeHomeFolder", action : "identity")
    "/backoffice/homeFolder/individual/$id/adultResponsibles" (controller : "backofficeHomeFolder", action : "adultResponsibles")
    "/backoffice/homeFolder/individual/$id/$externalServiceLabel/mapping" (controller : "backofficeHomeFolder", action : "mapping")
    "/backoffice/homeFolder/individual/$id/$externalServiceLabel/homeFolderMapping" (controller : "backofficeHomeFolder", action : "homeFolderMapping")
    "/backoffice/homeFolder/adult/$id/address" (controller : "backofficeHomeFolder", action : "address")
    "/backoffice/homeFolder/adult/$id/contact" (controller : "backofficeHomeFolder", action : "contact")
    "/backoffice/homeFolder/child/$id/responsibles" (controller : "backofficeHomeFolder", action : "responsibles")
    "/backoffice/homeFolder/child/$id/childInformationSheet" (controller : "backofficeHomeFolder", action : "childInformationSheet")
    "/backoffice/homeFolder/$action?/$id?" (controller : "backofficeHomeFolder" )
    "/backoffice/displayGroup/$action?/$id?" (controller : "backofficeDisplayGroup" )
    "/backoffice/agent/$action?/$id?" (controller : "backofficeAgent" )
    "/backoffice/statistic/$action?/$id?" (controller : "backofficeStatistic" )
    "/backoffice/externalApplication/$action?/$id?" (controller : "backofficeExternalApplication" )
    "/backoffice/external/$action?/$id?" (controller : "backofficeExternal" )
    "/backoffice/tasks/$action?/$id?" (controller : "backofficeTasks" )
    "/backoffice/ticketBooking/$action?/$id?" (controller : "backofficeTicketBooking" )
    "/backoffice/userSecurity/$action?/$id?" (controller : "backofficeUserSecurity" )
    "/backoffice/userDocumentInstruction/$action?/$id?" (controller : "backofficeUserDocumentInstruction" )
    "/backoffice/documentType/$action?/$id?" (controller : "backofficeDocumentType" )
    "/backoffice/userAdmin/$action?/$id?" (controller : "backofficeUserAdmin")
  }
}
