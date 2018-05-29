package cn.appsys.controller;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.AppInfoService;
import cn.appsys.service.AppversionService;
import cn.appsys.tools.Constants;

@Controller
@RequestMapping("/appVersion")
public class AppversionController {
	private static Logger logger = Logger.getLogger(AppversionController.class);
	@Resource
	private AppInfoService appInfoService;
	@Resource
	private AppversionService appversionService;

	@RequestMapping(value = "/listversion")
	public String getVersionId(String id, Model model, AppVersion av) {
		logger.info("----------进入了");
		Integer appid = Integer.parseInt(id);
		List<AppVersion> versionList = appversionService.getAppVersionId(Integer.parseInt(id));
		for (AppVersion ss : versionList) {
			System.out.println(ss.getAppName()
					+ "+++++++++++++++++++++++++++++");
		}
		model.addAttribute("appVersionList", versionList);
		model.addAttribute("appid", appid);
		av.setAppId(appid);
		return "developer/appversionadd";
	}

	/**
	 * 增加appversion信息
	 * 
	 * @param appInfo
	 * @return
	 */
	@RequestMapping(value = "/appversionaddsave", method = RequestMethod.POST)
	public String addVersionSave(
			AppVersion appVersion,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "a_downloadLink", required = false) MultipartFile attach) {
		String downloadLink = null;
		String apkLocPath = null;
		String apkFileName = null;
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();// 原文件名
			logger.info("uploadFile oldFileName:" + oldFileName);
			String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
			logger.info("uploadFile prefix:" + prefix);
			if (prefix.equalsIgnoreCase("apk")) {
				String apkName = null;
				try {
					apkName = appInfoService.getAppFind(appVersion.getAppId(),
							null).getAPKName();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (apkName == null || "".equals(apkName)) {// 上传格式为apk,否则返回错误的提示信息
					return "redirect:/appVersion/appversionadd?id="
							+ appVersion.getAppId() + "&error=error1";
				}
				apkFileName = apkName + "-" + appVersion.getVersionNo()
						+ ".apk";
				File targetFile = new File(path, apkFileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/appVersion/appversionadd?id="
							+ appVersion.getAppId() + "&error=error2";
				}
				downloadLink = request.getContextPath()
						+ "/statics/uploadfiles/" + apkFileName;
				apkLocPath = path + File.separator + apkFileName;
			} else {
				return "redirect:/appVersion/appversionadd?id="
						+ appVersion.getAppId() + "&error=error3";
			}
		}
		appVersion.setCreatedBy(((DevUser) session
				.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setCreationDate(new Date());
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(apkFileName);
		try {
			if (appversionService.addVersion(appVersion)) {
				return "redirect:/appInfo/list";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "appversionadd";
	}
	/**
	 * 修改app版本信息,跳转版本修改页面
	 * 
	 * @param versionId
	 * @param appId
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value = "/appversionmodify", method = RequestMethod.GET)
	public String modifyVersion(@RequestParam("vid") String versionId,
			@RequestParam("aid") String appId, Model model) {
		AppVersion appVersion = null;
		List<AppVersion> applist = null;
		try {
			appVersion =(AppVersion) appversionService.getVerSionId(Integer
					.parseInt(versionId));
			applist = appversionService.getAppVersionId(Integer
					.parseInt(appId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute(appVersion);
		model.addAttribute("appVersionList", applist);
		return "developer/appversionmodify";
	}

	@RequestMapping(value = "/appversionmodifysave", method = RequestMethod.POST)
	public String modifyAppVersionSave(
			AppVersion appVersion,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "attach", required = false) MultipartFile attach) {
		logger.debug("===============12");
		String downloadLink = null;
		String apkLocPath = null;
		String apkFileName = null;
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();// 原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
			if (prefix.equalsIgnoreCase("apk")) {// apk格式
				String apkName = null;
				try {
					apkName = appInfoService.getAppFind(appVersion.getAppId(),
							null).getAPKName();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (apkName == null || "".equals(apkName)) { // 有异常则返回异常信息
					return "redirect:/appVersion/appversionmodify?vid="
							+ appVersion.getId() + "&aid="
							+ appVersion.getAppId() + "&error=error1";
				}
				apkFileName = apkName + "-" + appVersion.getVersionNo()
						+ ".apk";
				File targetFile = new File(path, apkFileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/appVersion/appversionmodify?vid="
							+ appVersion.getId() + "&aid="
							+ appVersion.getAppId() + "&error=error2";
				}
				downloadLink = request.getContextPath()
						+ "/statics/uploadfiles/" + apkFileName;
				apkLocPath = path + File.separator + apkFileName;
			} else {
				return "redirect:/appVersion/appversionmodify?vid="
						+ appVersion.getId() + "&aid=" + appVersion.getAppId()
						+ "&error=error3";
			}
		}
		appVersion.setModifyBy(((DevUser) session
				.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setModifyDate(new Date());
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(apkFileName);
		try {
			if (appversionService.updateVersion(appVersion)) {
				return "redirect:/appInfo/list";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "developer/appversionmodify";
	}
}
