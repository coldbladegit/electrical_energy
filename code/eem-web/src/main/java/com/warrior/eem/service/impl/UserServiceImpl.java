package com.warrior.eem.service.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.warrior.eem.common.LoginResult;
import com.warrior.eem.dao.ElectricityPackageDao;
import com.warrior.eem.dao.IDao;
import com.warrior.eem.dao.PowerCustomerDao;
import com.warrior.eem.dao.RoleDao;
import com.warrior.eem.dao.UserDao;
import com.warrior.eem.dao.support.LogicalCondition;
import com.warrior.eem.dao.support.Order;
import com.warrior.eem.dao.support.Page;
import com.warrior.eem.dao.support.SimpleCondition;
import com.warrior.eem.dao.support.SqlRequest;
import com.warrior.eem.dao.support.Sql_Operator;
import com.warrior.eem.dao.support.Order.Order_Type;
import com.warrior.eem.entity.ElectricityPackage;
import com.warrior.eem.entity.PowerCustomer;
import com.warrior.eem.entity.Role;
import com.warrior.eem.entity.User;
import com.warrior.eem.entity.UserElectricityPackage;
import com.warrior.eem.entity.constant.UserStatus;
import com.warrior.eem.entity.constant.UserType;
import com.warrior.eem.entity.ui.Base64AndMD5Util;
import com.warrior.eem.entity.vo.ElectricityPackageVo;
import com.warrior.eem.entity.vo.PageVo;
import com.warrior.eem.entity.vo.UserCdtVo;
import com.warrior.eem.entity.vo.UserVo;
import com.warrior.eem.exception.EemException;
import com.warrior.eem.service.RoleService;
import com.warrior.eem.service.UserService;
import com.warrior.eem.util.EntityValidator;
import com.warrior.eem.util.ToolUtil;

/**
 * 系统用户服务
 * 
 * @author cold_blade
 * @version 1.0.0
 */
@Service
public class UserServiceImpl extends AbstractServiceImpl<User> implements UserService {
	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PowerCustomerDao customerDao;

	@Autowired
	private ElectricityPackageDao pkgDao;

	@Override
	IDao<User> getDao() {
		return userDao;
	}

	@Override
	@Transactional
	public User setRole(Long userId, Long roleId) {
		User user = userDao.getEntity(userId);
		if (null == user) {
			throw new EemException("无效的用户id：" + userId);
		}
		Role role = roleDao.getEntity(roleId);
		if (null == role) {
			throw new EemException("无效的权限id：" + userId);
		}
		user.setRole(role);
		userDao.updateDo(user);
		return user;
	}

	@Override
	@Transactional
	public void modifyPassword(Long userId, String oldPwd, String newPwd) {
		User user = (User) getEntity(userId);
		if (null == user) {
			throw new EemException("无效的用户id：" + userId);
		}
		if (!user.getPassword().equals(Base64AndMD5Util.encodeByBase64AndMd5(oldPwd))) {
			throw new EemException("密码不正确");
		}
		user.setPassword(Base64AndMD5Util.encodeByBase64AndMd5(newPwd));
		userDao.updateDo(user);
	}

	@Override
	@Transactional
	public User modifyName(Long userId, String newName) {
		User user = (User) getEntity(userId);
		if (null == user) {
			throw new EemException("无效的用户id：" + userId);
		}
		user.setName(newName);
		userDao.updateDo(user);
		return user;
	}

	@Override
	@Transactional
	public boolean containsElectricityPackage(Long userId, Long pkgId) {
		User user = userDao.getEntity(userId);
		if (null == user) {
			return false;
		}
		return user.containsElectricityPackage(pkgId);
	}

	@Override
	SqlRequest buildListSqlRequest(Serializable... conditions) {
		UserCdtVo cdt = (UserCdtVo) conditions[0];
		try {
			EntityValidator.checkEntity(cdt);
		} catch (IllegalAccessException | SecurityException e) {
			throw new EemException("用户列表查询条件解析失败");
		}
		SqlRequest req = new SqlRequest();
		req.setPage(new Page(cdt.getStartPage(), cdt.getPerPageCnt()));
		Order order = new Order();
		order.addOrder("id", Order_Type.ASC);
		req.setOrder(order);
		LogicalCondition sqlCdt = LogicalCondition.emptyOfTrue();
		if (!ToolUtil.isStringEmpty(cdt.getName())) {
			sqlCdt = sqlCdt.and(SimpleCondition.like("name", "%" + cdt.getName() + "%"));
		}
		sqlCdt = sqlCdt.and(new SimpleCondition("status", Sql_Operator.EQ, UserStatus.ACTIVE));
		sqlCdt = sqlCdt.and(new SimpleCondition("name", Sql_Operator.NOT_EQ, "admin"));
		req.setCdt(sqlCdt);
		return req;
	}

	@Override
	SqlRequest buildCountSqlRequest(Serializable... conditions) {
		return null;
	}

	@Override
	User convertVoToDoForUpdate(Serializable dbo, Serializable vo) {
		return null;
	}

