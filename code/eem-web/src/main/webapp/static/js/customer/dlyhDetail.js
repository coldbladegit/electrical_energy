//@ sourceURL=dlyhDetail.js
function DlyhDetail(afterSaveCallbk, curData)
{
	var g_afterSaveCallbk = afterSaveCallbk;
	var g_curData = curData;
	init();
	function init()
	{
		initControlAction();
		inintControlVal();
	}
	
	function initControlAction()
	{
		$("#detail_form").validate({
		    highlight: function(element) {
		      $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
		    },
		    success: function(element) {
		      $(element).closest('.form-group').removeClass('has-error');
		    },
		    submitHandler : function(){
		    	if($('#name').val() == '')
		    	{
		    		showDynamicMessage(STR_CONFIRM, '未输入电力用户名称', MESSAGE_TYPE_ERROR);
		    		return false;
		    	}
		    	if($('#nickName').val() == '')
		    	{
		    		showDynamicMessage(STR_CONFIRM, '未输入电力用户简称', MESSAGE_TYPE_ERROR);
		    		return false;
		    	}
		    	if($('#address').val() == '')
		    	{
		    		showDynamicMessage(STR_CONFIRM, '未输入地址', MESSAGE_TYPE_ERROR);
		    		return false;
		    	}
		    	if($('#contactName').val() == '')
		    	{
		    		showDynamicMessage(STR_CONFIRM, '未输入联系人', MESSAGE_TYPE_ERROR);
		    		return false;
		    	}
		    	if($('#contactPhone').val() == '')
		    	{
		    		showDynamicMessage(STR_CONFIRM, '未输入联系电话', MESSAGE_TYPE_ERROR);
		    		return false;
		    	}
		    	return doSaveAction();
		    }
		});
		
		$('#industryType').append(getIndustryTypeSelectStr());
		$('.detail_search').niceSelect();
		$('#cancel').unbind('click').click(function(){
			$('div.eem_window_close').click();
		})
		
		$('input').focus(function(){
			$('label.input_msg').hide();
			$('label.input_msg[for="' + $(this).attr('id')+ '"]').show();
		});
	}
	
	function inintControlVal()
	{
		if(g_curData != null)
		{
			$('#name').val(getObjStr(g_curData.name));
			$('#nickName').val(getObjStr(g_curData.nickName));
			$('#address').val(getObjStr(g_curData.address));
			$('#contactName').val(getObjStr(g_curData.contactName));
			$('#contactPhone').val(getObjStr(g_curData.contactPhone));
			$('#contactPosition').val(getObjStr(g_curData.contactPosition));
			$('#contactEmail').val(getObjStr(g_curData.contactEmail));
			$('#fax').val(getObjStr(g_curData.fax));
			$('#natureType').val(g_curData.natureType);
			$('#natureType').niceSelect('update');
			$('#industryType').val(g_curData.industryType);
			$('#industryType').niceSelect('update');
			getAllProvinceSelecte('province', g_curData.province, 'city', g_curData.city);
		}
		else
		{
			getAllProvinceSelecte('province', null, 'city');
		}
	}
	
	function doSaveAction()
	{
		var name = $('#name').val();
		var nickName = $('#nickName').val();
		var industryType = $('#industryType').val();
		var province = $('#province').val();
		var city = $('#city').val();
		var address = $('#address').val();
		var natureType = $('#natureType').val();
		var contactName = $('#contactName').val();
		var contactPhone = $('#contactPhone').val();
		var contactPosition = $('#contactPosition').val();
		var contactEmail = $('#contactEmail').val();
		var fax = $('#fax').val();

		var temp = {
			name : name,
			nickName : nickName,
			industryType : industryType, 
			province : province,
			city : city,
			address : address,
			natureType : natureType,
			contactName : contactName,
			contactPhone : contactPhone,
			contactPosition : contactPosition,
			contactEmail : contactEmail,
			fax : fax
		};
		
		var ajaxType = 'POST';
		var msgTitle = '添加电力用户';
		if(g_curData != null)
		{
			ajaxType = 'PUT';
			msgTitle = '修改电力用户';
			temp.id = g_curData.id;
		}
		
		
    	var progress = showProgress('正在保存电力用户');
    	
		$.ajax({
			url: rootpath + '/' + PATH_DLYH + '/info',
			type : ajaxType,
			dataType: 'json',
		    contentType: 'application/json',
			data : JSON.stringify(temp),
			complete : function(XHR, TS) {
				hideProgress(progress);
				if (TS == "success") {
					var ar = JSON.parse(XHR.responseText);
					if(ar.code == 0)
					{
						showDynamicMessage(STR_CONFIRM, msgTitle + '成功', MESSAGE_TYPE_INFO);
						if(g_afterSaveCallbk != null)
						{
							g_afterSaveCallbk();
						}
						$('div.eem_window_close').click();
						return true;
					}
					else
					{
						//alert(msgTitle + '失败:' + ar.msg, MESSAGE_TYPE_ERROR);
						showDynamicMessage(STR_CONFIRM, msgTitle + '失败:' + ar.msg, MESSAGE_TYPE_ERROR);
					}
				}
				else
				{
					showSystemError();
				}
				return false;
			}
		});
	}
	
	return this;
}