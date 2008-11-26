<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <title>CapDémat Lab - FrontOfficeNG</title>
    <g:render template="/shared/fong_resources" />
    <g:layoutHead />
  </head>
  <body>
  
   <div id="doc4" class="yui-t4 yui-skin-fong">
   
     <!-- header -->
     <div id="hd">
       <h1>
         <img src="${createLinkTo(dir:'fong/images/',file:'logo_zen_200.png')}" alt="Zenexity Lab" />
         <span>CapDémat - FrontOffice NG</span>
       </h1>
       <map id="hd-menu">
        <a href="#">se déconnecter</a> | 
        <a href="#">aide</a> |
        <a href="#">f.a.q</a> 
       </map>
     </div>
     
       <!-- main navifgation bar -->
     <div id="bd-menu">
       <a href="${createLink(controller:'frontofficeHome', action: 'index')}" class="${menu.current(elem:'home')}" >
          <span>Accueil</span>
       </a>
       <a href="${createLink(controller:'frontofficeHome',action: 'persons')}" 
          class="${menu.current(elem:'persons')}" >
          <span>Personnes</span>
       </a>
       <a href="${createLink(controller:'frontofficeRequest')}" 
         class="${menu.current(elem:'requests')}">
         <span>Demandes</span>
       </a>
       <a href="${createLink(controller:'frontofficeHome',action: 'documents')}" 
          class="${menu.current(elem:'documents')}">
          <span>Documents</span>
       </a>
       <a href="${createLink(controller:'frontofficeHome',action: 'services')}" 
          class="${menu.current(elem:'services')}" >
          <span>Services</span>
       </a>
       <a href="${createLink(controller:'frontofficeHome',action: 'activities')}" 
          class="${menu.current(elem:'activities')}">
          <span>Activités</span>
       </a>
       <a href="${createLink(controller:'frontofficeHome',action: 'payments')}" 
          class="${menu.current(elem:'payments')}" >
          <span>Paiements</span>
       </a>
     </div>
     
     <!-- header -->
     <div id="bd">
     <div id="bd-2">
       <g:layoutBody />
     </div> <!-- end bd-2  -->
     </div> <!-- end bd  -->
     
   <!-- footer -->
   <div id="ft">
     <a href="http://www.zenexity.fr">Conçu et réalisé par Zenexity</a>
   </div>
   
   </div> <!-- end doc -->
 
  </body>
</html>


