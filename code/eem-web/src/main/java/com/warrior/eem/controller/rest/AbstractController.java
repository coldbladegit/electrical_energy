package com.warrior.eem.controller.rest;

import java.io.IOException;

import javax.persistence.EntityExistsException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warrior.eem.common.Result;
import com.warrior.eem.enums.CodeStatus;
import com.warrior.eem.exception.EemException;

/**
 * 抽象rest control类
 * 
 * @author seangan
 *
 */
public class AbstractController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final ObjectMapper om = new ObjectMapper();

	@ExceptionHandler(Exception.class)
	public void handleSysException(Exception e, HttpServletRequest req, HttpServletResponse rep) {
		try {
			ServletOutputStream out = rep.getOutputStream();
			rep.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			String res = om.writeValueAsString(Result.failure(CodeStatus.INTERNAL_SERVER_ERROR.getCode(),
					CodeStatus.INTERNAL_SERVER_ERROR.getDesc()));
			if (e instanceof EemException) {
				res = om.writeValueAsString(Result.failure(((EemException) e).getCode(), e.getMessage()));
			} else if (e instanceof AuthorizationException) {
				res = om.writeValueAsString(
						Result.failure(CodeStatus.FORBIDDEN.getCode(), CodeStatus.FORBIDDEN.getDesc()));
			} else if(e instanceof EntityExistsException) {
				res = om.writeValueAsString(Result.failure(((EemException) e).getCode(), "数据已存在"));
			} else {
				logger.error(e.getMessage(), e);
			}
			out.write(res.getBytes());
			out.flush();
		} catch (IOException e1) {
		}
	}
	
	/**
	 * id转换器
	 * @param id
	 * @return
	 */
	Long convertId(String id) {
		Long idNum = 0L;
		if (id == null || id.trim().length() == 0) {
			throw new EemException("id不能为空");
		}
		try {
			idNum = Long.valueOf(id);
		} catch (NumberFormatException e) {
			throw new EemException("id格式不对");
		}
		return idNum;
	}
}
