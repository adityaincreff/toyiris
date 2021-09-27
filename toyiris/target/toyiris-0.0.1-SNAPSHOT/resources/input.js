function getAlgoUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	console.log(baseUrl);
	return baseUrl + "/api/algo";
}

function getAllParameters(){
    url = getAlgoUrl()+"/input";
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            $('#liquidation-cleanup-multiplier').val(data.liquidationMultiplier);
            $('#good-size').val(data.goodSize);
            $('#bad-size').val(data.badSize);
            var day = ("0" + data.date.dayOfMonth).slice(-2);
            var month = ("0" + (data.date.monthValue)).slice(-2);

            var today = data.date.year+"-"+(month)+"-"+(day);
            $('#input-end-date').val(today);
        }
    });
}



function addInputParameters(){
    url = getAlgoUrl()+"/input";
    var $form = $("#report-form");
	var json = toJson($form);
	console.log(json);
	if($('#good-size').val()==null || $('#bad-size').val()==null || $('#good-size').val()=="" || $('#bad-size').val()==""){
	    nativeToast({
              message: "Some of the entries are empty." ,
              position: 'north',
              timeout: 0,
              type: 'error',
              closeOnClick: true
        });
	}else{
        let successFx=function(){
            successMessageDisplay("Algo inputs added.");
        }
    ajaxRequest(url,'POST',json,successFx);
    }
}

function runModule(){
    url = getAlgoUrl();


    let successFx = function(){
        successMessageDisplay("Algo Run Successful.");
    }
    nativeToast({
                          message: "Algo is running." ,
                          position: 'north',
                          timeout: 0,
                          type: 'success',
                          closeOnClick: true
                    });

    ajaxRequest(url,'GET',1,successFx);
}
function init(){
    $('#input-parameters').click(addInputParameters);
    $('#run-module').click(runModule);
}

$(document).ready(getAllParameters);
$(document).ready(init);