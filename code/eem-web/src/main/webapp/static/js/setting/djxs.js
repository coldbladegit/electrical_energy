/**
 * 电价系数
 */
$(function(){
	init();
	function init()
	{
		initControlAction();
		initControlVal();
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
		    	doSaveAction();
		    	return false;
		    }
		});

		$('input').focus(function(){
			$('label.input_msg').hide();
			$('label.input_msg[for="' + $(this).attr('id')+ '"]').show();
		});
	}
	
	function initControlVal()
	{
		var search = {
			'startPage' : FIRST_PAGE,
			'perPageCnt' : MAX_COUNT
		};
		$.ajax({
			url: rootpath + '/' + PATH_DJXS + '/list',
			type : 'GET', 
			dataType: 'json',
			data : JSON.stringify(search),
		    contentType: 'application/json',
			complete : function(XHR, TS) {
				$('#datas tr[type="loading_msg"]').hide();
				if (TS == "success") {
					var ar = JSON.parse(XHR.responseText);
					if(ar.code == 0)
					{
						var temp = ar.data;console.log(g_curData);
					}
				}
				else
				{
					showSystemError();
				}
			}
		});
	}
	
	function doSaveAction()
	{
		var name = $('#name').val();

		var temp = {
			name : name
		};
		
		var ajaxType = 'POST';
		var msgTitle = '添加电价系数';
		if(g_curData != null)
		{
			ajaxType = 'PUT';
			msgTitle = '修改电价系数';
			temp.id = g_curData.id;
		}
		
		$('div.eem_window_close').click();
    	var progress = showProgress('正在保存电价系数');
    	
		$.ajax({
			url: rootpath + '/' + PATH_DJXS + '/info',
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
					}
					else
					{
						showDynamicMessage(STR_CONFIRM, msgTitle + '失败:' + ar.msg, MESSAGE_TYPE_ERROR);
					}
				}
				else
				{
					showSystemError();
				}
			}
		});
	}
});