package com.zy.controller.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.zy.entity.client.RecipientInfo;
import com.zy.entity.client.UserInfo;
import com.zy.entity.client.UserLogin;
import com.zy.entity.service.CargoInfo;
import com.zy.entity.service.OrderInfo;
import com.zy.service.client.DingDanService;
import com.zy.utils.MsgUtil;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("dingdan")
public class DdController {
	@Autowired
	private DingDanService dingDanService;

	@RequestMapping("index")
	public String index() {
		return "client/index";
	}

	/**
	 * 
	 * @MethodName: userinfo
	 * @param session
	 * @return String
	 * @Description: TODO
	 * @date 2020-07-16 07:07:21
	 */
	@RequestMapping("userinfo")
	public String userinfo(HttpSession session) {
		Object obj = session.getAttribute("userlogin");
		UserLogin userLogin = (UserLogin) obj;
		UserInfo userinfo = dingDanService.selectUserInfo(userLogin.getUserId());
		session.setAttribute("userinfo", userinfo);
		return "client/userinfo";
	}

	/**
	 * 
	 * @MethodName: send
	 * @return String
	 * @Description: TODO
	 * @date 2020-07-16 07:07:26
	 */
	@RequestMapping("send")
	public String send() {
		return "client/send";
	}

	/**
	 * 
	 * @MethodName: logout
	 * @param session
	 * @return String
	 * @Description: TODO
	 * @date 2020-07-16 07:07:30
	 */
	@ResponseBody
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		MsgUtil msg = new MsgUtil();
		session.invalidate();
		msg.setSuccess(true);
		return JSON.toJSONString(msg);
	}

	/**
	 * 
	 * @MethodName: wuliu
	 * @param session
	 * @param model
	 * @return String
	 * @Description: TODO
	 * @date 2020-07-16 07:07:34
	 */
	@RequestMapping("wuliu")
	public String wuliu(HttpSession session, Model model) {
		Object obj = session.getAttribute("wuliu");

		if (obj != null) {
			UserInfo userinfo = (UserInfo) obj;
			System.out.println(userinfo);
			int userInfoId = userinfo.getUserInfoId();
			List<OrderInfo> list = dingDanService.selectWuliu(userInfoId);
			model.addAttribute("OrderInfo", list);
			return "client/wuliu";
		} else {
			return "client/userinfo";
		}

	}

	/**
	 * 
	 * @MethodName: add
	 * @return String
	 * @Description: TODO
	 * @date 2020-07-16 07:07:38
	 */
	@RequestMapping("add")
	public String add() {
		return "client/recipientInfoadd";
	}

	@ResponseBody
	@RequestMapping("moren")
	public String moren(int recipientId, HttpSession session) {
		MsgUtil msg = new MsgUtil();
		RecipientInfo recipientInfo = dingDanService.selectByPrimaryKey(recipientId);
		if (recipientInfo != null) {
			session.setAttribute("recipientInfo", recipientInfo);
			msg.setSuccess(true);
		}
		return JSON.toJSONString(msg);
	}

	/**
	 * 
	 * @MethodName: recipientInfoadd
	 * @param recipientInfo
	 * @return String
	 * @Description: TODO
	 * @date 2020-07-16 07:07:41
	 */
	@ResponseBody
	@RequestMapping("recipientInfoadd")
	public String recipientInfoadd(RecipientInfo recipientInfo) {
		MsgUtil msg = new MsgUtil();
		try {
			dingDanService.insertRecipientInfo(recipientInfo);
			msg.setSuccess(true);
		} catch (Exception e) {
			msg.setSuccess(false);
			msg.setMessage("已存在此收件人，请修改收件人姓名！");
		}

		return JSON.toJSONString(msg);
	}

	/**
	 * 
	 * @MethodName: recipientInfoedit
	 * @param recipientInfo
	 * @return String
	 * @Description: TODO
	 * @date 2020-07-16 07:07:45
	 */
	@ResponseBody
	@RequestMapping("recipientInfoedit")
	public String recipientInfoedit(RecipientInfo recipientInfo) {
		MsgUtil msg = new MsgUtil();
		try {
			dingDanService.updateRecipientInfo(recipientInfo);
			msg.setSuccess(true);
		} catch (Exception e) {
			msg.setSuccess(false);
			msg.setMessage(e.getMessage());
		}

		return JSON.toJSONString(msg);
	}

	/**
	 * 
	 * @MethodName: edit
	 * @param recipientId
	 * @return ModelAndView
	 * @Description: TODO
	 * @date 2020-07-16 07:07:48
	 */
	@RequestMapping("edit")
	public ModelAndView edit(int recipientId) {
		ModelAndView mav = new ModelAndView();
		RecipientInfo recipientInfo = dingDanService.selectByPrimaryKey(recipientId);
		mav.setViewName("client/recipientInfoedit");
		mav.addObject("RecipientInfo", recipientInfo);
		return mav;

	}

	/**
	 * 
	 * @MethodName: delete
	 * @param recipientId
	 * @return String
	 * @Description: TODO
	 * @date 2020-07-16 07:07:52
	 */
	@ResponseBody
	@RequestMapping("delete")
	public String delete(int recipientId) {
		MsgUtil msg = new MsgUtil();
		try {
			dingDanService.deleteByrecipientId(recipientId);
			msg.setSuccess(true);
		} catch (Exception e) {
			msg.setSuccess(false);
			msg.setMessage(e.getMessage());
		}

		return JSON.toJSONString(msg);
	}

	/**
	 * 
	 * @MethodName: shoujianren
	 * @param session
	 * @return ModelAndView
	 * @Description: TODO
	 * @date 2020-07-16 07:07:55
	 */
	@RequestMapping("shoujianren")
	public ModelAndView shoujianren(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		Object obj = session.getAttribute("wuliu");
		if (obj != null) {
			UserInfo userinfo = (UserInfo) obj;
			int userInfoId = userinfo.getUserInfoId();
			List<RecipientInfo> list = dingDanService.selectAllRecipientInfo(userInfoId);
			mav.setViewName("client/shoujianren");
			mav.addObject("RecipientInfo", list);
			return mav;
		} else {
			mav.setViewName("client/userinfo");
			return mav;
		}

	}

	/**
	 * 
	 * @MethodName: dingdan
	 * @param cargoInfo
	 * @param serviceName
	 * @param recipientName
	 * @param userName
	 * @return String
	 * @Description: TODO
	 * @date 2020-07-16 07:07:59
	 */
	@ResponseBody
	@RequestMapping("dingdan")
	public String dingdan(CargoInfo cargoInfo, String serviceName, String recipientName, String userName) {
		MsgUtil msg = new MsgUtil();
		try {
			Integer serviceId = dingDanService.selectByServiceName(serviceName);
			if (serviceId != null) {
				dingDanService.insertCargoInfo(cargoInfo);
				int SendFreight = dingDanService.insertSend(cargoInfo.getCargoId(), recipientName, userName, serviceId,
						cargoInfo.getCargoWeight());
				DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				String orderId = df.format(new Date());
				dingDanService.insertOrderInfo(orderId, cargoInfo.getCargoId(), recipientName, userName);
				dingDanService.insertLogisticsInfo(orderId);
				msg.setSuccess(true);
				msg.setMessage("下单成功，您的订单号为" + orderId + "     总计：" + SendFreight + "元");

			} else {
				msg.setSuccess(false);
				msg.setMessage("下单失败，您的服务点地址不存在");
			}
		} catch (Exception e) {
			msg.setSuccess(false);
			msg.setMessage(e.getMessage());
		}
		return JSON.toJSONString(msg);
	}

}
