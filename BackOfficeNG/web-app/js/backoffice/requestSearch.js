(function(){

  var yue = YAHOO.util.Event;
  var yud = YAHOO.util.Dom;
  var yus = YAHOO.util.Selector;
  var zct = zenexity.libredemat.tools;
  zct.namespace("zenexity.libredemat.bong.request");
  var zcb = zenexity.libredemat.bong;
  var zcbr = zenexity.libredemat.bong.request;

  zcbr.search = function() {

    var zcc = zenexity.libredemat.common;

    var displayPaginator = function() {
      var myPaginator = new YAHOO.widget.Paginator({
        containers: ['pagination-top','pagination-bottom'],
        rowsPerPage : parseInt(yud.get('rowsPerPage').value),
        totalRecords: parseInt(yud.get('totalRecords').value),
        recordOffset: parseInt(yud.get('recordOffset').value),
        template : "{FirstPageLink} {PreviousPageLink} <span>{CurrentPageReport}</span> {NextPageLink} {LastPageLink}",
        previousPageLinkLabel : '&lt;',
        firstPageLinkLabel : '&lt;&lt;',
        nextPageLinkLabel : '&gt;',
        lastPageLinkLabel : '&gt;&gt;',
        pageReportTemplate : 'R&eacute;sultats {startRecord} &agrave; {endRecord} sur {totalRecords}'
      });

      function handlePaginatorChange(state) {
        yud.get('recordOffset').value = state.recordOffset;
        yud.get('requestForm').submit();
      }
      myPaginator.subscribe('changeRequest', handlePaginatorChange);
      myPaginator.render();
    };

    var initCalendars = function() {
      zcb.Calendar("creationDateFrom");
      zcb.Calendar("creationDateTo");
    };

    var initCsvXmlExporter = function() {
      yue.addListener(yud.get("export-csv"), "click", function() {
        var form = yud.get('requestForm');
        var searchAction = yud.getAttribute(form, "action");
        var exportAction = yud.getAttribute(this, "data-action");
        yud.setAttribute(form, "action", exportAction);
        form.submit();
        yud.setAttribute(form, "action", searchAction);
      });
      yue.addListener(yud.get("export-xml"), "click", function() {
          var form = yud.get('requestForm');
          var searchAction = yud.getAttribute(form, "action");
          var exportAction = yud.getAttribute(this, "data-action");
          yud.setAttribute(form, "action", exportAction);
          form.submit();
          yud.setAttribute(form, "action", searchAction);
        });
    };

    var sortSearchRequest = function(sortType) {
      yud.get('sortBy').value = sortType;
      yud.get('requestForm').submit();
    };

    var orderSearch = function(event) {
      yud.get('sortDir').value = yue.getTarget(event).id;
      yud.get('requestForm').submit()
    };

    var filterSearchRequest = function(filterType) {
      yud.get('filterBy').value = [yud.get('filterBy').value,
        '@', filterType, '=', yud.get(filterType).value].join('');
      // hack to reset request season filter when we change request type
      if (filterType === "requestTypeIdFilter") {
        yud.get("filterBy").value += "@requestSeasonIdFilter=";
      }
      yud.get('requestForm').action = yud.get('searchAction').value;
      yud.get('requestForm').submit();
    };

    return {
      init: function() {
        initCalendars();
        displayPaginator();
        initCsvXmlExporter();
        yue.on(yus.query('#requestSearchSorters input[type*=radio]'), 'click',
          function(e) {
            sortSearchRequest(yue.getTarget(e).id);
          }
        );
        yue.on(yus.query('#requestSearchOrder input[type="radio"]'), 'click', orderSearch);
        yue.on(yus.query('select[id*=Filter]'), 'change',
          function(e) {
            filterSearchRequest(yue.getTarget(e).id);
          }
        );
        yue.on(yud.get('requestForm'), 'submit', function(e) {
          yud.get('requestForm')['requesterLastName'].value=yud.get('requestForm')['requesterLastName'].value.trim()
          yud.get('requestForm')['subjectLastName'].value=yud.get('requestForm')['subjectLastName'].value.trim()
          yud.get('requestForm')['id'].value=yud.get('requestForm')['id'].value.trim()
          yud.get('requestForm')['homeFolderId'].value=yud.get('requestForm')['homeFolderId'].value.trim()
          yud.get('requestForm').action = yud.get('searchAction').value;
        })
      }
    };
  }();

  yue.onDOMReady(zcbr.search.init);
}());
