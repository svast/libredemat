/**
 * @description This file contains the Javascript module for the seasons management page
 * for request type of registration kind
 *
 * @author jsb@zenexity.fr
 */

zenexity.libredemat.tools.namespace('zenexity.libredemat.bong.localauthority');

(function(){

  var zct = zenexity.libredemat.tools;
  var zcc = zenexity.libredemat.common;
  var zcv = zenexity.libredemat.Validation;
  var zcb = zenexity.libredemat.bong;
  var zcbl = zenexity.libredemat.bong.localauthority;

  var yl = YAHOO.lang;
  var yu = YAHOO.util;
  var yud = yu.Dom;
  var yue = YAHOO.util.Event;
  var yus = YAHOO.util.Selector;
  var ylj = YAHOO.lang.JSON;

  zcbl.Aspect = function() {
    var content = {head : "Attention !", body : "Confirmez-vous le retour à l'ancienne version ?"};
    return {
      clickEv : undefined,
      init : function() {
        zcbl.Aspect.clickEv = new zct.Event(zcbl.Aspect,zcbl.Aspect.processClick);
        yue.on(yud.get('cssFoBox'),'click',zcbl.Aspect.clickEv.dispatch,zcbl.Aspect.clickEv,true);
        yue.on(yud.get('bannerBox'),'click',zcbl.Aspect.clickEv.dispatch,zcbl.Aspect.clickEv,true);
        yue.on(yud.get('logoFoBox'),'click',zcbl.Aspect.clickEv.dispatch,zcbl.Aspect.clickEv,true);
        yue.on(yud.get('logoBoBox'),'click',zcbl.Aspect.clickEv.dispatch,zcbl.Aspect.clickEv,true);
        yue.on(yud.get('logoPdfBox'),'click',zcbl.Aspect.clickEv.dispatch,zcbl.Aspect.clickEv,true);
        yue.on(yud.get('footerPdfBox'),'click',zcbl.Aspect.clickEv.dispatch,zcbl.Aspect.clickEv,true);
        zcbl.Aspect.loadBox("cssFo");
        zcbl.Aspect.loadBox("banner");
        zcbl.Aspect.loadBox("logoFo");
        zcbl.Aspect.loadBox("logoBo");
        zcbl.Aspect.loadBox("logoPdf");
        zcbl.Aspect.loadBox("footerPdf");
      },
      /**
      * @description The name of the method to call is the first part of the clicked item's ID, except for new season creation
      */
      processClick : function(e) {
        var target  = yue.getTarget(e);
        return (target.id||'_').split('_')[0];
      },
      loadBox : function(fileID) {
        zct.doAjaxCall("/aspect/" + fileID, null, function(o){
          yud.get(fileID + "Box").innerHTML = o.responseText;
        });
      },
      /**
      * @description Upload a new file
      */
      save : function(e) {
        var fileID = (yue.getTarget(e).id||'_').split('_')[1];
        var cont = yud.get('setupFormErrors_' + fileID);
        cont.innerHTML = "";
        var validform = zcv.check(yud.get('setupForm_' + fileID), cont);
        if (validform) {
          var target = yue.getTarget(e);
          zct.doAjaxFormSubmitCall('setupForm_' + fileID,[],function(o){
            zct.Notifier.processMessage('success', ylj.parse(o.responseText).success_msg, null, target);
            zcbl.Aspect.loadBox(fileID);
          }, true);
        }
      },
      /**
      * @description Revenir à l'ancienne version
      */
      rollback : function(e) {
        var fileID = (yue.getTarget(e).id||'_').split('_')[1]; //FIXME: Call more clean ConfirmationDialog
        new zct.ConfirmationDialog(content, function(){
          var target = yue.getTarget(e);
          zct.doAjaxCall("/rollback/" + fileID, null, function(o){
            zct.Notifier.processMessage('success', ylj.parse(o.responseText).success_msg, null, target);
            zcbl.Aspect.loadBox(fileID);
          });
        }).show(e);
      }
    };
  }();
  yue.onDOMReady(zcbl.Aspect.init);
}());