	@Override
	User convertVoToDoForCreate(Serializable vo) {
		User user = new User();
		UserVo userVo = (UserVo) vo;
		user.setName(userVo.getName());
		user.setPassword(Base64AndMD5Util.encodeByBase64AndMd5(userVo.getPassword()));
		user.setType(UserType.convert(userVo.getType()));
		if (UserType.ELECTRICITY == user.getType()) {
			PowerCustomer customer = customerDao.getEntity(userVo.getCustomerId());
			if (null != customer) {
				user.setCustomer(customer);
			}
			Role role = roleService.queryCommonRole();
			if (null != role) {
				user.setRole(role);
			}
		} else {
			Role role = roleDao.getEntity(userVo.getRoleId());
			if (null != role) {
				user.setRole(role);
			}
		}
		Timestamp time = ToolUtil.getCurrentTime();
		user.setAddTime(time);
		user.setStatus(UserStatus.ACTIVE);
		return user;
	}

	@Override
	@Transactional
	public void deleteEntity(Serializable id) {
		User user = (User) getEntity(id);
		if (null == user) {
			throw new EemException("无效的用户id：" + id);
		}
		user.setStatus(UserStatus.DISABLE);
		userDao.updateDo(user);
	}

	@Override
	@Transactional
	public boolean createAdminIfAbsent() {
		if (checkExistAdminUser()) {
			return true;
		}
		userDao.createDo(buildAdmin());
		return checkExistAdminUser();
	}

	@Override
	@Transactional
	public PageVo listEntities(Serializable... conditions) {
		PageVo pageVo = super.listEntities(conditions);
		List<UserVo> vos = new ArrayList<UserVo>();
		for (Object obj : pageVo.getDatas()) {
			vos.add(((User) obj).convert());
		}
		pageVo.setDatas(vos);
		return pageVo;
	}

	private boolean checkExistAdminUser() {
		SqlRequest req = new SqlRequest();
		SimpleCondition scdt = new SimpleCondition("name", Sql_Operator.EQ, "admin");
		req.setCdt(scdt);
		return userDao.countDos(req) > 0;
	}

	private User buildAdmin() {
		User admin = new User();
		admin.setName("admin");
		admin.setPassword(Base64AndMD5Util.encodeByBase64AndMd5("mirror-0"));
		admin.setStatus(UserStatus.ACTIVE);
		admin.setType(UserType.SYSTEM);
		Role adminRole = queryAdminRole();
		if (null != adminRole) {
			admin.setRole(adminRole);
		}
		Timestamp time = ToolUtil.getCurrentTime();
		admin.setAddTime(time);
		admin.setLastLoginTime(time);
		return admin;
	}

	private Role queryAdminRole() {
		SqlRequest req = new SqlRequest();
		req.setCdt(new SimpleCondition("name", Sql_Operator.EQ, "管理员"));
		List<?> roles = roleDao.listDos(req);
		return roles.isEmpty() ? null : (Role) roles.get(0);
	}

	@Override
	@Transactional
	public void handleElectricityPackage(Long userId, Long pkgId) {
		User user = userDao.getEntity(userId);
		if (null == user) {
			throw new EemException("无效的用户id：" + userId);
		}
		ElectricityPackage pkg = pkgDao.getEntity(pkgId);
		if (null == pkg) {
			throw new EemException("无效的套餐id：" + pkgId);
		}
		user.handleElectricityPackage(pkg);
	}

	@Override
	@Transactional
	public void cancelElectricityPackage(Long userId, Long pkgId) {
		User user = userDao.getEntity(userId);
		if (null == user) {
			throw new EemException("无效的用户id：" + userId);
		}
		user.cancelElectricityPackage(pkgId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElectricityPackageVo> getElectricityPackages(Long userId) {
		User user = userDao.getEntity(userId);
		if (null == user) {
			throw new EemException("无效的用户id：" + userId);
		}
		List<UserElectricityPackage> pkgs = user.getElectricityPackages();
		if (pkgs.isEmpty()) {
			return new ArrayList<>();
		}
		List<ElectricityPackageVo> vos = new ArrayList<>(pkgs.size());
		ElectricityPackageVo vo;
		for (UserElectricityPackage pkg : pkgs) {
			vo = pkg.getPkg().convert();
			vo.setHandled(true);
			vo.setHandleTime(ToolUtil.formatTime(pkg.getAddTime()));
			vos.add(vo);
		}
		return vos;
	}

	@Override
	@Transactional(readOnly = true)
	public LoginResult login(String name, String pwd) {
		SqlRequest req = new SqlRequest();
		LogicalCondition c = LogicalCondition.emptyOfTrue();
		c = c.and(new SimpleCondition("name", Sql_Operator.EQ, name));
		c = c.and(new SimpleCondition("password", Sql_Operator.EQ, Base64AndMD5Util.encodeByBase64AndMd5(String
				.valueOf(pwd))));
		req.setCdt(c);
		List<?> users = userDao.listDos(req);
		if (users == null || users.size() == 0) {
			throw new EemException("用户名或者密码有误");
		}
		LoginResult result = new LoginResult();
		User user = (User) users.get(0);
		result.setUser(user);
		if (null != user.getRole()) {
			result.setAuthorities(user.getRole().getAuthorities());
		}
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public UserVo getEntityVo(Long id) {
		User user = userDao.getEntity(id);
		if (null == user) {
			throw new EemException("未找到id（" + id + "）对应的数据");
		}
		return user.convert();
	}
}
