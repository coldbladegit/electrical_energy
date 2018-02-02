<%@page import="com.warrior.eem.util.ToolUtil"%>
<%@ page language="java"  pageEncoding="utf-8"%>
<%
	String basePath = ToolUtil.getBasePath(request);
%>
<h4 class="page-title detail_sub_title">
	当前位置：<span >客户</span> / <span >电源商</span>
</h4>
<div class="divider" style="margin-top: 6px;margin-bottom: 6px;"></div>
<form class="form-horizontal">
	<div>
		<div class="tab-content detail_content">
			<div class="row">
				<div class="col-md-3">
					<div class="form-group">
	                  <label class="col-sm-3 control-label">名称</label>
	                  <div class="col-sm-9">
	                   <input type="text" class="form-control" id="name">
	                  </div>
	                </div>
				</div>
				
				<div class="col-md-4">
					<div class="form-group">
	                  <label class="col-sm-2 control-label">所在城市</label>
	                  <div class="col-sm-5">
		                   <select class="select2" data-placeholder="请选择省份" id="province">
							<option value=""></option>
							<option value="apple">四川</option>
							<option value="orange">北京</option>
							<option value="grapes">上海</option>
							<option value="strawberry">云南</option>
						</select> 
					</div>
					<div class="col-sm-5">	
						<select class="select2" data-placeholder="请选择市区" id="city">
							<option value=""></option>
							<option value="apple">成都</option>
							<option value="orange">德阳</option>
							<option value="grapes">绵阳</option>
							<option value="strawberry">宜宾</option>
						</select>
	                  </div>
	                </div>
				</div>
				<div class="col-md-2">
					<button class="btn btn-primary" id="search" type="button">查询</button>
					<button class="btn btn-info" id="add" type="button">添加</button>
				</div>
			</div>

			<table class="table table-striped table-hover" style="margin-bottom:10px;border: 1px solid #ddd;background-color: #FFFFFF;">
				<thead>
					<tr>
						<th>电源商名称</th>
						<th>电源商简称</th>
						<th>所在城市</th>
						<th>电源类型</th>
						<th>年均发电量</th>
						<th>企业性质</th>
						<th>联系人</th>
						<th>联系电话</th>
						<th>联系人职务</th>
						<th>联系邮箱</th>
						<th>录入时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="datas">
					<tr>
						<td>测试电源商1</td>
						<td>电源商1</td>
						<td>四川成都</td>
						<td>火电</td>
						<td>2300万kWh</td>
						<td>国营企业</td>					
						<td>张三</td>
						<td>122222222</td>
						<td>经理</td>
						<td>test@tttt.com</td>
						<td>2017-04-17 15:11:05</td>
						<td>
							<a class="btn btn-primary btn-xs" style="margin-right: 20px;" flag="modify">修改</a>
							<a class="btn btn-danger btn-xs" flag="del">删除</a>
						</td>
					</tr>
					<tr>
						<td>测试电源商1</td>
						<td>电源商1</td>
						<td>四川成都</td>
						<td>火电</td>
						<td>2300万kWh</td>
						<td>国营企业</td>					
						<td>张三</td>
						<td>122222222</td>
						<td>经理</td>
						<td>test@tttt.com</td>
						<td>2017-04-17 15:11:05</td>
						<td>
							<a class="btn btn-primary btn-xs" style="margin-right: 20px;" flag="modify">修改</a>
							<a class="btn btn-danger btn-xs" flag="del">删除</a>
						</td>
					</tr>
					<tr>
						<td>测试电源商1</td>
						<td>电源商1</td>
						<td>四川成都</td>
						<td>火电</td>
						<td>2300万kWh</td>
						<td>国营企业</td>					
						<td>张三</td>
						<td>122222222</td>
						<td>经理</td>
						<td>test@tttt.com</td>
						<td>2017-04-17 15:11:05</td>
						<td>
							<a class="btn btn-primary btn-xs" style="margin-right: 20px;" flag="modify">修改</a>
							<a class="btn btn-danger btn-xs" flag="del">删除</a>
						</td>
					</tr>
					<tr>
						<td>测试电源商1</td>
						<td>电源商1</td>
						<td>四川成都</td>
						<td>火电</td>
						<td>2300万kWh</td>
						<td>国营企业</td>					
						<td>张三</td>
						<td>122222222</td>
						<td>经理</td>
						<td>test@tttt.com</td>
						<td>2017-04-17 15:11:05</td>
						<td>
							<a class="btn btn-primary btn-xs" style="margin-right: 20px;" flag="modify">修改</a>
							<a class="btn btn-danger btn-xs" flag="del">删除</a>
						</td>
					</tr>
					<tr>
						<td>测试电源商1</td>
						<td>电源商1</td>
						<td>四川成都</td>
						<td>火电</td>
						<td>2300万kWh</td>
						<td>国营企业</td>					
						<td>张三</td>
						<td>122222222</td>
						<td>经理</td>
						<td>test@tttt.com</td>
						<td>2017-04-17 15:11:05</td>
						<td>
							<a class="btn btn-primary btn-xs" style="margin-right: 20px;" flag="modify">修改</a>
							<a class="btn btn-danger btn-xs" flag="del">删除</a>
						</td>
					</tr>
					<tr>
						<td>测试电源商1</td>
						<td>电源商1</td>
						<td>四川成都</td>
						<td>火电</td>
						<td>2300万kWh</td>
						<td>国营企业</td>					
						<td>张三</td>
						<td>122222222</td>
						<td>经理</td>
						<td>test@tttt.com</td>
						<td>2017-04-17 15:11:05</td>
						<td>
							<a class="btn btn-primary btn-xs" style="margin-right: 20px;" flag="modify">修改</a>
							<a class="btn btn-danger btn-xs" flag="del">删除</a>
						</td>
					</tr>
					<tr>
						<td>测试电源商1</td>
						<td>电源商1</td>
						<td>四川成都</td>
						<td>火电</td>
						<td>2300万kWh</td>
						<td>国营企业</td>					
						<td>张三</td>
						<td>122222222</td>
						<td>经理</td>
						<td>test@tttt.com</td>
						<td>2017-04-17 15:11:05</td>
						<td>
							<a class="btn btn-primary btn-xs" style="margin-right: 20px;" flag="modify">修改</a>
							<a class="btn btn-danger btn-xs" flag="del">删除</a>
						</td>
					</tr>
				</tbody>
			</table>
			<div id="page">
			</div>	
		</div>
	</div>
</form>
<script type="text/javascript" src="<%=basePath%>static/js/customer/dys.js"></script>