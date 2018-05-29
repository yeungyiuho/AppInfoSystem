package cn.appsys.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.AppCategoryService;
import cn.appsys.service.AppInfoService;
import cn.appsys.service.AppversionService;
import cn.appsys.service.DataDictionaryService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;

@Controller
@RequestMapping("/appInfo")
public class Appinfocontroller {
	private static Logger logger = Logger.getLogger(Appinfocontroller.class);

	@Resource
	private AppInfoService appInfoService;

	@Resource
	private AppCategoryService appCategoryService;

	@Resource
	private DataDictionaryService dataDictionaryService;
	@Resource
	public AppversionService appverSion;

	public List<DataDictionary> getDataDictionaryList(String typeCode) {
		List<DataDictionary> dataDictionaryList = null;
		try {
			dataDictionaryList = dataDictionaryService
					.getDataDictionaryList(typeCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataDictionaryList;
	}

	@RequestMapping(value = "/datadictionarylist.json", method = RequestMethod.GET)
	@ResponseBody
	public List<DataDictionary> getDataDicList(@RequestParam String tcode) {
		logger.debug("getDataDicList tcode ============ " + tcode);
		return this.getDataDictionaryList(tcode);
	}

	// 获取类型列表

	public List<AppCategory> getCategoryList(String pid) {
		List<AppCategory> categoryLevelList = null;
		try {
			categoryLevelList = appCategoryService
					.getAppCategoryListByParentId(pid == null ? null : Integer
							.parseInt(pid));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categoryLevelList;
	}

	// 根据parentId查询出相应的分类级别列表

	@RequestMapping(value = "/categorylevellist.json", method = RequestMethod.GET)
	@ResponseBody
	public List<AppCategory> getAppCategoryList(@RequestParam String pid) {
		logger.debug("getAppCategoryList pid ============ " + pid);
		if (pid.equals(""))
			pid = null;
		return getCategoryList(pid);
	}

	@RequestMapping(value = "/list")
	public String getAppInfoList(
			Model model,
			HttpSession session,
			@RequestParam(value = "querySoftwareName", required = false) String querySoftwareName,
			@RequestParam(value = "queryStatus", required = false) String _queryStatus,
			@RequestParam(value = "queryCategoryLevel1", required = false) String _queryCategoryLevel1,
			@RequestParam(value = "queryCategoryLevel2", required = false) String _queryCategoryLevel2,
			@RequestParam(value = "queryCategoryLevel3", required = false) String _queryCategoryLevel3,
			@RequestParam(value = "queryFlatformId", required = false) String _queryFlatformId,
			@RequestParam(value = "pageIndex", required = false) String pageIndex) {
		logger.info("getAppInfoList -- > querySoftwareName: "
				+ querySoftwareName);
		logger.info("getAppInfoList -- > queryStatus: " + _queryStatus);
		logger.info("getAppInfoList -- > queryCategoryLevel1: "
				+ _queryCategoryLevel1);
		logger.info("getAppInfoList -- > queryCategoryLevel2: "
				+ _queryCategoryLevel2);
		logger.info("getAppInfoList -- > queryCategoryLevel3: "
				+ _queryCategoryLevel3);
		logger.info("getAppInfoList -- > queryFlatformId: " + _queryFlatformId);
		logger.info("getAppInfoList -- > pageIndex: " + pageIndex);

		Integer devId = ((DevUser) session
				.getAttribute(Constants.DEV_USER_SESSION)).getId();
		List<AppInfo> appInfoList = null;
		List<DataDictionary> statusList = null;
		List<DataDictionary> flatFormList = null;
		List<AppCategory> categoryLevel1List = null;
		List<AppCategory> categoryLevel2List = null;
		List<AppCategory> categoryLevel3List = null;
		// 页面容量
		int pageSize = Constants.pageSize;
		// 当前页码
		Integer currentPageNo = 1;

		if (pageIndex != null) {
			try {
				currentPageNo = Integer.valueOf(pageIndex);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		Integer queryStatus = null;
		if (_queryStatus != null && !_queryStatus.equals("")) {
			queryStatus = Integer.parseInt(_queryStatus);
		}
		Integer queryCategoryLevel1 = null;
		if (_queryCategoryLevel1 != null && !_queryCategoryLevel1.equals("")) {
			queryCategoryLevel1 = Integer.parseInt(_queryCategoryLevel1);
		}
		Integer queryCategoryLevel2 = null;
		if (_queryCategoryLevel2 != null && !_queryCategoryLevel2.equals("")) {
			queryCategoryLevel2 = Integer.parseInt(_queryCategoryLevel2);
		}
		Integer queryCategoryLevel3 = null;
		if (_queryCategoryLevel3 != null && !_queryCategoryLevel3.equals("")) {
			queryCategoryLevel3 = Integer.parseInt(_queryCategoryLevel3);
		}
		Integer queryFlatformId = null;
		if (_queryFlatformId != null && !_queryFlatformId.equals("")) {
			queryFlatformId = Integer.parseInt(_queryFlatformId);
		}

		// 总数量（表）
		int totalCount = 0;
		try {
			totalCount = appInfoService.getAppInfoCount(querySoftwareName,
					queryStatus, queryCategoryLevel1, queryCategoryLevel2,
					queryCategoryLevel3, queryFlatformId, devId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 总页数
		PageSupport pages = new PageSupport();
		pages.setCurrentPageNo(currentPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount = pages.getTotalPageCount();
		// 控制首页和尾页
		if (currentPageNo < 1) {
			currentPageNo = 1;
		} else if (currentPageNo > totalPageCount) {
			currentPageNo = totalPageCount;
		}
		try {
			appInfoList = appInfoService.getAppInfoList(querySoftwareName,
					queryStatus, queryCategoryLevel1, queryCategoryLevel2,
					queryCategoryLevel3, queryFlatformId, devId, currentPageNo,
					pageSize);
			// 获取相应词典列表
			statusList = this.getDataDictionaryList("APP_STATUS");
			flatFormList = this.getDataDictionaryList("APP_FLATFORM");
			categoryLevel1List = appCategoryService
					.getAppCategoryListByParentId(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("appInfoList", appInfoList);
		model.addAttribute("statusList", statusList);
		model.addAttribute("flatFormList", flatFormList);
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		model.addAttribute("pages", pages);
		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
		model.addAttribute("queryFlatformId", queryFlatformId);

		if (queryCategoryLevel2 != null && !queryCategoryLevel2.equals("")) {
			categoryLevel2List = getCategoryList(queryCategoryLevel1.toString());
			model.addAttribute("categoryLevel2List", categoryLevel2List);
		}
		if (queryCategoryLevel3 != null && !queryCategoryLevel3.equals("")) {
			categoryLevel3List = getCategoryList(queryCategoryLevel2.toString());
			model.addAttribute("categoryLevel3List", categoryLevel3List);
		}
		return "developer/appinfolist";
	}

	// 查看
	@Resource
	private AppversionService appversionService;

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable String id, Model model, AppVersion av) {
		logger.info("view id===========" + id);
		Integer appid = Integer.parseInt(id);
		List<AppVersion> versionList = appversionService
				.getAppVersionId(Integer.parseInt(id));
		for (AppVersion ss : versionList) {
			System.out.println(ss.getAppName()
					+ "+++++++++++++++++++++++++++++");
		}
		model.addAttribute("appVersionList", versionList);
		model.addAttribute("appid", appid);
		av.setAppId(appid);

		AppInfo info = appInfoService.getAppinfoId(id);
		model.addAttribute(info);
		return "developer/appinfoview";
	}

	// 跳入查看页面

	@RequestMapping(value = "viewapp")
	public String viewapp() {
		logger.info("======");
		return "developer/appinfoview";
	}

	// 跳入新增app信息页面

	@RequestMapping(value = "/addApp")
	public String addApp() {
		logger.info("++++++++++");
		return "developer/appinfoadd";
	}

	// 新增App信息

	@RequestMapping(value = "/appinfoaddsave", method = RequestMethod.POST)
	public String addAppInfoSave(
			AppInfo appinfo,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "a_logoPicPath", required = false) MultipartFile attach) {
		String logoPicPath = null;
		String logoLocPath = null;
		if (!attach.isEmpty()) {
			String path = request
					.getSession()
					.getServletContext()
					.getRealPath(
							"statics" + java.io.File.separator + "uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();// 原文件名
			logger.info("uploadFile oldFileName:" + oldFileName);
			String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
			logger.info("uploadFile prefix:" + prefix);
			int filesize = 500000;
			logger.debug("uploadFile size:" + attach.getSize());
			if (attach.getSize() > filesize) {// 上传大小不得超过 50k
				request.setAttribute("fileUploadError",
						Constants.FILEUPLOAD_ERROR_4);
				return "developer/appinfoadd";
			} else if (prefix.equalsIgnoreCase("jpg")
					|| prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jepg")
					|| prefix.equalsIgnoreCase("pneg")) {// 上传图片格式
				String fileName = appinfo.getAPKName() + ".jpg";// 上传LOGO图片命名:apk名称.apk
				File targetFile = new File(path, fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("fileUploadError",
							Constants.FILEUPLOAD_ERROR_2);
					return "developer/appinfoadd";
				}
				logoPicPath = request.getContextPath()
						+ "/statics/uploadfiles/" + fileName;
				logoLocPath = path + File.separator + fileName;
			} else {
				request.setAttribute("fileUploadError",
						Constants.FILEUPLOAD_ERROR_3);
				return "developer/appinfoadd";
			}
		}
		appinfo.setCreatedBy(((DevUser) session
				.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appinfo.setCreationDate(new Date());
		// 相对路径
		appinfo.setLogoPicPath(logoPicPath);
		// 绝对路径
		appinfo.setLogoLocPath(logoLocPath);
		appinfo.setDevId(((DevUser) session
				.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appinfo.setStatus(1);
		try {
			if (appInfoService.addAppinfo(appinfo)) {
				return "redirect:/appInfo/list";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "developer/appinfoadd";
	}

	@RequestMapping(value = "apkexist.json", method = RequestMethod.GET)
	@ResponseBody
	public Object apkName(@RequestParam String APKName) {
		logger.info("======" + APKName);
		HashMap<String, String> result = new HashMap<String, String>();
		if (StringUtils.isEmpty(APKName)) {
			result.put("APKName", "empty");
		} else {
			AppInfo info = appInfoService.getAppFind(null, APKName);
			if (null != info)
				result.put("APKName", "exist");
			else
				result.put("APKName", "noexist");
		}
		return JSONArray.toJSONString(result);
	}

	@RequestMapping(value = "/delapp.json", method = RequestMethod.GET)
	@ResponseBody
	public String delete(
			@RequestParam(value = "id", required = false) String appinfoid) {
		logger.info("hahahahahhahaah");
		HashMap<String, String> haha = new HashMap<String, String>();
		List<AppVersion> iu = appverSion.getAppVersionId(Integer
				.parseInt(appinfoid));
		if (iu.size() > 0) {
			logger.debug("集合个数===" + iu.size());
			if (appInfoService.deleteAppinfo(Integer.parseInt(appinfoid))) {
				haha.put("delResult", "true");
			} else {
				haha.put("delResult", "false");
			}
		} else {
			if (appInfoService.deleteAppVersion(appinfoid)) {
				haha.put("delResult", "true");
			} else {
				haha.put("delResult", "false");
			}
		}
		return JSONArray.toJSONString(haha);
	}

	// 修改app基本信息,跳转基本修改页面

	@RequestMapping(value = "/appinfomodify", method = RequestMethod.GET)
	public String modifyAppInfo(@RequestParam(value = "id") String appId,
			Model model) {
		AppInfo appInfo = null;

		try {
			appInfo = appInfoService.getAppFind(Integer.parseInt(appId), null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute(appInfo);
		return "developer/appinfomodify";
	}

	@RequestMapping(value = "/appinfomodifysave", method = RequestMethod.POST)
	public String modifyAppInfoSave(
			AppInfo appInfo,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "attach", required = false) MultipartFile attach) {
		String logoPicPath = null;
		String logoLocPath = null;
		String APKName = appInfo.getAPKName();
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();// 原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
			int filesize = 50000000;
			if (attach.getSize() > filesize) {
				return "redirect:/dev/flatform/app/appinfomodify?id="
						+ appInfo.getId() + "&error=error4";
			} else if (prefix.equalsIgnoreCase("jpg")
					|| prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jepg")
					|| prefix.equalsIgnoreCase("pneg")) {// 上传图片格式
				String fileName = APKName + ".jpg";// 上传LOGO图片命名:apk名称.apk
				File targetFile = new File(path, fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/dev/flatform/app/appinfomodify?id="
							+ appInfo.getId() + "&error=error2";
				}
				logoPicPath = request.getContextPath()
						+ "/statics/uploadfiles/" + fileName;
				logoLocPath = path + File.separator + fileName;
			} else {
				return "redirect:/dev/flatform/app/appinfomodify?id="
						+ appInfo.getId() + "&error=error3";
			}
		}
		appInfo.setModifyBy(((DevUser) session
				.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setModifyDate(new Date());
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setLogoPicPath(logoPicPath);
		appInfo.setDevId(1);
		appInfo.setStatus(1);
		try {
			if (appInfoService.updateAppinfo(appInfo)) {
				return "redirect:/appInfo/list";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "developer/appinfomodify";
	}

	@RequestMapping(value = "/delfile", method = RequestMethod.GET)
	@ResponseBody
	public Object delFile(
			@RequestParam(value = "flag", required = false) String flag,
			@RequestParam(value = "id", required = false) String id) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		String fileLocPath = null;
		if (flag == null || flag.equals("") || id == null || id.equals("")) {
			resultMap.put("result", "failed");

		} else if (flag.equals("logo")) {
			// 这是我在修改app基本信息中,删除logo图片
			try {
				fileLocPath = (appInfoService.getAppFind(Integer.parseInt(id),
						null)).getLogoLocPath();
				File file = new File(fileLocPath);
				if (file.exists()) // 检查是否存在
					if (file.delete()) { // 再进行删除本地文件删除

						// 删除显示的logo图片
						if (appInfoService.deleteAppVersion(id)) {

							resultMap.put("result", "success");
							logger.info("删除成功");
						}
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return JSONArray.toJSONString(resultMap);
	}

	@RequestMapping(value = "/sale.json", method = RequestMethod.POST)
	@ResponseBody
	public String updateStatu(@RequestParam("appid") String appid,
			@RequestParam("openOrClose") String openOrClose) {
		HashMap<String, String> haha = new HashMap<String, String>();
		int statu = 0;
		if ("open".equals(openOrClose)) {
			statu = 4;
			if (appInfoService.updateStatus(Integer.parseInt(appid), statu)) {
				haha.put("resultMsg", "success");
			} else {
				haha.put("resultMsg", "failed");
			}
		} else if ("close".equals(openOrClose)) {
			statu = 5;
			if (appInfoService.updateStatus(Integer.parseInt(appid), statu)) {
				haha.put("resultMsg", "success");
			} else {
				haha.put("resultMsg", "failed");
			}
		}
		return JSONArray.toJSONString(haha);
	}

	// 跳入后台
	@RequestMapping(value = "/Backendlist")
	public String getBackendAppInfoList(
			Model model,
			HttpSession session,
			@RequestParam(value = "querySoftwareName", required = false) String querySoftwareName,
			@RequestParam(value = "queryStatus", required = false) String _queryStatus,
			@RequestParam(value = "queryCategoryLevel1", required = false) String _queryCategoryLevel1,
			@RequestParam(value = "queryCategoryLevel2", required = false) String _queryCategoryLevel2,
			@RequestParam(value = "queryCategoryLevel3", required = false) String _queryCategoryLevel3,
			@RequestParam(value = "queryFlatformId", required = false) String _queryFlatformId,
			@RequestParam(value = "pageIndex", required = false) String pageIndex) {
		logger.info("getAppInfoList -- > querySoftwareName: "
				+ querySoftwareName);
		logger.info("getAppInfoList -- > queryStatus: " + _queryStatus);
		logger.info("getAppInfoList -- > queryCategoryLevel1: "
				+ _queryCategoryLevel1);
		logger.info("getAppInfoList -- > queryCategoryLevel2: "
				+ _queryCategoryLevel2);
		logger.info("getAppInfoList -- > queryCategoryLevel3: "
				+ _queryCategoryLevel3);
		logger.info("getAppInfoList -- > queryFlatformId: " + _queryFlatformId);
		logger.info("getAppInfoList -- > pageIndex: " + pageIndex);

		Integer devId = ((BackendUser) session
				.getAttribute(Constants.USER_SESSION)).getId();
		List<AppInfo> appInfoList = null;
		List<DataDictionary> statusList = null;
		List<DataDictionary> flatFormList = null;
		List<AppCategory> categoryLevel1List = null;
		List<AppCategory> categoryLevel2List = null;
		List<AppCategory> categoryLevel3List = null;
		// 页面容量
		int pageSize = Constants.pageSize;
		// 当前页码
		Integer currentPageNo = 1;

		if (pageIndex != null) {
			try {
				currentPageNo = Integer.valueOf(pageIndex);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		Integer queryStatus = null;
		if (_queryStatus != null && !_queryStatus.equals("")) {
			queryStatus = Integer.parseInt(_queryStatus);
		}
		Integer queryCategoryLevel1 = null;
		if (_queryCategoryLevel1 != null && !_queryCategoryLevel1.equals("")) {
			queryCategoryLevel1 = Integer.parseInt(_queryCategoryLevel1);
		}
		Integer queryCategoryLevel2 = null;
		if (_queryCategoryLevel2 != null && !_queryCategoryLevel2.equals("")) {
			queryCategoryLevel2 = Integer.parseInt(_queryCategoryLevel2);
		}
		Integer queryCategoryLevel3 = null;
		if (_queryCategoryLevel3 != null && !_queryCategoryLevel3.equals("")) {
			queryCategoryLevel3 = Integer.parseInt(_queryCategoryLevel3);
		}
		Integer queryFlatformId = null;
		if (_queryFlatformId != null && !_queryFlatformId.equals("")) {
			queryFlatformId = Integer.parseInt(_queryFlatformId);
		}

		// 总数量（表）
		int totalCount = 0;
		try {
			totalCount = appInfoService.getAppInfoCount(querySoftwareName,
					queryStatus, queryCategoryLevel1, queryCategoryLevel2,
					queryCategoryLevel3, queryFlatformId, devId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 总页数
		PageSupport pages = new PageSupport();
		pages.setCurrentPageNo(currentPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount = pages.getTotalPageCount();
		// 控制首页和尾页
		if (currentPageNo < 1) {
			currentPageNo = 1;
		} else if (currentPageNo > totalPageCount) {
			currentPageNo = totalPageCount;
		}
		try {
			appInfoList = appInfoService.getAppInfoList(querySoftwareName,
					queryStatus, queryCategoryLevel1, queryCategoryLevel2,
					queryCategoryLevel3, queryFlatformId, devId, currentPageNo,
					pageSize);
			// 获取相应词典列表
			statusList = this.getDataDictionaryList("APP_STATUS");
			flatFormList = this.getDataDictionaryList("APP_FLATFORM");
			categoryLevel1List = appCategoryService
					.getAppCategoryListByParentId(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("appInfoList", appInfoList);
		model.addAttribute("statusList", statusList);
		model.addAttribute("flatFormList", flatFormList);
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		model.addAttribute("pages", pages);
		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
		model.addAttribute("queryFlatformId", queryFlatformId);

		if (queryCategoryLevel2 != null && !queryCategoryLevel2.equals("")) {
			categoryLevel2List = getCategoryList(queryCategoryLevel1.toString());
			model.addAttribute("categoryLevel2List", categoryLevel2List);
		}
		if (queryCategoryLevel3 != null && !queryCategoryLevel3.equals("")) {
			categoryLevel3List = getCategoryList(queryCategoryLevel2.toString());
			model.addAttribute("categoryLevel3List", categoryLevel3List);
		}
		return "backend/applist";
	}

	// 后台查看

	@RequestMapping(value = "/appcheck", method = RequestMethod.GET)
	public String Backendview(String aid, String vid, Model model) {
		logger.info("view id===========" + aid);
		AppInfo info = appInfoService.getAppinfoId(aid);
		AppVersion ve = appverSion.getVerSionId(Integer.parseInt(vid));
		model.addAttribute("appInfo", info);
		model.addAttribute("appVersion", ve);
		return "backend/appcheck";
	}

	@RequestMapping(value = "/updateStutus")
	public String updateStutus(Integer id, Integer status) {
		if (appInfoService.updateStatus(id, status)) {
			return "redirect:/appInfo/Backendlist";
		}
		return "backend/appcheck";
	}
}
