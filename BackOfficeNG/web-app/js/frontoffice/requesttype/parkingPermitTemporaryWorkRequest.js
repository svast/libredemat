zenexity.libredemat.tools.namespace('zenexity.libredemat.fong.requesttype');

(function () {

  var zcf = zenexity.libredemat.fong;
  var zcv = zenexity.libredemat.Validation;
  var zcfr = zcf.requesttype;

  var yu = YAHOO.util;
  var yud = yu.Dom;
  var yue = YAHOO.util.Event;
  var yl = YAHOO.lang;

  zcfr.ParkingPermitTemporaryWorkRequest = function () {

    var getDate = function (dayAdded, date) {
      var d = new Date();
      d.setHours(0,0,0,0);
      if (date != null) {
        var dates = date.split("/");
        d = new Date(dates[2] + "/" + dates[1] + "/" + dates[0]);
      }
      return new Date(d.getTime() + (1000 * 60 * 60 * 24 * dayAdded));
    };

    var getMinDate = function (target) {
      var classes = yud.getAttribute(target, "class").split(" ");
      var mindate = target.id === 'scaffoldingStartDateShow' ? 
        getDate(zenexity.libredemat.pptwrSpecificConfigurationData.minDaysBeforeScaffolding) :
        getDate(zenexity.libredemat.pptwrSpecificConfigurationData.minDaysBeforeFloorOccupation);
      for (var i = 0; i < classes.length; i++) {
        if (classes[i].indexOf('notBeforeDate_') > -1) {
          var inputLabel = classes[i].split("_")[1];
          if (inputLabel != undefined && inputLabel != "null" && yud.get(inputLabel) != null) {
            var inputValue = yud.get(inputLabel).value;
            if (inputValue != undefined && inputValue != "")
              mindate = getDate(0, inputValue);
          }
        }
      }
      return mindate;
    };

//    var isDisabled = function (target) {
//      var classes = yud.getAttribute(target, "class");
//      classes = classes.split(" ");
//      for (var i = 0; i < classes.length; i++) {
//        if (classes[i].indexOf('disabledWith_') > -1) {
//          if (classes[i].split("_").length > 1) {
//            var inputLabel = classes[i].split("_")[1];
//            if (inputLabel != undefined && inputLabel != "null" && yud.get(inputLabel) != null) {
//              var inputValue = yud.get(inputLabel).value;
//              return !(inputValue != undefined && inputValue != "");
//
//            }
//          }
//        }
//      }
//      return false;
//    };
//
//    var disabledFields = function (targetElementName) {
//      if (yud.get('work') === null)
//        return;
//      var periodeEnd = yud.get(targetElementName);
//      var periodeEndShow = yud.get(targetElementName + "Show");
//      if (isDisabled(periodeEndShow)) {
//        periodeEnd.disabled = true;
//        yud.setStyle(targetElementName + "Show", "cursor", "default");
//        periodeEndShow.disabled = true;
//      }
//      else {
//        yue.on(periodeEndShow, 'click', zcfr.ParkingPermitTemporaryWorkRequest.processClickEnd,
//            zcfr.ParkingPermitTemporaryWorkRequest.processClickEnd, true);
//        periodeEnd.disabled = false;
//        yud.setStyle(targetElementName + "Show", "cursor", "pointer");
//        periodeEndShow.disabled = false;
//      }
//    };

    var parseDate = function(str) {
      var mdy = str.split('/');
      return new Date(mdy[2], mdy[1]-1, mdy[0]);
    };

    var dayDiff = function(first, second) {
      return (second-first)/(1000*60*60*24);
    };

    var updateScaffoldingPrice = function() {
      if (!yud.get('scaffoldingPrice')) {
        var elem = yud.getLastChild('ScaffoldingInformation');
        var newNode = document.createElement('div');
        newNode.id = 'scaffoldingPrice';
        newNode.setAttribute('class', 'prices-information');
        newNode.innerHTML = "Tarification : " +
          "<span id='scaffoldingFixedInformation'>" +
            zenexity.libredemat.pptwrSpecificConfigurationData.scaffoldingPrice + " €</span> " +
          "x <span id='scaffoldingLengthInformation'>0</span> ml " +
          "x <span id='scaffoldingDurationInformation'>0</span> jours " +
          "= <span id='scaffoldingTotalPrice'>0</span> <span class='price-display'>€</span> TTC";
        yud.insertAfter(newNode, elem);
      }

      if (zcv.rules['numeric'].regex.test(yud.get('scaffoldingLength').value))
        yud.get('scaffoldingLengthInformation').innerHTML = yud.get('scaffoldingLength').value;
      else
        yud.get('scaffoldingLengthInformation').innerHTML = 0;
      
      if (zcv.rules['date'].func(yud.get('scaffoldingStartDate'))
          && zcv.rules['date'].func(yud.get('scaffoldingEndDate'))) {
        var numberOfDays = dayDiff(parseDate(yud.get('scaffoldingStartDate').value),
                                   parseDate(yud.get('scaffoldingEndDate').value));
        yud.get('scaffoldingDurationInformation').innerHTML = numberOfDays + 1;
      } else {
        yud.get('scaffoldingDurationInformation').innerHTML = 0;
      }

      yud.get('scaffoldingTotalPrice').innerHTML =
          (parseFloat(zenexity.libredemat.pptwrSpecificConfigurationData.scaffoldingPrice.replace(",", ".")) *
          parseFloat(yud.get('scaffoldingLengthInformation').innerHTML.replace(',','.')) *
          parseFloat(yud.get('scaffoldingDurationInformation').innerHTML)).toFixed(2);

      displayTotalPriceInformation();
    };

    var updateOccupationPrice = function() {
      if (!yud.get('occupationPrice')) {
        var elem = yud.getLastChild('VehicleParkingOrFloorOccupationInformation');
        var newNode = document.createElement('div');
        newNode.id = 'occupationPrice';
        newNode.setAttribute('class', 'prices-information');
        newNode.innerHTML = "Tarification : " +
            "<span id='occupationFixedInformation'>" +
            zenexity.libredemat.pptwrSpecificConfigurationData.floorOccupationPrice + " €</span> " +
            "x <span id='occupationSurfaceInformation'>0</span> m² " +
            "x <span id='occupationDurationInformation'>0</span> jours " +
            "= <span id='occupationTotalPrice'>0</span> <span class='price-display'>€</span> TTC";
        yud.insertAfter(newNode, elem);
      }

      if (zcv.rules['numeric'].regex.test(yud.get('occupation').value))
        yud.get('occupationSurfaceInformation').innerHTML = yud.get('occupation').value;
      else
        yud.get('occupationSurfaceInformation').innerHTML = 0;

      if (zcv.rules['date'].func(yud.get('occupationStartDate'))
          && zcv.rules['date'].func(yud.get('occupationEndDate'))) {
        var numberOfDays = dayDiff(parseDate(yud.get('occupationStartDate').value),
            parseDate(yud.get('occupationEndDate').value));
        yud.get('occupationDurationInformation').innerHTML = numberOfDays + 1;
      } else {
        yud.get('occupationDurationInformation').innerHTML = 0;
      }

      yud.get('occupationTotalPrice').innerHTML =
          (parseFloat(zenexity.libredemat.pptwrSpecificConfigurationData.floorOccupationPrice.replace(",", ".")) *
          parseFloat(yud.get('occupationSurfaceInformation').innerHTML.replace(',','.')) *
          parseFloat(yud.get('occupationDurationInformation').innerHTML)).toFixed(2);

      displayTotalPriceInformation();
    };

    var displayTotalPriceInformation = function() {
      var elem = yud.get('Observations-header-information');

      var pricesInformationNode;
      if (!yud.get('pricesInformation')) {
        pricesInformationNode = document.createElement('div');
        pricesInformationNode.id = 'pricesInformation';
        pricesInformationNode.setAttribute('class', 'prices-information');
        pricesInformationNode.innerHTML = "Un droit fixe de <span class='price-display'>" +
            zenexity.libredemat.pptwrSpecificConfigurationData.fixedChargePrice + " €</span> " +
            "est dû pour l'établissement de toute autorisation. Tout dépassement des surfaces ou durées autorisées " +
            "sera facturé par la ville au taux de <span class='price-display'>" +
            zenexity.libredemat.pptwrSpecificConfigurationData.exceedingPrice + " €</span> " +
            "par m² et par semaine (minimum de durée).";
        yud.insertBefore(pricesInformationNode, elem);
      } else {
        pricesInformationNode = yud.get('pricesInformation');
      }

      if (!yud.get('totalPrice')) {
        var newNode = document.createElement('div');
        newNode.id = 'totalPrice';
        newNode.setAttribute('class', 'prices-information');
        newNode.innerHTML = "Soit une estimation de <span id='totalPriceInformation'> 0 €</span> " +
            "qui sera à régler via ce télé-service par carte bleue après validation et confirmation par nos services.";
        yud.insertAfter(newNode, pricesInformationNode);
      }

      var scaffoldingTotalPrice = (yud.inDocument('scaffoldingTotalPrice') ? yud.get('scaffoldingTotalPrice').innerHTML : 0) *
          yud.get('scaffolding_yes').checked;
      var occupationTotalPrice = (yud.inDocument('occupationTotalPrice') ? yud.get('occupationTotalPrice').innerHTML : 0) *
          yud.get('vehicleParkingOrFloorOccupation_yes').checked;
      var totalPrice =
          parseFloat(zenexity.libredemat.pptwrSpecificConfigurationData.fixedChargePrice.replace(",", ".")) +
          parseFloat(scaffoldingTotalPrice) +
          parseFloat(occupationTotalPrice);
      yud.get('totalPriceInformation').innerHTML = totalPrice.toFixed(2) + ' €';
    };

    var createErrorField = function(elem, date, start) {
        var error = document.createElement('p');
        error.id= elem.name + 'Error';
        error.className = 'error';
        if(start) {
          error.innerHTML = 'La date minimum autorisée est le ' + date.getDate() + '/' + (date.getMonth()+1) + '/' + date.getFullYear();
        } else {
          error.innerHTML = 'La date de fin de période ne peut être antérieure à la date de début';
        }
        elem.parentNode.parentNode.parentNode.parentNode.parentNode.insertBefore(error,elem.parentNode.parentNode.parentNode.parentNode.nextSibling);
      };

    var removeErrorField = function(elem) {
      var error = yud.get(elem.name + 'Error');
      if( !yl.isNull(error)) {
        error.parentNode.removeChild(error)
      }
    };

    return {
      init: function () {
        yue.on(yud.get('scaffoldingStartDateShow'), 'click', zcfr.ParkingPermitTemporaryWorkRequest.processClickStart,
            zcfr.ParkingPermitTemporaryWorkRequest.processClickStart, true);
        yue.on(yud.get('occupationStartDateShow'), 'click', zcfr.ParkingPermitTemporaryWorkRequest.processClickStart,
            zcfr.ParkingPermitTemporaryWorkRequest.processClickStart, true);
        yue.on(yud.get('scaffoldingEndDateShow'), 'click', zcfr.ParkingPermitTemporaryWorkRequest.processClickEnd,
                zcfr.ParkingPermitTemporaryWorkRequest.processClickEnd, true);
        yue.on(yud.get('occupationEndDateShow'), 'click', zcfr.ParkingPermitTemporaryWorkRequest.processClickEnd,
                zcfr.ParkingPermitTemporaryWorkRequest.processClickEnd, true);

        yue.on(['scaffoldingLength', 'scaffoldingStartDate', 'scaffoldingEndDate'], 'change', function() {
          updateScaffoldingPrice();
        });
        updateScaffoldingPrice();

        yue.on(['occupation', 'occupationStartDate', 'occupationEndDate'], 'change', function() {
          updateOccupationPrice();
        });
        updateOccupationPrice();

        yue.on(['scaffolding_yes', 'scaffolding_no', 'vehicleParkingOrFloorOccupation_yes', 'vehicleParkingOrFloorOccupation_no'],
            'click', function() {
          displayTotalPriceInformation();
        });
        displayTotalPriceInformation();
        yud.removeClass(yud.get('occupationStartDate'),'validate-calendar');
        yud.removeClass(yud.get('occupationEndDate'),'validate-calendar');
        yud.addClass(yud.get('occupationStartDate'),'validate-checkDateStartOccupation');
        yud.addClass(yud.get('occupationEndDate'),'validate-checkDateEndOccupation');

        yud.removeClass(yud.get('scaffoldingStartDate'),'validate-calendar');
        yud.removeClass(yud.get('scaffoldingEndDate'),'validate-calendar');
        yud.addClass(yud.get('scaffoldingStartDate'),'validate-checkDateStartScaffolding');
        yud.addClass(yud.get('scaffoldingEndDate'),'validate-checkDateEndScaffolding');

        zcv.putRules({
            "checkDateStartOccupation" : new zcv.rule("func", function(f) {
              var minDate = getDate(zenexity.libredemat.pptwrSpecificConfigurationData.minDaysBeforeFloorOccupation);
              var startDate = getDate(0,yud.get('occupationStartDate').value);
              if(minDate > startDate && yl.isNull(yud.get('occupationStartDateError'))) {
                  createErrorField(yud.get('occupationStartDate'), minDate, true);
                } else if(minDate <= startDate) {
                  removeErrorField(yud.get('occupationStartDate'));
                }
              return (minDate <= startDate);
            }),
            "checkDateEndOccupation" : new zcv.rule("func", function(f) {
                var startDate = getDate(0,yud.get('occupationStartDate').value);
                var endDate = getDate(0,yud.get('occupationEndDate').value);
                if(startDate > endDate && yl.isNull(yud.get('occupationEndDateError'))) {
                  createErrorField(yud.get('occupationEndDate'), startDate)
                } else if(startDate <= endDate) {
                  removeErrorField(yud.get('occupationEndDate'));
                }
                return (startDate <= endDate);
            }),
            "checkDateStartScaffolding" : new zcv.rule("func", function(f) {
                var minDate = getDate(zenexity.libredemat.pptwrSpecificConfigurationData.minDaysBeforeScaffolding);
                var startDate = getDate(0,yud.get('scaffoldingStartDate').value);
                if(minDate > startDate && yl.isNull(yud.get('scaffoldingStartDateError'))) {
                    createErrorField(yud.get('scaffoldingStartDate'), minDate, true);
                  } else if(minDate <= startDate) {
                    removeErrorField(yud.get('scaffoldingStartDate'));
                  }
                return (minDate <= startDate);
            }),
            "checkDateEndScaffolding" : new zcv.rule("func", function(f) {
                var startDate = getDate(0,yud.get('scaffoldingStartDate').value);
                var endDate = getDate(0,yud.get('scaffoldingEndDate').value);
                if(startDate > endDate && yl.isNull(yud.get('scaffoldingEndDateError'))) {
                  createErrorField(yud.get('scaffoldingEndDate'), startDate)
                } else if(startDate <= endDate) {
                  removeErrorField(yud.get('scaffoldingEndDate'));
                }
                return (startDate <= endDate);
             })
            });
      },
      /**
       * @description The name of the method to call is the first part of the
       *              clicked item's ID, except for new season creation
       */
      processClickStart: function (e) {
        // Pour les options voir le fichier calendar_fo.js
        var target = yue.getTarget(e);
        var targetElementName = target.id.replace("Show","");
        var options =
        {
          close: true,
          title: "Date de début ",
          minDate: getMinDate(target)
        };
        zcf.Calendar(targetElementName, options, zcfr.ParkingPermitTemporaryWorkRequest.callBack);
      },

      processClickEnd: function (e) {
        // Pour les options voir le fichier calendar_fo.js
        var target = yue.getTarget(e);
        var targetElementName = target.id.replace("Show","");
        var options =
        {
          close: true,
          title: "Date de fin ",
          minDate: getMinDate(target)
        };
        zcf.Calendar(targetElementName, options, zcfr.ParkingPermitTemporaryWorkRequest.callBack);
      },

      callBack: function () {
        updateScaffoldingPrice();
        updateOccupationPrice();
        displayTotalPriceInformation();
      }
    }
  }();
}());
